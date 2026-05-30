package barberiaapp.persistence.repository;

import barberiaapp.domain.Cliente;
import barberiaapp.persistence.mapper.ClienteRowMapper;
import barberiaapp.services.outputport.ClientePersistencePort;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteRepositoryAdapterMySql implements ClientePersistencePort {

    private final Connection connection;
    private final ClienteRowMapper rowMapper;

    public ClienteRepositoryAdapterMySql(Connection connection, ClienteRowMapper rowMapper) {
        this.connection = connection; this.rowMapper = rowMapper;
    }

    @Override
    public Cliente guardarCliente(Cliente c) {
        String sql = "INSERT INTO cliente (nombre, apellido, email, password, estado, saldo, puntos, tipo_cliente, es_recurrente) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, c);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) c.setId(keys.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar cliente: " + e.getMessage(), e);
        }
        return c;
    }

    @Override
    public Optional<Cliente> buscarClientePorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(rowMapper.mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(rowMapper.mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Cliente actualizarCliente(Cliente c) {
        String sql = "UPDATE cliente SET nombre=?,apellido=?,email=?,password=?,estado=?,saldo=?,puntos=?,tipo_cliente=?,es_recurrente=? WHERE id_cliente=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParams(ps, c);
            ps.setInt(10, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cliente: " + e.getMessage(), e);
        }
        return c;
    }

    @Override
    public void eliminarCliente(int id) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cliente: " + e.getMessage(), e);
        }
    }

    private void setParams(PreparedStatement ps, Cliente c) throws SQLException {
        ps.setString(1, c.getNombre());
        ps.setString(2, c.getApellido());
        ps.setString(3, c.getEmail());
        ps.setString(4, c.getPassword());
        ps.setBoolean(5, c.isEstado());
        ps.setDouble(6, c.getSaldo());
        ps.setDouble(7, c.getPuntos());
        ps.setString(8, c.getTipoCliente());
        ps.setBoolean(9, c.isEsRecurrente());
    }
}

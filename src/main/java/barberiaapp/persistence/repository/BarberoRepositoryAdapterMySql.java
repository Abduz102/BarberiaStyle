package barberiaapp.persistence.repository;

import barberiaapp.domain.Barbero;
import barberiaapp.persistence.mapper.BarberoRowMapper;
import barberiaapp.services.outputport.BarberoPersistencePort;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BarberoRepositoryAdapterMySql implements BarberoPersistencePort {

    private final Connection connection;
    private final BarberoRowMapper rowMapper;

    public BarberoRepositoryAdapterMySql(Connection connection, BarberoRowMapper rowMapper) {
        this.connection = connection; this.rowMapper = rowMapper;
    }

    @Override
    public Barbero guardarBarbero(Barbero b) {
        String sql = "INSERT INTO barbero (nombre, apellido, email, password, estado, especialidad, saldo) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, b);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) b.setId(keys.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar barbero: " + e.getMessage(), e);
        }
        return b;
    }

    @Override
    public Optional<Barbero> buscarBarberoPorId(int id) {
        String sql = "SELECT * FROM barbero WHERE id_barbero = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(rowMapper.mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar barbero: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Barbero> listarBarberos() {
        List<Barbero> lista = new ArrayList<>();
        String sql = "SELECT * FROM barbero";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(rowMapper.mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar barberos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Barbero actualizarBarbero(Barbero b) {
        String sql = "UPDATE barbero SET nombre=?,apellido=?,email=?,password=?,estado=?,especialidad=?,saldo=? WHERE id_barbero=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            setParams(ps, b); ps.setInt(8, b.getId()); ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar barbero: " + e.getMessage(), e);
        }
        return b;
    }

    @Override
    public void eliminarBarbero(int id) {
        String sql = "DELETE FROM barbero WHERE id_barbero = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar barbero: " + e.getMessage(), e);
        }
    }

    private void setParams(PreparedStatement ps, Barbero b) throws SQLException {
        ps.setString(1, b.getNombre()); ps.setString(2, b.getApellido());
        ps.setString(3, b.getEmail()); ps.setString(4, b.getPassword());
        ps.setBoolean(5, b.isEstado()); ps.setString(6, b.getEspecialidad());
        ps.setDouble(7, b.getSaldo());
    }
}

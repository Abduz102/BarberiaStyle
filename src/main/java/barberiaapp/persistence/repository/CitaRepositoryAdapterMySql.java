package barberiaapp.persistence.repository;

import barberiaapp.domain.Cita;
import barberiaapp.persistence.mapper.CitaRowMapper;
import barberiaapp.services.outputport.CitaPersistencePort;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CitaRepositoryAdapterMySql implements CitaPersistencePort {

    private final Connection connection;
    private final CitaRowMapper rowMapper;

    private static final String SELECT_JOIN = """
        SELECT c.id_cita, c.fecha_hora, c.tipo_servicio, c.monto_pagado,
               c.metodo_pago, c.estado,
               cl.id_cliente, cl.nombre AS cliente_nombre, cl.apellido AS cliente_apellido,
               b.id_barbero, b.nombre AS barbero_nombre, b.especialidad AS barbero_especialidad
        FROM cita c
        JOIN cliente cl ON c.id_cliente = cl.id_cliente
        JOIN barbero b  ON c.id_barbero = b.id_barbero
        """;

    public CitaRepositoryAdapterMySql(Connection connection, CitaRowMapper rowMapper) {
        this.connection = connection; this.rowMapper = rowMapper;
    }

    @Override
    public Cita guardarCita(Cita cita) {
        String sql = "INSERT INTO cita (fecha_hora, id_cliente, id_barbero, tipo_servicio, monto_pagado, metodo_pago, estado) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, cita);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) cita.setIdCita(keys.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar cita: " + e.getMessage(), e);
        }
        return cita;
    }

    @Override
    public Optional<Cita> buscarCitaPorId(int id) {
        String sql = SELECT_JOIN + " WHERE c.id_cita = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(rowMapper.mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cita: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Cita> listarCitas() {
        return ejecutarQuery(SELECT_JOIN, -1);
    }

    @Override
    public List<Cita> listarCitasPorBarbero(int idBarbero) {
        return ejecutarQuery(SELECT_JOIN + " WHERE b.id_barbero = ?", idBarbero);
    }

    @Override
    public List<Cita> listarCitasPorCliente(int idCliente) {
        return ejecutarQuery(SELECT_JOIN + " WHERE cl.id_cliente = ?", idCliente);
    }

    @Override
    public Cita actualizarCita(Cita cita) {
        String sql = "UPDATE cita SET estado=? WHERE id_cita=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cita.getEstado()); ps.setInt(2, cita.getIdCita());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cita: " + e.getMessage(), e);
        }
        return cita;
    }

    @Override
    public void eliminarCita(int id) {
        String sql = "DELETE FROM cita WHERE id_cita = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cita: " + e.getMessage(), e);
        }
    }

    private List<Cita> ejecutarQuery(String sql, int filtroId) {
        List<Cita> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (filtroId >= 0) ps.setInt(1, filtroId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rowMapper.mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar citas: " + e.getMessage(), e);
        }
        return lista;
    }

    private void setParams(PreparedStatement ps, Cita c) throws SQLException {
        ps.setTimestamp(1, Timestamp.valueOf(c.getFechaHora()));
        ps.setInt(2, c.getCliente().getId());
        ps.setInt(3, c.getBarbero().getId());
        ps.setString(4, c.getTipoServicio());
        ps.setDouble(5, c.getMontoPagado());
        ps.setString(6, c.getMetodoPago());
        ps.setString(7, c.getEstado());
    }
}

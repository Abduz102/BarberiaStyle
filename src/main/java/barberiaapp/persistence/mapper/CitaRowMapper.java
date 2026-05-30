package barberiaapp.persistence.mapper;

import barberiaapp.domain.Barbero;
import barberiaapp.domain.Cita;
import barberiaapp.domain.Cliente;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CitaRowMapper implements RowMapper<Cita> {
    @Override
    public Cita mapRow(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setIdCita(rs.getInt("id_cita"));
        cita.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());

        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id_cliente"));
        cliente.setNombre(rs.getString("cliente_nombre"));
        cliente.setApellido(rs.getString("cliente_apellido"));
        cita.setCliente(cliente);

        Barbero barbero = new Barbero();
        barbero.setId(rs.getInt("id_barbero"));
        barbero.setNombre(rs.getString("barbero_nombre"));
        barbero.setEspecialidad(rs.getString("barbero_especialidad"));
        cita.setBarbero(barbero);

        cita.setTipoServicio(rs.getString("tipo_servicio"));
        cita.setMontoPagado(rs.getDouble("monto_pagado"));
        cita.setMetodoPago(rs.getString("metodo_pago"));
        cita.setEstado(rs.getString("estado"));
        return cita;
    }
}

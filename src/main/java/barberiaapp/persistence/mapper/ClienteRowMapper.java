package barberiaapp.persistence.mapper;

import barberiaapp.domain.Cliente;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteRowMapper implements RowMapper<Cliente> {
    @Override
    public Cliente mapRow(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id_cliente"));
        c.setNombre(rs.getString("nombre"));
        c.setApellido(rs.getString("apellido"));
        c.setEmail(rs.getString("email"));
        c.setPassword(rs.getString("password"));
        c.setEstado(rs.getBoolean("estado"));
        c.setSaldo(rs.getDouble("saldo"));
        c.setPuntos(rs.getDouble("puntos"));
        c.setTipoCliente(rs.getString("tipo_cliente"));
        c.setEsRecurrente(rs.getBoolean("es_recurrente"));
        return c;
    }
}

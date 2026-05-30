package barberiaapp.persistence.mapper;

import barberiaapp.domain.Barbero;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BarberoRowMapper implements RowMapper<Barbero> {
    @Override
    public Barbero mapRow(ResultSet rs) throws SQLException {
        Barbero b = new Barbero();
        b.setId(rs.getInt("id_barbero"));
        b.setNombre(rs.getString("nombre"));
        b.setApellido(rs.getString("apellido"));
        b.setEmail(rs.getString("email"));
        b.setPassword(rs.getString("password"));
        b.setEstado(rs.getBoolean("estado"));
        b.setEspecialidad(rs.getString("especialidad"));
        b.setSaldo(rs.getDouble("saldo"));
        return b;
    }
}

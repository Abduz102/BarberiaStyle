package barberiaapp.persistence.mapper;

import barberiaapp.domain.Administrador;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministradorRowMapper implements RowMapper<Administrador> {
    @Override
    public Administrador mapRow(ResultSet rs) throws SQLException {
        Administrador a = new Administrador();
        a.setId(rs.getInt("id_admin"));
        a.setNombre(rs.getString("nombre"));
        a.setApellido(rs.getString("apellido"));
        a.setEmail(rs.getString("email"));
        a.setPassword(rs.getString("password"));
        a.setEstado(rs.getBoolean("estado"));
        a.setSaldo(rs.getDouble("saldo"));
        a.setRol(rs.getString("rol"));
        return a;
    }
}

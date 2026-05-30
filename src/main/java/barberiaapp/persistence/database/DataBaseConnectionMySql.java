package barberiaapp.persistence.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnectionMySql {

    private static DataBaseConnectionMySql instance;
    private final Connection connection;

    private static final String URL      = "jdbc:mysql://localhost:3306/barberia_style";
    private static final String USERNAME = "barberia_user";
    private static final String PASSWORD = "barberia";

    private DataBaseConnectionMySql() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("  Conectado a barberia_style (MySQL)");
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    public static synchronized DataBaseConnectionMySql getInstance() {
        if (instance == null) instance = new DataBaseConnectionMySql();
        return instance;
    }

    public Connection getConnection() { return connection; }
}

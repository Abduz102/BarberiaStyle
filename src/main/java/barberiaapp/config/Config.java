package barberiaapp.config;

import barberiaapp.persistence.database.DataBaseConnectionMySql;
import barberiaapp.persistence.mapper.*;
import barberiaapp.persistence.repository.*;
import barberiaapp.services.*;
import barberiaapp.services.outputport.*;
import barberiaapp.userinterface.MenuApp;
import barberiaapp.view.*;

import java.sql.Connection;

public class Config {

    public static MenuApp createMenuApp() {

        Connection connection = DataBaseConnectionMySql.getInstance().getConnection();

        // Mappers
        ClienteRowMapper       clienteMapper       = new ClienteRowMapper();
        BarberoRowMapper       barberoMapper        = new BarberoRowMapper();
        CitaRowMapper          citaMapper           = new CitaRowMapper();

        // Repositories
        ClientePersistencePort  clienteRepo  = new ClienteRepositoryAdapterMySql(connection, clienteMapper);
        BarberoPersistencePort  barberoRepo  = new BarberoRepositoryAdapterMySql(connection, barberoMapper);
        CitaPersistencePort     citaRepo     = new CitaRepositoryAdapterMySql(connection, citaMapper);

        // Services
        ClienteServiceImpl clienteService = new ClienteServiceImpl(clienteRepo);
        BarberoServiceImpl barberoService = new BarberoServiceImpl(barberoRepo);
        CitaServiceImpl    citaService    = new CitaServiceImpl(citaRepo, clienteRepo, barberoRepo);
        AdminServiceImpl   adminService   = new AdminServiceImpl(clienteRepo, barberoRepo, 0.0);

        // Views
        ClienteView clienteView = new ClienteView(clienteService);
        BarberoView barberoView = new BarberoView(barberoService);
        CitaView    citaView    = new CitaView(citaService);
        AdminView   adminView   = new AdminView(adminService);

        return new MenuApp(clienteView, barberoView, citaView, adminView);
    }
}

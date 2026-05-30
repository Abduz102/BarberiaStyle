package barberiaapp.services.outputport;

import barberiaapp.domain.Administrador;
import java.util.List;
import java.util.Optional;

public interface AdministradorPersistencePort {
    Administrador guardarAdministrador(Administrador admin);
    Optional<Administrador> buscarAdminPorId(int id);
    List<Administrador> listarAdmins();
    Administrador actualizarAdministrador(Administrador admin);
}

package barberiaapp.services.outputport;

import barberiaapp.domain.Cita;
import java.util.List;
import java.util.Optional;

public interface CitaPersistencePort {
    Cita guardarCita(Cita cita);
    Optional<Cita> buscarCitaPorId(int id);
    List<Cita> listarCitas();
    List<Cita> listarCitasPorBarbero(int idBarbero);
    List<Cita> listarCitasPorCliente(int idCliente);
    Cita actualizarCita(Cita cita);
    void eliminarCita(int id);
}

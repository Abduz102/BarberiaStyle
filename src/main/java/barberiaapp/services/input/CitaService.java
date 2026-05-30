package barberiaapp.services.input;

import barberiaapp.domain.Cita;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CitaService {
    Cita agendarCita(LocalDateTime fechaHora, int idCliente, int idBarbero,
                     String tipoServicio, String metodoPago);
    Optional<Cita> buscarCitaPorId(int id);
    List<Cita> listarCitas();
    List<Cita> listarCitasPorBarbero(int idBarbero);
    List<Cita> listarCitasPorCliente(int idCliente);
    Cita cambiarEstadoCita(int idCita, String nuevoEstado);
    void cancelarCita(int idCita);
}

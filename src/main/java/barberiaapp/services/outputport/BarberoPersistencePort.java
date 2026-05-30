package barberiaapp.services.outputport;

import barberiaapp.domain.Barbero;
import java.util.List;
import java.util.Optional;

public interface BarberoPersistencePort {
    Barbero guardarBarbero(Barbero barbero);
    Optional<Barbero> buscarBarberoPorId(int id);
    List<Barbero> listarBarberos();
    Barbero actualizarBarbero(Barbero barbero);
    void eliminarBarbero(int id);
}

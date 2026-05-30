package barberiaapp.services.input;

import barberiaapp.domain.Barbero;
import java.util.List;
import java.util.Optional;

public interface BarberoService {
    Barbero crearBarbero(String nombre, String apellido, String email,
                         String password, boolean estado, String especialidad);
    Optional<Barbero> buscarBarberoPorId(int id);
    List<Barbero> listarBarberos();
    Barbero actualizarBarbero(int id, String nombre, String apellido, String email,
                               String password, boolean estado, String especialidad);
    void eliminarBarbero(int id);
}

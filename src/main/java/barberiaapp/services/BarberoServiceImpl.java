package barberiaapp.services;

import barberiaapp.domain.Barbero;
import barberiaapp.services.input.BarberoService;
import barberiaapp.services.outputport.BarberoPersistencePort;

import java.util.List;
import java.util.Optional;

public class BarberoServiceImpl implements BarberoService {

    private final BarberoPersistencePort repo;

    public BarberoServiceImpl(BarberoPersistencePort repo) { this.repo = repo; }

    @Override
    public Barbero crearBarbero(String nombre, String apellido, String email,
                                String password, boolean estado, String especialidad) {
        Barbero b = new Barbero(0, nombre, apellido, email, password, estado, especialidad, 0.0);
        return repo.guardarBarbero(b);
    }

    @Override
    public Optional<Barbero> buscarBarberoPorId(int id) { return repo.buscarBarberoPorId(id); }

    @Override
    public List<Barbero> listarBarberos() { return repo.listarBarberos(); }

    @Override
    public Barbero actualizarBarbero(int id, String nombre, String apellido, String email,
                                     String password, boolean estado, String especialidad) {
        Barbero b = repo.buscarBarberoPorId(id).orElseThrow(() -> new RuntimeException("Barbero no encontrado: " + id));
        b.setNombre(nombre); b.setApellido(apellido); b.setEmail(email);
        if (password != null && !password.isEmpty()) b.setPassword(password);
        b.setEstado(estado); b.setEspecialidad(especialidad);
        return repo.actualizarBarbero(b);
    }

    @Override
    public void eliminarBarbero(int id) { repo.eliminarBarbero(id); }
}

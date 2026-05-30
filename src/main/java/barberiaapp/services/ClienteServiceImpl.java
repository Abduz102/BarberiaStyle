package barberiaapp.services;

import barberiaapp.domain.Cliente;
import barberiaapp.services.input.ClienteService;
import barberiaapp.services.outputport.ClientePersistencePort;

import java.util.List;
import java.util.Optional;

public class ClienteServiceImpl implements ClienteService {

    private final ClientePersistencePort repo;

    public ClienteServiceImpl(ClientePersistencePort repo) { this.repo = repo; }

    @Override
    public Cliente crearCliente(String nombre, String apellido, String email, String password,
                                boolean estado, double saldo, double puntos, String tipoCliente) {
        Cliente c = new Cliente(0, nombre, apellido, email, password, estado, saldo, puntos, tipoCliente, false);
        return repo.guardarCliente(c);
    }

    @Override
    public Optional<Cliente> buscarClientePorId(int id) { return repo.buscarClientePorId(id); }

    @Override
    public List<Cliente> listarClientes() { return repo.listarClientes(); }

    @Override
    public Cliente actualizarCliente(int id, String nombre, String apellido, String email,
                                     String password, boolean estado, double saldo,
                                     double puntos, String tipoCliente) {
        Cliente c = repo.buscarClientePorId(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
        c.setNombre(nombre); c.setApellido(apellido); c.setEmail(email);
        if (password != null && !password.isEmpty()) c.setPassword(password);
        c.setEstado(estado); c.setSaldo(saldo); c.setPuntos(puntos); c.setTipoCliente(tipoCliente);
        return repo.actualizarCliente(c);
    }

    @Override
    public void eliminarCliente(int id) { repo.eliminarCliente(id); }

    @Override
    public boolean recargarSaldo(int idCliente, double monto) {
        if (monto <= 0) return false;
        Cliente c = repo.buscarClientePorId(idCliente).orElseThrow();
        c.setSaldo(c.getSaldo() + monto);
        repo.actualizarCliente(c);
        return true;
    }

    @Override
    public boolean retirarSaldo(int idCliente, double monto) {
        if (monto <= 0) return false;
        Cliente c = repo.buscarClientePorId(idCliente).orElseThrow();
        if (c.getSaldo() < monto) return false;
        c.setSaldo(c.getSaldo() - monto);
        repo.actualizarCliente(c);
        return true;
    }

    @Override
    public boolean transferirPuntos(int idOrigen, int idDestino, double puntos) {
        if (puntos <= 0) return false;
        Cliente origen = repo.buscarClientePorId(idOrigen).orElseThrow();
        Cliente destino = repo.buscarClientePorId(idDestino).orElseThrow();
        if (origen.getPuntos() < puntos) return false;
        origen.setPuntos(origen.getPuntos() - puntos);
        destino.setPuntos(destino.getPuntos() + puntos);
        repo.actualizarCliente(origen);
        repo.actualizarCliente(destino);
        return true;
    }
}

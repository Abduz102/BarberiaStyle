package barberiaapp.services.outputport;

import barberiaapp.domain.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClientePersistencePort {
    Cliente guardarCliente(Cliente cliente);
    Optional<Cliente> buscarClientePorId(int id);
    List<Cliente> listarClientes();
    Cliente actualizarCliente(Cliente cliente);
    void eliminarCliente(int id);
}

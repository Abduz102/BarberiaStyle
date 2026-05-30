package barberiaapp.services.input;

import barberiaapp.domain.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    Cliente crearCliente(String nombre, String apellido, String email, String password,
                         boolean estado, double saldo, double puntos, String tipoCliente);
    Optional<Cliente> buscarClientePorId(int id);
    List<Cliente> listarClientes();
    Cliente actualizarCliente(int id, String nombre, String apellido, String email, String password,
                               boolean estado, double saldo, double puntos, String tipoCliente);
    void eliminarCliente(int id);
    boolean recargarSaldo(int idCliente, double monto);
    boolean retirarSaldo(int idCliente, double monto);
    boolean transferirPuntos(int idOrigen, int idDestino, double puntos);
}

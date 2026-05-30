package barberiaapp.services.input;

import barberiaapp.domain.Cliente;
import barberiaapp.domain.Barbero;
import java.util.List;

public interface AdminService {
    List<Cliente> listarTodosLosClientes();
    List<Barbero> listarTodosLosBarberos();
    void eliminarCliente(int id);
    void eliminarBarbero(int id);
    boolean pagarNominaBarbero(int idBarbero, double monto);
    double obtenerSaldoAdmin();
}

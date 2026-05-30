package barberiaapp.services;

import barberiaapp.domain.Barbero;
import barberiaapp.domain.Cliente;
import barberiaapp.services.input.AdminService;
import barberiaapp.services.outputport.BarberoPersistencePort;
import barberiaapp.services.outputport.ClientePersistencePort;

import java.util.List;

public class AdminServiceImpl implements AdminService {

    private final ClientePersistencePort clienteRepo;
    private final BarberoPersistencePort barberoRepo;
    private double saldoAdmin;

    public AdminServiceImpl(ClientePersistencePort clienteRepo,
                            BarberoPersistencePort barberoRepo,
                            double saldoAdmin) {
        this.clienteRepo = clienteRepo;
        this.barberoRepo = barberoRepo;
        this.saldoAdmin = saldoAdmin;
    }

    @Override
    public List<Cliente> listarTodosLosClientes() { return clienteRepo.listarClientes(); }

    @Override
    public List<Barbero> listarTodosLosBarberos() { return barberoRepo.listarBarberos(); }

    @Override
    public void eliminarCliente(int id) { clienteRepo.eliminarCliente(id); }

    @Override
    public void eliminarBarbero(int id) { barberoRepo.eliminarBarbero(id); }

    @Override
    public boolean pagarNominaBarbero(int idBarbero, double monto) {
        if (monto <= 0 || saldoAdmin < monto) return false;
        Barbero b = barberoRepo.buscarBarberoPorId(idBarbero)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado."));
        b.setSaldo(b.getSaldo() + monto);
        saldoAdmin -= monto;
        barberoRepo.actualizarBarbero(b);
        return true;
    }

    @Override
    public double obtenerSaldoAdmin() { return saldoAdmin; }

    public void recibirPago(double monto) { this.saldoAdmin += monto; }
}

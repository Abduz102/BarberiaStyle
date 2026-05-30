package barberiaapp.services;

import barberiaapp.domain.*;
import barberiaapp.domain.enums.EstadoCitaEnum;
import barberiaapp.domain.enums.TipoServicioEnum;
import barberiaapp.services.input.CitaService;
import barberiaapp.services.outputport.BarberoPersistencePort;
import barberiaapp.services.outputport.CitaPersistencePort;
import barberiaapp.services.outputport.ClientePersistencePort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CitaServiceImpl implements CitaService {

    private final CitaPersistencePort citaRepo;
    private final ClientePersistencePort clienteRepo;
    private final BarberoPersistencePort barberoRepo;

    public CitaServiceImpl(CitaPersistencePort citaRepo,
                           ClientePersistencePort clienteRepo,
                           BarberoPersistencePort barberoRepo) {
        this.citaRepo = citaRepo;
        this.clienteRepo = clienteRepo;
        this.barberoRepo = barberoRepo;
    }

    @Override
    public Cita agendarCita(LocalDateTime fechaHora, int idCliente, int idBarbero,
                            String tipoServicio, String metodoPago) {
        Cliente cliente = clienteRepo.buscarClientePorId(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + idCliente));
        Barbero barbero = barberoRepo.buscarBarberoPorId(idBarbero)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado: " + idBarbero));

        double monto = calcularMonto(tipoServicio);

        if (metodoPago.equalsIgnoreCase("Saldo Billetera")) {
            if (cliente.getSaldo() < monto) throw new RuntimeException("Saldo insuficiente.");
            cliente.setSaldo(cliente.getSaldo() - monto);
            clienteRepo.actualizarCliente(cliente);
        } else if (metodoPago.equalsIgnoreCase("Barber Points")) {
            if (cliente.getPuntos() < monto) throw new RuntimeException("Puntos insuficientes.");
            cliente.setPuntos(cliente.getPuntos() - monto);
            clienteRepo.actualizarCliente(cliente);
        }

        Cita cita = new Cita(fechaHora, cliente, barbero, tipoServicio, monto,
                metodoPago, EstadoCitaEnum.PENDIENTE.getDescripcion());
        return citaRepo.guardarCita(cita);
    }

    @Override
    public Optional<Cita> buscarCitaPorId(int id) { return citaRepo.buscarCitaPorId(id); }

    @Override
    public List<Cita> listarCitas() { return citaRepo.listarCitas(); }

    @Override
    public List<Cita> listarCitasPorBarbero(int idBarbero) { return citaRepo.listarCitasPorBarbero(idBarbero); }

    @Override
    public List<Cita> listarCitasPorCliente(int idCliente) { return citaRepo.listarCitasPorCliente(idCliente); }

    @Override
    public Cita cambiarEstadoCita(int idCita, String nuevoEstado) {
        Cita cita = citaRepo.buscarCitaPorId(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + idCita));
        cita.setEstado(nuevoEstado);
        return citaRepo.actualizarCita(cita);
    }

    @Override
    public void cancelarCita(int idCita) { citaRepo.eliminarCita(idCita); }

    private double calcularMonto(String tipoServicio) {
        for (TipoServicioEnum t : TipoServicioEnum.values()) {
            if (t.getDescripcion().equalsIgnoreCase(tipoServicio)) return t.getPrecio();
        }
        return 30000;
    }
}

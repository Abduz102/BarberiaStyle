package barberiaapp.view;

import barberiaapp.domain.Cita;
import barberiaapp.domain.enums.EstadoCitaEnum;
import barberiaapp.domain.enums.MetodoPagoEnum;
import barberiaapp.domain.enums.TipoServicioEnum;
import barberiaapp.services.input.CitaService;
import barberiaapp.utils.FormValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CitaView {

    private final CitaService citaService;

    public CitaView(CitaService citaService) { this.citaService = citaService; }

    public void agendarCita() {
        System.out.println("\n--- Agendar Nueva Cita ---");
        int idCliente = FormValidator.validarEntero("ID del cliente");
        int idBarbero = FormValidator.validarEntero("ID del barbero");

        LocalDateTime fechaHora = leerFechaHora();
        String tipoServicio = seleccionarTipoServicio();
        String metodoPago   = seleccionarMetodoPago();

        try {
            Cita cita = citaService.agendarCita(fechaHora, idCliente, idBarbero, tipoServicio, metodoPago);
            System.out.println("  Cita agendada exitosamente: " + cita);
        } catch (RuntimeException e) {
            System.out.println("  Error al agendar cita: " + e.getMessage());
        }
    }

    public void listarCitas() {
        List<Cita> lista = citaService.listarCitas();
        if (lista.isEmpty()) { System.out.println("  No hay citas registradas."); return; }
        System.out.println("\n--- Todas las Citas ---");
        lista.forEach(c -> System.out.println("  " + c));
    }

    public void listarCitasPorBarbero() {
        int idBarbero = FormValidator.validarEntero("ID del barbero");
        List<Cita> lista = citaService.listarCitasPorBarbero(idBarbero);
        if (lista.isEmpty()) { System.out.println("  Sin citas para este barbero."); return; }
        lista.forEach(c -> System.out.println("  " + c));
    }

    public void listarCitasPorCliente() {
        int idCliente = FormValidator.validarEntero("ID del cliente");
        List<Cita> lista = citaService.listarCitasPorCliente(idCliente);
        if (lista.isEmpty()) { System.out.println("  Sin citas para este cliente."); return; }
        lista.forEach(c -> System.out.println("  " + c));
    }

    public void cambiarEstadoCita() {
        int idCita = FormValidator.validarEntero("ID de la cita");
        System.out.println("  1=Aceptada  2=Rechazada  3=Cancelada");
        int op = FormValidator.validarEntero("Nuevo estado");
        String estado = switch (op) {
            case 1 -> EstadoCitaEnum.ACEPTADA.getDescripcion();
            case 2 -> EstadoCitaEnum.RECHAZADA.getDescripcion();
            case 3 -> EstadoCitaEnum.CANCELADA.getDescripcion();
            default -> EstadoCitaEnum.PENDIENTE.getDescripcion();
        };
        Cita c = citaService.cambiarEstadoCita(idCita, estado);
        System.out.println("  Estado actualizado: " + c);
    }

    public void buscarCitaPorId() {
        int id = FormValidator.validarEntero("ID de la cita");
        citaService.buscarCitaPorId(id)
                .ifPresentOrElse(
                        c -> System.out.println("  " + c),
                        () -> System.out.println("  Cita no encontrada.")
                );
    }

    private LocalDateTime leerFechaHora() {
        while (true) {
            try {
                String fecha = FormValidator.validarTexto("Fecha (YYYY-MM-DD)");
                String hora  = FormValidator.validarTexto("Hora (HH:MM)");
                return LocalDateTime.of(LocalDate.parse(fecha), LocalTime.parse(hora));
            } catch (DateTimeParseException e) {
                System.out.println("  [!] Formato invalido. Usa YYYY-MM-DD y HH:MM.");
            }
        }
    }

    private String seleccionarTipoServicio() {
        System.out.println("  1=" + TipoServicioEnum.CORTE_ESTANDAR.getDescripcion() + " ($30.000)");
        System.out.println("  2=" + TipoServicioEnum.PLAN_AHORRO.getDescripcion() + " ($25.000)");
        System.out.println("  3=" + TipoServicioEnum.MEMBRESIA_VIP.getDescripcion() + " ($120.000)");
        int op = FormValidator.validarEntero("Selecciona el servicio");
        return switch (op) {
            case 2 -> TipoServicioEnum.PLAN_AHORRO.getDescripcion();
            case 3 -> TipoServicioEnum.MEMBRESIA_VIP.getDescripcion();
            default -> TipoServicioEnum.CORTE_ESTANDAR.getDescripcion();
        };
    }

    private String seleccionarMetodoPago() {
        System.out.println("  1=" + MetodoPagoEnum.SALDO.getDescripcion());
        System.out.println("  2=" + MetodoPagoEnum.PUNTOS.getDescripcion());
        System.out.println("  3=" + MetodoPagoEnum.EFECTIVO.getDescripcion());
        int op = FormValidator.validarEntero("Metodo de pago");
        return switch (op) {
            case 2 -> MetodoPagoEnum.PUNTOS.getDescripcion();
            case 3 -> MetodoPagoEnum.EFECTIVO.getDescripcion();
            default -> MetodoPagoEnum.SALDO.getDescripcion();
        };
    }
}

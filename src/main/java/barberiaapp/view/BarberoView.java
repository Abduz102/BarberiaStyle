package barberiaapp.view;

import barberiaapp.domain.Barbero;
import barberiaapp.services.input.BarberoService;
import barberiaapp.utils.FormValidator;

import java.util.List;

public class BarberoView {

    private final BarberoService barberoService;

    public BarberoView(BarberoService barberoService) { this.barberoService = barberoService; }

    public void crearBarbero() {
        System.out.println("\n--- Registrar Nuevo Barbero ---");
        String nombre       = FormValidator.validarTexto("Nombre");
        String apellido     = FormValidator.validarTexto("Apellido");
        String email        = FormValidator.validarTexto("Email");
        String password     = FormValidator.validarTexto("Password");
        boolean estado      = FormValidator.validarBooleano("Estado activo");
        String especialidad = FormValidator.validarTexto("Especialidad (ej: Degradados VIP)");
        Barbero b = barberoService.crearBarbero(nombre, apellido, email, password, estado, especialidad);
        System.out.println("  Barbero registrado: " + b);
    }

    public void listarBarberos() {
        List<Barbero> lista = barberoService.listarBarberos();
        if (lista.isEmpty()) { System.out.println("  No hay barberos registrados."); return; }
        System.out.println("\n--- Lista de Barberos ---");
        lista.forEach(b -> System.out.println("  " + b));
    }

    public void buscarBarberoPorId() {
        int id = FormValidator.validarEntero("ID del barbero a buscar");
        barberoService.buscarBarberoPorId(id)
                .ifPresentOrElse(
                        b -> System.out.println("  Encontrado: " + b),
                        () -> System.out.println("  Barbero no encontrado.")
                );
    }

    public void actualizarBarbero() {
        int id = FormValidator.validarEntero("ID del barbero a actualizar");
        barberoService.buscarBarberoPorId(id).orElseThrow();
        String nombre       = FormValidator.validarTexto("Nuevo nombre");
        String apellido     = FormValidator.validarTexto("Nuevo apellido");
        String email        = FormValidator.validarTexto("Nuevo email");
        String password     = FormValidator.validarTexto("Nueva password");
        boolean estado      = FormValidator.validarBooleano("Estado activo");
        String especialidad = FormValidator.validarTexto("Nueva especialidad");
        Barbero b = barberoService.actualizarBarbero(id, nombre, apellido, email, password, estado, especialidad);
        System.out.println("  Actualizado: " + b);
    }

    public void eliminarBarbero() {
        int id = FormValidator.validarEntero("ID del barbero a eliminar");
        barberoService.eliminarBarbero(id);
        System.out.println("  Barbero eliminado.");
    }
}

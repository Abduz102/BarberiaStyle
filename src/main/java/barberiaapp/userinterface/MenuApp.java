package barberiaapp.userinterface;

import barberiaapp.utils.FormValidator;
import barberiaapp.view.*;

public class MenuApp {

    private final ClienteView clienteView;
    private final BarberoView barberoView;
    private final CitaView    citaView;
    private final AdminView   adminView;

    public MenuApp(ClienteView clienteView, BarberoView barberoView,
                   CitaView citaView, AdminView adminView) {
        this.clienteView = clienteView;
        this.barberoView = barberoView;
        this.citaView    = citaView;
        this.adminView   = adminView;
    }

    public void mostrarMenuPrincipal() {
        System.out.println("\n==========================================");
        System.out.println("   BARBERIA STYLE — Sistema de Gestion   ");
        System.out.println("==========================================");

        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\nMENU PRINCIPAL");
            System.out.println("  1. Gestion de Clientes");
            System.out.println("  2. Gestion de Barberos");
            System.out.println("  3. Gestion de Citas");
            System.out.println("  4. Panel Administrador");
            System.out.println("  0. Salir");

            int op = FormValidator.validarEntero("Selecciona una opcion");
            switch (op) {
                case 1 -> menuClientes();
                case 2 -> menuBarberos();
                case 3 -> menuCitas();
                case 4 -> menuAdmin();
                case 0 -> { corriendo = false; System.out.println("  Hasta luego!"); }
                default -> System.out.println("  [!] Opcion invalida.");
            }
        }
    }

    private void menuClientes() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n--- CLIENTES ---");
            System.out.println("  1. Registrar cliente");
            System.out.println("  2. Buscar por ID");
            System.out.println("  3. Listar todos");
            System.out.println("  4. Actualizar");
            System.out.println("  5. Eliminar");
            System.out.println("  6. Recargar saldo");
            System.out.println("  7. Retirar saldo");
            System.out.println("  8. Transferir Barber Points");
            System.out.println("  0. Volver");

            int op = FormValidator.validarEntero("Opcion");
            switch (op) {
                case 1 -> clienteView.crearCliente();
                case 2 -> clienteView.buscarClientePorId();
                case 3 -> clienteView.listarClientes();
                case 4 -> clienteView.actualizarCliente();
                case 5 -> clienteView.eliminarCliente();
                case 6 -> clienteView.recargarSaldo();
                case 7 -> clienteView.retirarSaldo();
                case 8 -> clienteView.transferirPuntos();
                case 0 -> corriendo = false;
                default -> System.out.println("  [!] Opcion invalida.");
            }
        }
    }

    private void menuBarberos() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n--- BARBEROS ---");
            System.out.println("  1. Registrar barbero");
            System.out.println("  2. Buscar por ID");
            System.out.println("  3. Listar todos");
            System.out.println("  4. Actualizar");
            System.out.println("  5. Eliminar");
            System.out.println("  0. Volver");

            int op = FormValidator.validarEntero("Opcion");
            switch (op) {
                case 1 -> barberoView.crearBarbero();
                case 2 -> barberoView.buscarBarberoPorId();
                case 3 -> barberoView.listarBarberos();
                case 4 -> barberoView.actualizarBarbero();
                case 5 -> barberoView.eliminarBarbero();
                case 0 -> corriendo = false;
                default -> System.out.println("  [!] Opcion invalida.");
            }
        }
    }

    private void menuCitas() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n--- CITAS ---");
            System.out.println("  1. Agendar cita");
            System.out.println("  2. Buscar por ID");
            System.out.println("  3. Listar todas");
            System.out.println("  4. Ver citas de un barbero");
            System.out.println("  5. Ver citas de un cliente");
            System.out.println("  6. Cambiar estado (Aceptar / Rechazar)");
            System.out.println("  0. Volver");

            int op = FormValidator.validarEntero("Opcion");
            switch (op) {
                case 1 -> citaView.agendarCita();
                case 2 -> citaView.buscarCitaPorId();
                case 3 -> citaView.listarCitas();
                case 4 -> citaView.listarCitasPorBarbero();
                case 5 -> citaView.listarCitasPorCliente();
                case 6 -> citaView.cambiarEstadoCita();
                case 0 -> corriendo = false;
                default -> System.out.println("  [!] Opcion invalida.");
            }
        }
    }

    private void menuAdmin() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n--- PANEL ADMIN ---");
            System.out.println("  1. Listar todos los clientes");
            System.out.println("  2. Listar todos los barberos");
            System.out.println("  3. Eliminar cliente");
            System.out.println("  4. Eliminar barbero");
            System.out.println("  5. Pagar nomina a barbero");
            System.out.println("  6. Ver saldo cuenta corriente");
            System.out.println("  0. Volver");

            int op = FormValidator.validarEntero("Opcion");
            switch (op) {
                case 1 -> adminView.listarClientes();
                case 2 -> adminView.listarBarberos();
                case 3 -> adminView.eliminarCliente();
                case 4 -> adminView.eliminarBarbero();
                case 5 -> adminView.pagarNomina();
                case 6 -> adminView.mostrarSaldo();
                case 0 -> corriendo = false;
                default -> System.out.println("  [!] Opcion invalida.");
            }
        }
    }
}

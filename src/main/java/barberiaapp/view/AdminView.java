package barberiaapp.view;

import barberiaapp.services.AdminServiceImpl;
import barberiaapp.utils.FormValidator;

public class AdminView {

    private final AdminServiceImpl adminService;

    public AdminView(AdminServiceImpl adminService) { this.adminService = adminService; }

    public void listarClientes() {
        System.out.println("\n--- Todos los Clientes ---");
        adminService.listarTodosLosClientes().forEach(c -> System.out.println("  " + c));
    }

    public void listarBarberos() {
        System.out.println("\n--- Todos los Barberos ---");
        adminService.listarTodosLosBarberos().forEach(b -> System.out.println("  " + b));
    }

    public void eliminarCliente() {
        int id = FormValidator.validarEntero("ID del cliente a eliminar");
        adminService.eliminarCliente(id);
        System.out.println("  Cliente eliminado.");
    }

    public void eliminarBarbero() {
        int id = FormValidator.validarEntero("ID del barbero a eliminar");
        adminService.eliminarBarbero(id);
        System.out.println("  Barbero eliminado.");
    }

    public void pagarNomina() {
        System.out.println("  Saldo admin actual: $" + String.format("%.2f", adminService.obtenerSaldoAdmin()));
        int idBarbero = FormValidator.validarEntero("ID del barbero");
        double monto  = FormValidator.validarDecimal("Monto de la nomina");
        if (adminService.pagarNominaBarbero(idBarbero, monto))
            System.out.println("  Nomina pagada correctamente.");
        else
            System.out.println("  Error: saldo del admin insuficiente o monto invalido.");
    }

    public void mostrarSaldo() {
        System.out.println("  Saldo cuenta corriente admin: $" + String.format("%.2f", adminService.obtenerSaldoAdmin()));
    }
}

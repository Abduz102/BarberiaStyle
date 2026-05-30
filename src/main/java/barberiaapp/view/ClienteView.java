package barberiaapp.view;

import barberiaapp.domain.Cliente;
import barberiaapp.domain.enums.TipoClienteEnum;
import barberiaapp.services.input.ClienteService;
import barberiaapp.utils.FormValidator;

import java.util.List;
import java.util.Optional;

public class ClienteView {

    private final ClienteService clienteService;

    public ClienteView(ClienteService clienteService) { this.clienteService = clienteService; }

    public void crearCliente() {
        System.out.println("\n--- Registrar Nuevo Cliente ---");
        String nombre   = FormValidator.validarTexto("Nombre");
        String apellido = FormValidator.validarTexto("Apellido");
        String email    = FormValidator.validarTexto("Email");
        String password = FormValidator.validarTexto("Password");
        boolean estado  = FormValidator.validarBooleano("Estado (activo)");
        double saldo    = FormValidator.validarDecimal("Saldo inicial");
        String tipo     = seleccionarTipoCliente();
        Cliente c = clienteService.crearCliente(nombre, apellido, email, password, estado, saldo, 0.0, tipo);
        System.out.println("  Cliente registrado: " + c);
    }

    public void buscarClientePorId() {
        int id = FormValidator.validarEntero("ID del cliente a buscar");
        Optional<Cliente> opt = clienteService.buscarClientePorId(id);
        opt.ifPresentOrElse(
                c -> System.out.println("  Encontrado: " + c),
                () -> System.out.println("  Cliente no encontrado.")
        );
    }

    public void listarClientes() {
        List<Cliente> lista = clienteService.listarClientes();
        if (lista.isEmpty()) { System.out.println("  No hay clientes registrados."); return; }
        System.out.println("\n--- Lista de Clientes ---");
        lista.forEach(c -> System.out.println("  " + c));
    }

    public void actualizarCliente() {
        int id = FormValidator.validarEntero("ID del cliente a actualizar");
        clienteService.buscarClientePorId(id).orElseThrow(() -> new RuntimeException("No existe."));
        String nombre   = FormValidator.validarTexto("Nuevo nombre");
        String apellido = FormValidator.validarTexto("Nuevo apellido");
        String email    = FormValidator.validarTexto("Nuevo email");
        System.out.println("  (Deja en blanco para no cambiar la contrasena)");
        String password = FormValidator.validarTexto("Nueva password (o cualquier texto si no cambias)");
        boolean estado  = FormValidator.validarBooleano("Estado activo");
        double saldo    = FormValidator.validarDecimal("Saldo actual");
        double puntos   = FormValidator.validarDecimal("Puntos actuales");
        String tipo     = seleccionarTipoCliente();
        Cliente c = clienteService.actualizarCliente(id, nombre, apellido, email, password, estado, saldo, puntos, tipo);
        System.out.println("  Actualizado: " + c);
    }

    public void eliminarCliente() {
        int id = FormValidator.validarEntero("ID del cliente a eliminar");
        clienteService.eliminarCliente(id);
        System.out.println("  Cliente eliminado correctamente.");
    }

    public void recargarSaldo() {
        int id     = FormValidator.validarEntero("ID del cliente");
        double monto = FormValidator.validarDecimal("Monto a recargar");
        if (clienteService.recargarSaldo(id, monto)) System.out.println("  Saldo recargado correctamente.");
        else System.out.println("  Error: monto invalido.");
    }

    public void retirarSaldo() {
        int id     = FormValidator.validarEntero("ID del cliente");
        double monto = FormValidator.validarDecimal("Monto a retirar");
        if (clienteService.retirarSaldo(id, monto)) System.out.println("  Retiro exitoso.");
        else System.out.println("  Error: saldo insuficiente o monto invalido.");
    }

    public void transferirPuntos() {
        int idOrigen  = FormValidator.validarEntero("ID cliente origen");
        int idDestino = FormValidator.validarEntero("ID cliente destino");
        double puntos = FormValidator.validarDecimal("Puntos a transferir");
        if (clienteService.transferirPuntos(idOrigen, idDestino, puntos))
            System.out.println("  Transferencia de puntos exitosa.");
        else System.out.println("  Error: puntos insuficientes.");
    }

    private String seleccionarTipoCliente() {
        System.out.println("  Tipo: 1=Nuevo  2=Recurrente  3=VIP  4=Moroso");
        int op = FormValidator.validarEntero("Opcion");
        return switch (op) {
            case 2 -> TipoClienteEnum.RECURRENTE.getDescripcion();
            case 3 -> TipoClienteEnum.VIP.getDescripcion();
            case 4 -> TipoClienteEnum.MOROSO.getDescripcion();
            default -> TipoClienteEnum.NUEVO.getDescripcion();
        };
    }
}

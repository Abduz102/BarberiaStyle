package barberiaapp.domain;

public class Administrador extends Persona {
    private double saldo;
    private String rol;

    public Administrador(int id, String nombre, String apellido, String email, String password,
                         boolean estado, double saldo, String rol) {
        super(id, nombre, apellido, email, password, estado);
        this.saldo = saldo; this.rol = rol;
    }
    public Administrador() {}

    public double getSaldo() { return saldo; }
    public void setSaldo(double s) { this.saldo = s; }
    public String getRol() { return rol; }
    public void setRol(String r) { this.rol = r; }

    @Override
    public String toString() {
        return super.toString()
             + " | Rol: " + rol
             + " | Saldo: $" + String.format("%.2f", saldo);
    }
}

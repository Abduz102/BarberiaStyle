package barberiaapp.domain;

public class Barbero extends Persona {
    private String especialidad;
    private double saldo;

    public Barbero(int id, String nombre, String apellido, String email, String password,
                   boolean estado, String especialidad, double saldo) {
        super(id, nombre, apellido, email, password, estado);
        this.especialidad = especialidad; this.saldo = saldo;
    }
    public Barbero() {}

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String e) { this.especialidad = e; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double s) { this.saldo = s; }

    @Override
    public String toString() {
        return super.toString()
             + " | Especialidad: " + especialidad
             + " | Saldo nomina: $" + String.format("%.2f", saldo);
    }
}

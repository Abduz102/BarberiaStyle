package barberiaapp.domain;

public class Cliente extends Persona {
    private double saldo;
    private double puntos;
    private String tipoCliente;
    private boolean esRecurrente;

    public Cliente(int id, String nombre, String apellido, String email, String password,
                   boolean estado, double saldo, double puntos,
                   String tipoCliente, boolean esRecurrente) {
        super(id, nombre, apellido, email, password, estado);
        this.saldo = saldo; this.puntos = puntos;
        this.tipoCliente = tipoCliente; this.esRecurrente = esRecurrente;
    }
    public Cliente() {}

    public double getSaldo() { return saldo; }
    public void setSaldo(double s) { this.saldo = s; }
    public double getPuntos() { return puntos; }
    public void setPuntos(double p) { this.puntos = p; }
    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String t) { this.tipoCliente = t; }
    public boolean isEsRecurrente() { return esRecurrente; }
    public void setEsRecurrente(boolean r) { this.esRecurrente = r; }

    @Override
    public String toString() {
        return super.toString()
             + " | Saldo: $" + String.format("%.2f", saldo)
             + " | Puntos: " + String.format("%.2f", puntos)
             + " | Tipo: " + tipoCliente
             + " | Recurrente: " + (esRecurrente ? "Si" : "No");
    }
}

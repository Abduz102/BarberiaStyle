package barberiaapp.domain;

import java.time.LocalDateTime;

public class Cita {
    private int idCita;
    private LocalDateTime fechaHora;
    private Cliente cliente;
    private Barbero barbero;
    private String tipoServicio;
    private double montoPagado;
    private String metodoPago;
    private String estado;

    public Cita(LocalDateTime fechaHora, Cliente cliente, Barbero barbero,
                String tipoServicio, double montoPagado,
                String metodoPago, String estado) {
        this.fechaHora = fechaHora; this.cliente = cliente;
        this.barbero = barbero; this.tipoServicio = tipoServicio;
        this.montoPagado = montoPagado; this.metodoPago = metodoPago;
        this.estado = estado;
    }
    public Cita() {}

    public int getIdCita() { return idCita; }
    public void setIdCita(int i) { this.idCita = i; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime f) { this.fechaHora = f; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente c) { this.cliente = c; }
    public Barbero getBarbero() { return barbero; }
    public void setBarbero(Barbero b) { this.barbero = b; }
    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String t) { this.tipoServicio = t; }
    public double getMontoPagado() { return montoPagado; }
    public void setMontoPagado(double m) { this.montoPagado = m; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String m) { this.metodoPago = m; }
    public String getEstado() { return estado; }
    public void setEstado(String e) { this.estado = e; }

    @Override
    public String toString() {
        return "Cita #" + idCita
             + " | " + fechaHora
             + " | Cliente: " + (cliente != null ? cliente.getNombre() + " " + cliente.getApellido() : "N/A")
             + " | Barbero: " + (barbero != null ? barbero.getNombre() : "N/A")
             + " | " + tipoServicio
             + " | $" + String.format("%.2f", montoPagado)
             + " | " + metodoPago
             + " | " + estado;
    }
}

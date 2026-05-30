package barberiaapp.domain.enums;

public enum EstadoCitaEnum {
    PENDIENTE("Pendiente"),
    ACEPTADA("Aceptada"),
    RECHAZADA("Rechazada"),
    CANCELADA("Cancelada");

    private final String descripcion;
    EstadoCitaEnum(String d) { this.descripcion = d; }
    public String getDescripcion() { return descripcion; }
}

package barberiaapp.domain.enums;

public enum MetodoPagoEnum {
    SALDO("Saldo Billetera"),
    PUNTOS("Barber Points"),
    CUOTAS("Financiamiento en Cuotas"),
    EFECTIVO("Efectivo");

    private final String descripcion;
    MetodoPagoEnum(String d) { this.descripcion = d; }
    public String getDescripcion() { return descripcion; }
}

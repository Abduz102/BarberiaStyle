package barberiaapp.domain.enums;

public enum TipoServicioEnum {
    CORTE_ESTANDAR("Corte Estandar", 30000),
    PLAN_AHORRO("Plan Ahorro", 25000),
    MEMBRESIA_VIP("Membresia VIP", 120000);

    private final String descripcion;
    private final double precio;

    TipoServicioEnum(String d, double p) { this.descripcion = d; this.precio = p; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
}

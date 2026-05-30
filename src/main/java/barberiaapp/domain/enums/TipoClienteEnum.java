package barberiaapp.domain.enums;

public enum TipoClienteEnum {
    NUEVO("Cliente Nuevo"),
    RECURRENTE("Cliente Recurrente"),
    VIP("Cliente VIP"),
    MOROSO("Cliente en Mora");

    private final String descripcion;
    TipoClienteEnum(String d) { this.descripcion = d; }
    public String getDescripcion() { return descripcion; }
}

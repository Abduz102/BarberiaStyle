package barberiaapp.domain.enums;

public enum EstadoPersonaEnum {
    ACTIVO(true),
    INACTIVO(false);

    private final boolean valor;
    EstadoPersonaEnum(boolean v) { this.valor = v; }
    public boolean getValor() { return valor; }
}

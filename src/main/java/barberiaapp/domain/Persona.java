package barberiaapp.domain;

public abstract class Persona {
    protected int id;
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String password;
    protected boolean estado;

    public Persona(int id, String nombre, String apellido,
                   String email, String password, boolean estado) {
        this.id = id; this.nombre = nombre; this.apellido = apellido;
        this.email = email; this.password = password; this.estado = estado;
    }
    public Persona() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }
    public String getApellido() { return apellido; }
    public void setApellido(String a) { this.apellido = a; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }
    public boolean isEstado() { return estado; }
    public void setEstado(boolean e) { this.estado = e; }

    @Override
    public String toString() {
        return "ID: " + id + " | " + nombre + " " + apellido
             + " | " + email + " | Estado: " + (estado ? "Activo" : "Inactivo");
    }
}

export class Usuario {
    #contrasena; #intentosFallidos;
    constructor(c, n, t, u, p) { this.cedula = c; this.nombreCompleto = n; this.celular = t; this.usuario = u; this.#contrasena = p; this.#intentosFallidos = 0; this.rol = "UsuarioBase"; this.bloqueado = false; }
    getIntentosFallidos() { return this.#intentosFallidos; }
    _setIntentosFallidos(i) { this.#intentosFallidos = i; }
    cambiarContrasena(p) { if (p) this.#contrasena = p; }
    _getContrasenaInterna() { return this.#contrasena; }
    desbloquearCuenta() { this.bloqueado = false; this.#intentosFallidos = 0; }
    login(u, p) {
        if (this.bloqueado) return { success: false, msg: "Bloqueado por 3 intentos." };
        if (this.usuario === u && this.#contrasena === p) { this.#intentosFallidos = 0; return { success: true, msg: "Exitoso." }; }
        this.#intentosFallidos++;
        if (this.#intentosFallidos >= 3) { this.bloqueado = true; return { success: false, msg: "Bloqueado por seguridad." }; }
        return { success: false, msg: `Incorrecto. Intentos: ${this.#intentosFallidos}/3` };
    }
    toJSON() { return { rol: this.rol, cedula: this.cedula, nombreCompleto: this.nombreCompleto, celular: this.celular, usuario: this.usuario, contrasena: this.#contrasena, intentosFallidos: this.#intentosFallidos, bloqueado: this.bloqueado, saldo: this.saldo, puntos: this.puntos, historialCitas: this.historialCitas, historialBancario: this.historialBancario, esRecurrente: this.esRecurrente, especialidad: this.especialidad }; }
}

import { Usuario } from './Usuario.js';

export class Cliente extends Usuario {
    #saldo; #puntos;
    constructor(c, n, t, u, p) { super(c, n, t, u, p); this.rol = "Cliente"; this.#saldo = 0; this.#puntos = 0; this.historialCitas = []; this.esRecurrente = false; }
    get saldo() { return this.#saldo; } get puntos() { return this.#puntos; }
    recargarSaldo(m) { if (m > 0) { this.#saldo += m; return true; } return false; }
    descontarSaldo(m) { this.#saldo -= m; }
    agregarPuntos(c) { this.#puntos += c; }
    descontarPuntos(c) { if (this.#puntos >= c) { this.#puntos -= c; return true; } return false; }
    transferirPuntos(d, c) { if (c > 0 && this.#puntos >= c) { this.descontarPuntos(c); d.agregarPuntos(c); return true; } return false; }
    agendarCita(c) { this.historialCitas.push(c); if (this.historialCitas.length >= 3) this.esRecurrente = true; }
    _restaurarBilletera(s, p) { if (s) this.#saldo = s; if (p) this.#puntos = p; }
    // Este método es detectado automáticamente por JSON.stringify()
    toJSON() {
        return {
            ...this,              // Copia todas las variables públicas (cedula, usuario, etc.)
            saldo: this.saldo,    // Fuerza a guardar el saldo usando tu 'get saldo()'
            puntos: this.puntos   // Fuerza a guardar los puntos usando tu 'get puntos()'
        };
    }
}

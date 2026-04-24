import { Usuario } from './Usuario.js';

export class Cliente extends Usuario {
    #saldo; #puntos;
    constructor(c, n, t, u, p) { super(c, n, t, u, p); this.rol = "Cliente"; this.#saldo = 0; this.#puntos = 0; this.historialCitas = []; this.historialBancario = []; this.esRecurrente = false; }
    get saldo() { return this.#saldo; } get puntos() { return this.#puntos; }
    registrarMovimiento(tipo, monto, detalle) {
        const fecha = new Date().toLocaleString();
        this.historialBancario.push({ fecha, tipo, monto, detalle });
    }
    recargarSaldo(m) { if (m > 0) { this.#saldo += m; this.registrarMovimiento("Depósito", m, "Recarga de Billetera"); return true; } return false; }
    retirarSaldo(m) { if (m > 0 && this.#saldo >= m) { this.#saldo -= m; this.registrarMovimiento("Retiro", m, "Retiro a Cuenta Bancaria"); return true; } return false; }
    simularInteres() { if (this.#saldo > 0) { const interes = this.#saldo * 0.015; this.#saldo += interes; this.registrarMovimiento("Rendimiento", interes, "Interés Mensual 1.5%"); return true; } return false; }
    descontarSaldo(m) { this.#saldo -= m; this.registrarMovimiento("Pago", m, "Pago de Servicio Barbería"); }
    agregarPuntos(c) { this.#puntos += c; }
    descontarPuntos(c) { if (this.#puntos >= c) { this.#puntos -= c; return true; } return false; }
    transferirPuntos(d, c) { if (c > 0 && this.#puntos >= c) { this.descontarPuntos(c); d.agregarPuntos(c); this.registrarMovimiento("Transferencia", c, `Puntos enviados a ${d.usuario}`); d.registrarMovimiento("Recepción", c, `Puntos recibidos de ${this.usuario}`); return true; } return false; }
    agendarCita(c) { this.historialCitas.push(c); if (this.historialCitas.length >= 3) this.esRecurrente = true; }
    _restaurarBilletera(s, p) { if (s) this.#saldo = s; if (p) this.#puntos = p; }
}

import { Usuario } from './Usuario.js';
export class Administrador extends Usuario { 
    #saldo;
    constructor(c, n, t, u, p) { 
        super(c, n, t, u, p); 
        this.rol = "Admin"; 
        this.#saldo = 0;
        this.historialBancario = [];
    }
    get saldo() { return this.#saldo; }
    registrarMovimiento(tipo, monto, detalle) {
        const fecha = new Date().toLocaleString();
        this.historialBancario.push({ fecha, tipo, monto, detalle });
    }
    agregarFondo(m) { 
        if (m > 0) {
            this.#saldo += m;
            this.registrarMovimiento("Ingreso", m, "Fondos transferidos desde un retiro de cliente");
        }
    }
    _restaurarBilletera(s) { if(s) this.#saldo = s; }
}
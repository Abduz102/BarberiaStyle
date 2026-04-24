import { Usuario } from './Usuario.js';
export class Barbero extends Usuario { 
    #saldo;
    constructor(c, n, t, u, p, e) { 
        super(c, n, t, u, p); 
        this.rol = "Barbero"; 
        this.especialidad = e || "General"; 
        this.#saldo = 0;
        this.historialBancario = [];
    } 
    get saldo() { return this.#saldo; }
    registrarMovimiento(tipo, monto, detalle) {
        const fecha = new Date().toLocaleString();
        this.historialBancario.push({ fecha, tipo, monto, detalle });
    }
    recibirPago(monto, detalle = "Pago de Nómina") {
        if (monto > 0) {
            this.#saldo += monto;
            this.registrarMovimiento("Ingreso", monto, detalle);
            return true;
        }
        return false;
    }
    _restaurarBilletera(s) { if(s) this.#saldo = s; }
}

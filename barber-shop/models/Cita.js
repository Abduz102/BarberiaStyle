export class Cita { constructor(s, m, met, b) { this.fecha = new Date().toLocaleString(); this.servicio = s.nombre; this.barbero = b; this.montoPagado = m; this.metodoPago = met; } }

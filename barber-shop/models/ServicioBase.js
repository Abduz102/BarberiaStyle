export class ServicioBase { constructor(n, c) { this.nombre = n; this.costoBase = c; } calcularPrecio(c) { return this.costoBase; } procesarPago(c) { throw new Error(""); } }

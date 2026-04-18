import { Usuario } from './Usuario.js';
export class Barbero extends Usuario { constructor(c, n, t, u, p, e) { super(c, n, t, u, p); this.rol = "Barbero"; this.especialidad = e || "General"; } }

import { Usuario } from './Usuario.js';
export class Administrador extends Usuario { constructor(c, n, t, u, p) { super(c, n, t, u, p); this.rol = "Admin"; } }

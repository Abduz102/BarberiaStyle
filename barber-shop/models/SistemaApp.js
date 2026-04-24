import { Administrador } from './Administrador.js';
import { Barbero } from './Barbero.js';
import { Cliente } from './Cliente.js';

export class SistemaApp {
    constructor() { this.usuarios = []; this.currentUser = null; this.cargar(); }
    guardar() { localStorage.setItem("barberDB_v3", JSON.stringify(this.usuarios)); }
    cargar() {
        const data = localStorage.getItem("barberDB_v3");
        if (data) {
            this.usuarios = JSON.parse(data).map(o => {
                let inst;
                if (o.rol === "Admin") { inst = new Administrador(o.cedula, o.nombreCompleto, o.celular, o.usuario, o.contrasena); inst._restaurarBilletera(o.saldo); inst.historialBancario = o.historialBancario || []; }
                else if (o.rol === "Barbero") { inst = new Barbero(o.cedula, o.nombreCompleto, o.celular, o.usuario, o.contrasena, o.especialidad); inst._restaurarBilletera(o.saldo); inst.historialBancario = o.historialBancario || []; }
                else { 
                    inst = new Cliente(o.cedula, o.nombreCompleto, o.celular, o.usuario, o.contrasena); 
                    inst._restaurarBilletera(o.saldo, o.puntos); 
                    
                    // Parche para citas antiguas sin ID ni estado
                    inst.historialCitas = (o.historialCitas || []).map(cita => {
                        if (!cita.id) cita.id = 'cita-' + Date.now() + '-' + Math.floor(Math.random() * 10000);
                        if (!cita.estado) cita.estado = "Pendiente";
                        return cita;
                    });
                    
                    inst.historialBancario = o.historialBancario || []; 
                    inst.esRecurrente = o.esRecurrente || false; 
                }
                inst.bloqueado = o.bloqueado || false; inst._setIntentosFallidos(o.intentosFallidos || 0); return inst;
            });
        } else this.initDatosFalsos();
    }
    initDatosFalsos() {
        const admin = new Administrador("000", "Administrador", "099", "admin", "admin123");
        const barbero1 = new Barbero("777", "Barbero Mike", "097", "mike", "123", "Estilos Clásicos");
        const barbero2 = new Barbero("888", "Barbero Jhon", "098", "jhon", "123", "Degradados VIP");
        const barbero3 = new Barbero("999", "Barbero Alex", "096", "alex", "123", "Spa de Barba");
        const cl = new Cliente("123", "Cliente Estrella", "091", "cliente", "123"); cl.recargarSaldo(150000); cl.agregarPuntos(10);
        const cl2 = new Cliente("456", "Luis", "092", "luis", "123");
        this.usuarios.push(admin, barbero1, barbero2, barbero3, cl, cl2); this.guardar();
    }
    login(u, p) {
        const usr = this.usuarios.find(x => x.usuario === u); if (!usr) return { success: false, msg: "No encontrado" };
        const res = usr.login(u, p); this.guardar();
        if (res.success) this.currentUser = usr; return res;
    }
    logout() { this.currentUser = null; }
    registrar(c, n, t, u, p) {
        if (this.usuarios.find(x => x.usuario === u)) return { success: false, msg: "Ya existe" };
        this.usuarios.push(new Cliente(c, n, t, u, p)); this.guardar(); return { success: true };
    }
    crudCrear(c, n, t, r, u, p) {
        if (this.usuarios.find(x => x.usuario === u)) return { success: false, msg: "Ya existe" };
        let inst;
        if (r === "Admin") inst = new Administrador(c, n, t, u, p);
        else if (r === "Barbero") inst = new Barbero(c, n, t, u, p, "General");
        else inst = new Cliente(c, n, t, u, p);
        this.usuarios.push(inst); this.guardar(); return { success: true };
    }
    crudEditar(orig, c, n, t, r, u, p) {
        const idx = this.usuarios.findIndex(x => x.usuario === orig); if (idx === -1) return { success: false, msg: "No hallado" };
        let usr = this.usuarios[idx];
        if (u !== orig && this.usuarios.find(x => x.usuario === u)) return { success: false, msg: "El nuevo user existe" };
        const pk = p ? p : usr._getContrasenaInterna();
        if (usr.rol !== r) {
            let inst;
            if (r === "Admin") inst = new Administrador(c, n, t, u, pk); else if (r === "Barbero") inst = new Barbero(c, n, t, u, pk, "General"); else inst = new Cliente(c, n, t, u, pk);
            inst.bloqueado = usr.bloqueado; inst._setIntentosFallidos(usr.getIntentosFallidos()); this.usuarios[idx] = inst;
        } else {
            usr.cedula = c; usr.nombreCompleto = n; usr.celular = t; usr.usuario = u; if (p) usr.cambiarContrasena(p);
        }
        this.guardar(); return { success: true };
    }
    editarMiPerfil(c, n, t, u, p) {
        if (!this.currentUser) return { success: false, msg: "No hay sesión activa" };
        if (u !== this.currentUser.usuario && this.usuarios.find(x => x.usuario === u)) return { success: false, msg: "El nombre de usuario ya está en uso." };
        this.currentUser.cedula = c;
        this.currentUser.nombreCompleto = n;
        this.currentUser.celular = t;
        this.currentUser.usuario = u;
        if (p) this.currentUser.cambiarContrasena(p);
        this.guardar(); return { success: true };
    }
    crudEliminar(orig) {
        if (orig === "admin") return { success: false, msg: "No puede ser eliminado" };
        this.usuarios = this.usuarios.filter(x => x.usuario !== orig); this.guardar(); return { success: true };
    }
    simular() {
        if (this.currentUser.rol !== "Admin") return { success: false, msg: "No aut." };
        let cg = 0;
        this.usuarios.forEach(x => { if (x.rol === "Cliente" && x.saldo > 0) { x.agregarPuntos(x.saldo * 0.015); cg++; } });
        this.guardar(); return { success: true, msg: `Generado 1.5% a ${cg} clientes.` };
    }
}

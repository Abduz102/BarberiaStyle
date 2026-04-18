import { SistemaApp } from '../models/SistemaApp.js';
import { CorteEstandar } from '../models/CorteEstandar.js';
import { PlanAhorro } from '../models/PlanAhorro.js';
import { MembresiaVIP } from '../models/MembresiaVIP.js';
import { Cita } from '../models/Cita.js';

const app = new SistemaApp();
window.showToast = function (msg, isError = false) {
    let t = document.getElementById("app-toast");
    if (!t) { t = document.createElement("div"); t.id = "app-toast"; document.body.appendChild(t); }
    t.innerText = msg;
    t.className = "show " + (isError ? "toast-error" : "toast-success");
    setTimeout(() => { t.className = t.className.replace("show", ""); }, 3000);
};

document.addEventListener("DOMContentLoaded", () => {
    const vD = document.getElementById("dashboard-view");
    const vM = document.getElementById("admin-view"), vB = document.getElementById("barber-view");
    const vL = document.getElementById("landing-view");

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('active');
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1 });
    document.querySelectorAll('.reveal').forEach(el => observer.observe(el));

    // Navegación Dinámica del Menú Fijo
    const navObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active-link'));
                const targetLink = document.querySelector(`.nav-link[href="#${entry.target.id}"]`);
                if (targetLink) targetLink.classList.add('active-link');
            }
        });
    }, { threshold: 0.5 });
    document.querySelectorAll('section[id]').forEach(sec => navObserver.observe(sec));


    document.querySelectorAll('.btn-cerrar, .btn-cerrar-login, .cerrar-modal').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            window.location.hash = '';
        });
    });

    document.getElementById("login-form").addEventListener("submit", (e) => {
        e.preventDefault(); const u = document.getElementById("login-user").value; const p = document.getElementById("login-pass").value;
        const res = app.login(u, p);
        if (res.success) {
            vL.style.display = "none";
            window.location.hash = '';
            if (app.currentUser.rol === "Admin") { vM.style.display = "flex"; renderAdminTable(); }
            else if (app.currentUser.rol === "Barbero") { vB.style.display = "flex"; document.getElementById("welcome-msg-barbero").innerText = `Hola, ${app.currentUser.nombreCompleto}`; renderCitasBarbero(); }
            else { vD.style.display = "flex"; actD(); }
            showToast("Acceso Concedido al Portal");
        } else showToast(res.msg, true);
    });

    document.getElementById("register-form").addEventListener("submit", (e) => {
        e.preventDefault(); const res = app.registrar(document.getElementById("reg-cedula").value, document.getElementById("reg-nombre").value, document.getElementById("reg-celular").value, document.getElementById("reg-user").value, document.getElementById("reg-pass").value);
        if (res.success) { showToast("Registro exitoso. Inicia sesión."); window.location.hash = '#ingresar'; } else showToast(res.msg, true);
    });

    const logOut = () => {
        app.logout();
        vD.style.display = "none"; vM.style.display = "none"; vB.style.display = "none";
        vL.style.display = "block";
        window.location.hash = '';
        document.getElementById("login-form").reset();
        document.getElementById("register-form").reset();
        showToast("Sesión cerrada");
    };
    ["btn-logout", "btn-logout-admin", "btn-logout-barber"].forEach(id => { const el = document.getElementById(id); if (el) el.addEventListener("click", logOut); });

    function actD() {
        document.getElementById("welcome-msg").innerText = `Hola, ${app.currentUser.nombreCompleto}`;
        document.getElementById("disp-saldo").innerText = `$${app.currentUser.saldo.toFixed(2)}`;
        document.getElementById("disp-puntos").innerText = app.currentUser.puntos.toFixed(2);
        const sel = document.getElementById("select-barbero"); sel.innerHTML = "";
        app.usuarios.filter(x => x.rol === "Barbero").forEach(b => sel.innerHTML += `<option value="${b.nombreCompleto}">${b.nombreCompleto} - ${b.especialidad}</option>`);

        const tb = document.querySelector("#historial-table tbody"); tb.innerHTML = "";
        app.currentUser.historialCitas.forEach(c => { tb.innerHTML += `<tr><td>${c.fecha}</td><td><strong style="color:var(--chrome)">${c.servicio}</strong></td><td>${c.barbero}</td><td><span style="color:var(--gold); font-weight:600;">$${c.montoPagado.toFixed(2)}</span></td><td><span style="font-size:0.85rem; border:1px solid var(--border); padding:4px 8px; border-radius:6px; background:rgba(255,255,255,0.05);">${c.metodoPago}</span></td></tr>`; });
    }

    document.getElementById("btn-show-recharge").addEventListener("click", () => { document.getElementById("recharge-section").style.display = "flex"; document.getElementById("transfer-section").style.display = "none"; });
    document.getElementById("btn-show-transfer").addEventListener("click", () => { document.getElementById("transfer-section").style.display = "flex"; document.getElementById("recharge-section").style.display = "none"; });

    document.getElementById("btn-recargar").addEventListener("click", () => { if (app.currentUser.recargarSaldo(parseFloat(document.getElementById("monto-recarga").value))) { app.guardar(); actD(); showToast("Saldo recargado correctamente"); document.getElementById("monto-recarga").value = ""; } else showToast("Monto inválido", true); });
    document.getElementById("btn-transferir").addEventListener("click", () => { const d = app.usuarios.find(x => x.usuario === document.getElementById("destino-transfer").value && x.rol === "Cliente"); if (!d) return showToast("Destino no válido", true); if (app.currentUser.transferirPuntos(d, parseFloat(document.getElementById("monto-transfer").value))) { app.guardar(); actD(); showToast("Se han transferido los puntos"); document.getElementById("destino-transfer").value = ""; document.getElementById("monto-transfer").value = ""; } else showToast("Asegúrate de tener puntos suficientes", true); });

    document.getElementById("select-servicio").addEventListener("change", (e) => document.getElementById("vip-options").style.display = e.target.value === "vip" ? "block" : "none");
    document.getElementById("agendar-form").addEventListener("submit", (e) => {
        e.preventDefault(); const t = document.getElementById("select-servicio").value; const b = document.getElementById("select-barbero").value; let serv;
        if (t === "estandar") serv = new CorteEstandar(); else if (t === "ahorro") serv = new PlanAhorro(25000); else serv = new MembresiaVIP(document.getElementById("select-cuotas").value);
        const res = serv.procesarPago(app.currentUser);
        if (res.success) { app.currentUser.agendarCita(new Cita(serv, serv.calcularPrecio(app.currentUser), res.method, b)); app.guardar(); actD(); showToast("Cita Agendada: " + res.method); } else showToast(res.msg, true);
    });

    const F = document.getElementById("form-crud-user");
    document.getElementById("btn-admin-add").addEventListener("click", () => { 
        document.getElementById("admin-crud-form").style.display = "block"; 
        document.getElementById("form-crud-user").reset();
        document.getElementById("crud-original-user").value = "";
        document.getElementById("crud-pass").required = true;
        document.getElementById("crud-title").innerHTML = `<i class="fa-solid fa-plus"></i> Inyectar Nueva Instancia`;
    });
    document.getElementById("btn-crud-cancel").addEventListener("click", () => { document.getElementById("admin-crud-form").style.display = "none"; });
    document.getElementById("btn-admin-simular").addEventListener("click", () => { const res = app.simular(); showToast(res.msg); actD(); });

    F.addEventListener("submit", (e) => {
        e.preventDefault(); const orig = document.getElementById("crud-original-user").value; const c = document.getElementById("crud-cedula").value, n = document.getElementById("crud-nombre").value, t = document.getElementById("crud-celular").value, r = document.getElementById("crud-rol").value, u = document.getElementById("crud-user").value, p = document.getElementById("crud-pass").value;
        const res = orig ? app.crudEditar(orig, c, n, t, r, u, p) : app.crudCrear(c, n, t, r, u, p);
        if (res.success) { document.getElementById("admin-crud-form").style.display = "none"; showToast("Operación CRUD Guardada"); renderAdminTable(); } else showToast(res.msg, true);
    });

    window.editarUsuarioForm = (o) => {
        const x = app.usuarios.find(u => u.usuario === o); if (!x) return;
        document.getElementById("admin-crud-form").style.display = "block";
        document.getElementById("crud-title").innerHTML = `<i class="fa-solid fa-user-pen"></i> Alterando Instancia: <span style="color:#fff; font-weight:700;">${x.nombreCompleto}</span>`;
        document.getElementById("crud-original-user").value = x.usuario; document.getElementById("crud-cedula").value = x.cedula; document.getElementById("crud-nombre").value = x.nombreCompleto; document.getElementById("crud-celular").value = x.celular; document.getElementById("crud-rol").value = x.rol; document.getElementById("crud-user").value = x.usuario; document.getElementById("crud-pass").value = ""; document.getElementById("crud-pass").required = false;
    };
    window.eliminarUsuarioAction = (o) => { const res = app.crudEliminar(o); if (res.success) { showToast("Usuario Eliminado"); renderAdminTable(); } else showToast(res.msg, true); };
    window.desbloquearUsuarioAction = (o) => { const x = app.usuarios.find(u => u.usuario === o); if (x) { x.desbloquearCuenta(); app.guardar(); showToast("Cuenta reactivada"); renderAdminTable(); } };

    function renderAdminTable() {
        const tb = document.querySelector("#usuarios-table tbody"); tb.innerHTML = "";
        app.usuarios.forEach(x => {
            let bts = `<button onclick="editarUsuarioForm('${x.usuario}')" class="btn-secondary btn-sm" title="Editar"><i class="fa-solid fa-pen"></i></button>`;
            if (x.usuario !== "admin") bts += `<button onclick="eliminarUsuarioAction('${x.usuario}')" class="btn-secondary btn-sm" style="color:var(--error); border-color:rgba(255,255,255,0.1);" title="Eliminar"><i class="fa-solid fa-trash"></i></button>`;
            if (x.bloqueado) bts += `<button onclick="desbloquearUsuarioAction('${x.usuario}')" class="btn-secondary btn-sm" style="color:var(--success); border-color:var(--success);" title="Desbloquear"><i class="fa-solid fa-unlock"></i></button>`;
            tb.innerHTML += `<tr><td>${x.cedula}</td><td><strong>${x.nombreCompleto}</strong></td><td>${x.rol}</td><td>${x.bloqueado ? 'Bloqueado' : 'Activo'}</td><td><div style="display:flex; gap:5px;">${bts}</div></td></tr>`;
        });
    }

    function renderCitasBarbero() {
        const tb = document.querySelector("#citas-barbero-table tbody"); if (!tb) return; tb.innerHTML = ""; let i = 0;
        app.usuarios.forEach(u => { if (u.rol === "Cliente") { u.historialCitas.forEach(c => { if (c.barbero.includes(app.currentUser.nombreCompleto)) { i++; tb.innerHTML += `<tr><td>${c.fecha}</td><td><strong>${u.nombreCompleto}</strong></td><td><span style="color:var(--chrome)">${c.servicio}</span></td><td><span style="font-size:0.85rem; border:1px solid var(--border); padding:4px 8px; border-radius:6px; background:rgba(255,255,255,0.05);">${c.metodoPago}</span></td></tr>`; } }); } });
        if (i === 0) tb.innerHTML = `<tr><td colspan="4" style="text-align:center; padding:3rem; color:var(--text-muted);">El cronograma está libre de citas por ahora. ¡Disfruta tu día!</td></tr>`;
    }
});

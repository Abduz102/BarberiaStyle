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
            const m = document.getElementById('mi-perfil-modal');
            if(m) {
                m.style.visibility = 'hidden';
                m.style.opacity = '0';
            }
        });
    });
    
    // Abrir/Cerrar seccion de mi perfil inline
    document.querySelectorAll('.btn-mi-perfil').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            const u = app.currentUser;
            if (!u) {
                console.error("No hay currentUser definido al abrir perfil");
                return;
            }
            
            // Encontrar la sección de perfil más cercana a la vista actual
            let panelActivo = document.getElementById("dashboard-view").style.display !== "none" ? document.getElementById("dashboard-view") : 
                              (document.getElementById("admin-view").style.display !== "none" ? document.getElementById("admin-view") : document.getElementById("barber-view"));
                              
            const section = panelActivo.querySelector(".mi-perfil-section");
            if (!section) return;

            if (section.style.display === "none") {
                section.querySelector(".perfil-cedula").value = u.cedula || "";
                section.querySelector(".perfil-nombre").value = u.nombreCompleto || "";
                section.querySelector(".perfil-celular").value = u.celular || "";
                section.querySelector(".perfil-usuario").value = u.usuario || "";
                section.querySelector(".perfil-contrasena").value = "";
                section.style.display = "block";
            } else {
                section.style.display = "none";
            }
        });
    });

    document.querySelectorAll('.btn-cerrar-perfil-inline').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            btn.closest(".mi-perfil-section").style.display = "none";
        });
    });

    // Enviar cambios de mi perfil inline
    document.querySelectorAll(".form-mi-perfil-inline").forEach(form => {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            const section = form.closest(".mi-perfil-section");
            const c = section.querySelector(".perfil-cedula").value;
            const n = section.querySelector(".perfil-nombre").value;
            const t = section.querySelector(".perfil-celular").value;
            const u = section.querySelector(".perfil-usuario").value;
            const p = section.querySelector(".perfil-contrasena").value;
            
            const res = app.editarMiPerfil(c, n, t, u, p);
            if (res.success) {
                showToast("Perfil actualizado correctamente");
                section.style.display = "none";
                
                // Refrescar mensajes de bienvenida
                if (app.currentUser.rol === "Admin") {
                    document.getElementById("welcome-msg-admin").innerText = `Hola, ${app.currentUser.nombreCompleto}`;
                } else if (app.currentUser.rol === "Barbero") {
                    document.getElementById("welcome-msg-barbero").innerText = `Hola, ${app.currentUser.nombreCompleto}`;
                } else {
                    document.getElementById("welcome-msg").innerText = `Hola, ${app.currentUser.nombreCompleto}`;
                }
            } else {
                showToast(res.msg, true);
            }
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
        [...app.currentUser.historialCitas].reverse().forEach(c => { 
            let colorEstado = c.estado === "Aceptada" ? "var(--success)" : (c.estado === "Rechazada" ? "var(--error)" : "var(--neon)");
            tb.innerHTML += `<tr><td>${c.fecha}</td><td><strong style="color:var(--chrome)">${c.servicio}</strong></td><td>${c.barbero}</td><td><span style="color:var(--gold); font-weight:600;">$${c.montoPagado.toFixed(2)}</span></td><td><span style="font-size:0.85rem; border:1px solid var(--border); padding:4px 8px; border-radius:6px; background:rgba(255,255,255,0.05);">${c.metodoPago}</span></td><td><strong style="color:${colorEstado}">${c.estado || 'Pendiente'}</strong></td></tr>`; 
        });

        const tbBancario = document.querySelector("#historial-bancario-table tbody");
        if (tbBancario) {
            tbBancario.innerHTML = "";
            (app.currentUser.historialBancario || []).slice().reverse().forEach(m => {
                let colorMonto = ["Retiro", "Pago", "Transferencia"].includes(m.tipo) ? "var(--error)" : "var(--success)";
                tbBancario.innerHTML += `<tr><td>${m.fecha}</td><td><strong>${m.tipo}</strong></td><td><span style="color:${colorMonto}; font-weight:600;">$${m.monto.toFixed(2)}</span></td><td>${m.detalle}</td></tr>`;
            });
        }
    }

    document.getElementById("btn-show-recharge").addEventListener("click", () => { document.getElementById("recharge-section").style.display = "flex"; document.getElementById("transfer-section").style.display = "none"; document.getElementById("withdraw-section").style.display = "none"; document.getElementById("simulador-section").style.display = "none"; });
    document.getElementById("btn-show-transfer").addEventListener("click", () => { document.getElementById("transfer-section").style.display = "flex"; document.getElementById("recharge-section").style.display = "none"; document.getElementById("withdraw-section").style.display = "none"; document.getElementById("simulador-section").style.display = "none"; });
    document.getElementById("btn-show-withdraw").addEventListener("click", () => { document.getElementById("withdraw-section").style.display = "flex"; document.getElementById("recharge-section").style.display = "none"; document.getElementById("transfer-section").style.display = "none"; document.getElementById("simulador-section").style.display = "none"; });
    document.getElementById("btn-show-simulador").addEventListener("click", () => { document.getElementById("simulador-section").style.display = "flex"; document.getElementById("recharge-section").style.display = "none"; document.getElementById("transfer-section").style.display = "none"; document.getElementById("withdraw-section").style.display = "none"; });

    document.getElementById("btn-calcular-simulacion").addEventListener("click", () => {
        const capital = parseFloat(document.getElementById("simulador-capital").value);
        const cuotas = parseInt(document.getElementById("simulador-cuotas").value);
        
        if (!capital || capital <= 0 || !cuotas || cuotas <= 0) {
            showToast("Por favor ingresa un capital y número de cuotas válidos", true);
            return;
        }

        let tasa = 0;
        let tasaStr = "0% (Sin interés)";
        if (cuotas >= 3 && cuotas <= 6) {
            tasa = 0.019; // 1.9%
            tasaStr = "1.9% mensual (Interés moderado)";
        } else if (cuotas >= 7) {
            tasa = 0.023; // 2.3%
            tasaStr = "2.3% mensual (Interés alto)";
        }

        let cuotaMensual = 0;
        if (tasa === 0) {
            cuotaMensual = capital / cuotas;
        } else {
            cuotaMensual = (capital * tasa) / (1 - Math.pow(1 + tasa, -cuotas));
        }

        const totalPagar = cuotaMensual * cuotas;

        document.getElementById("res-tasa").innerText = tasaStr;
        document.getElementById("res-cuota").innerText = "$" + cuotaMensual.toFixed(2);
        document.getElementById("res-total").innerText = "$" + totalPagar.toFixed(2);
        document.getElementById("resultado-simulacion").style.display = "block";
    });

    document.getElementById("btn-recargar").addEventListener("click", () => { if (app.currentUser.recargarSaldo(parseFloat(document.getElementById("monto-recarga").value))) { app.guardar(); actD(); showToast("Saldo recargado correctamente"); document.getElementById("monto-recarga").value = ""; } else showToast("Monto inválido", true); });
    document.getElementById("btn-transferir").addEventListener("click", () => { const d = app.usuarios.find(x => x.usuario === document.getElementById("destino-transfer").value && x.rol === "Cliente"); if (!d) return showToast("Destino no válido", true); if (app.currentUser.transferirPuntos(d, parseFloat(document.getElementById("monto-transfer").value))) { app.guardar(); actD(); showToast("Se han transferido los puntos"); document.getElementById("destino-transfer").value = ""; document.getElementById("monto-transfer").value = ""; } else showToast("Asegúrate de tener puntos suficientes", true); });

    document.getElementById("btn-retirar").addEventListener("click", () => {
        const monto = parseFloat(document.getElementById("monto-retiro").value);
        if (app.currentUser.retirarSaldo(monto)) {
            const admin = app.usuarios.find(x => x.rol === "Admin");
            if (admin) admin.agregarFondo(monto);
            app.guardar(); actD(); showToast("Fondos retirados y transferidos al administrador"); document.getElementById("monto-retiro").value = "";
        } else showToast("Monto inválido o saldo insuficiente", true);
    });

    document.getElementById("btn-simular-mes").addEventListener("click", () => {
        if (app.currentUser.simularInteres()) {
            app.guardar(); actD(); showToast("Mes simulado: 1.5% de interés aplicado");
        } else showToast("Necesitas saldo mayor a cero para generar interés", true);
    });

    document.getElementById("select-servicio").addEventListener("change", (e) => document.getElementById("vip-options").style.display = e.target.value === "vip" ? "block" : "none");
    document.getElementById("agendar-form").addEventListener("submit", (e) => {
        e.preventDefault(); 
        const t = document.getElementById("select-servicio").value; 
        const b = document.getElementById("select-barbero").value; 
        const f = document.getElementById("select-fecha").value;
        const h = document.getElementById("select-hora").value;
        let serv;
        if (t === "estandar") serv = new CorteEstandar(); else if (t === "ahorro") serv = new PlanAhorro(25000); else serv = new MembresiaVIP(document.getElementById("select-cuotas").value);
        const res = serv.procesarPago(app.currentUser);
        if (res.success) { 
            const precio = serv.calcularPrecio(app.currentUser);
            const fechaHoraStr = `${f} ${h}`;
            app.currentUser.agendarCita(new Cita(serv, precio, res.method, b, fechaHoraStr)); 
            const admin = app.usuarios.find(x => x.rol === "Admin");
            if (admin) admin.agregarFondo(precio, `Pago de servicio: ${serv.nombre} por cliente ${app.currentUser.usuario}`);
            app.guardar(); actD(); showToast("Cita Agendada: " + res.method); 
        } else showToast(res.msg, true);
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

    function renderAdminTransacciones() {
        const admin = app.currentUser; // Siendo admin, el currentUser es el administrador
        if (!admin || admin.rol !== "Admin") return;
        document.getElementById("admin-disp-saldo").innerText = `$${admin.saldo.toFixed(2)}`;
        
        const selB = document.getElementById("admin-select-barbero");
        if (selB) {
            selB.innerHTML = '<option value="">Selecciona un barbero...</option>';
            app.usuarios.filter(x => x.rol === "Barbero").forEach(b => {
                selB.innerHTML += `<option value="${b.usuario}">${b.nombreCompleto} (${b.especialidad})</option>`;
            });
        }

        const tb = document.querySelector("#admin-transacciones-table tbody"); 
        if (tb) {
            tb.innerHTML = "";
            (admin.historialBancario || []).slice().reverse().forEach(m => {
                let colorMonto = m.tipo === "Egreso" ? "var(--error)" : "var(--success)";
                tb.innerHTML += `<tr><td>${m.fecha}</td><td><strong>${m.tipo}</strong></td><td><span style="color:${colorMonto}; font-weight:600;">$${m.monto.toFixed(2)}</span></td><td>${m.detalle}</td></tr>`;
            });
        }
    }

    const btnPagarBarbero = document.getElementById("btn-pagar-barbero");
    if (btnPagarBarbero) {
        btnPagarBarbero.addEventListener("click", () => {
            const admin = app.currentUser;
            if (!admin || admin.rol !== "Admin") return;
            
            const barberUser = document.getElementById("admin-select-barbero").value;
            const monto = parseFloat(document.getElementById("admin-monto-pago").value);
            
            if (!barberUser || isNaN(monto) || monto <= 0) {
                return showToast("Completa los datos del pago correctamente.", true);
            }
            
            const barber = app.usuarios.find(x => x.usuario === barberUser);
            if (!barber) return showToast("Barbero no encontrado.", true);
            
            if (admin.retirarFondo(monto, `Pago de nómina a ${barber.nombreCompleto}`)) {
                barber.recibirPago(monto, "Pago de Nómina");
                app.guardar();
                renderAdminTransacciones();
                showToast(`Pago de $${monto} a ${barber.nombreCompleto} realizado con éxito.`);
                document.getElementById("admin-monto-pago").value = "";
                document.getElementById("admin-select-barbero").value = "";
            } else {
                showToast("Saldo insuficiente en la Cuenta Corriente.", true);
            }
        });
    }

    function renderAdminCitas() {
        const tb = document.querySelector("#admin-citas-table tbody"); 
        if (!tb) return;
        tb.innerHTML = "";
        
        let allCitas = [];
        app.usuarios.forEach(u => { 
            if (u.rol === "Cliente") { 
                (u.historialCitas || []).forEach(c => allCitas.push({c, u})); 
            } 
        });
        
        allCitas.sort((a,b) => b.c.fecha.localeCompare(a.c.fecha));
        
        allCitas.forEach(item => {
            const {c, u} = item;
            tb.innerHTML += `<tr><td>${c.fecha}</td><td><strong>${u.nombreCompleto}</strong></td><td><span style="color:var(--chrome)">${c.servicio}</span></td><td>${c.barbero}</td><td><span style="color:var(--gold); font-weight:600;">$${c.montoPagado.toFixed(2)}</span></td><td><span style="font-size:0.85rem; border:1px solid var(--border); padding:4px 8px; border-radius:6px; background:rgba(255,255,255,0.05);">${c.metodoPago}</span></td></tr>`; 
        });
        if (allCitas.length === 0) tb.innerHTML = `<tr><td colspan="6" style="text-align:center; padding:3rem; color:var(--text-muted);">No hay servicios registrados en la base de datos.</td></tr>`;
    }

    const menuUsr = document.getElementById("menu-admin-usuarios");
    const menuCitas = document.getElementById("menu-admin-citas");
    const menuTrx = document.getElementById("menu-admin-transacciones");
    const secCrud = document.getElementById("admin-crud-section");
    const secCitas = document.getElementById("admin-citas-section");
    const secTrx = document.getElementById("admin-transacciones-section");
    
    function resetAdminTabs() {
        if(secCrud) secCrud.style.display = "none";
        if(secCitas) secCitas.style.display = "none";
        if(secTrx) secTrx.style.display = "none";
        if(menuUsr) menuUsr.parentElement.classList.remove("active");
        if(menuCitas) menuCitas.parentElement.classList.remove("active");
        if(menuTrx) menuTrx.parentElement.classList.remove("active");
    }

    if (menuUsr) {
        menuUsr.addEventListener("click", (e) => {
            e.preventDefault();
            resetAdminTabs();
            secCrud.style.display = "block";
            menuUsr.parentElement.classList.add("active");
        });
    }
    if (menuCitas) {
        menuCitas.addEventListener("click", (e) => {
            e.preventDefault();
            resetAdminTabs();
            secCitas.style.display = "block";
            menuCitas.parentElement.classList.add("active");
            renderAdminCitas();
        });
    }
    if (menuTrx) {
        menuTrx.addEventListener("click", (e) => {
            e.preventDefault();
            resetAdminTabs();
            secTrx.style.display = "block";
            menuTrx.parentElement.classList.add("active");
            renderAdminTransacciones();
        });
    }

    window.cambiarEstadoCita = (citaId, clienteUser, nuevoEstado) => {
        const cliente = app.usuarios.find(x => x.usuario === clienteUser && x.rol === "Cliente");
        if (!cliente) return;
        const cita = cliente.historialCitas.find(x => x.id === citaId);
        if (!cita) return;
        
        cita.estado = nuevoEstado;
        
        if (nuevoEstado === "Rechazada") {
            const admin = app.usuarios.find(x => x.rol === "Admin");
            if (admin && admin.retirarFondo(cita.montoPagado, `Reembolso por cita rechazada: ${cita.servicio} (${clienteUser})`)) {
                cliente.recargarSaldo(cita.montoPagado);
                showToast("Cita rechazada y reembolso efectuado al cliente.");
            } else {
                showToast("Cita rechazada. (No se pudo realizar el reembolso automático por falta de fondos del Admin)", true);
            }
        } else {
            showToast("Cita aceptada exitosamente.");
        }
        
        app.guardar();
        renderCitasBarbero();
    };

    function renderCitasBarbero() {
        const tb = document.querySelector("#citas-barbero-table tbody"); if (!tb) return; tb.innerHTML = "";
        
        document.getElementById("barber-disp-saldo").innerText = `$${(app.currentUser.saldo || 0).toFixed(2)}`;
        const tbN = document.querySelector("#nomina-barbero-table tbody"); if (tbN) {
            tbN.innerHTML = "";
            (app.currentUser.historialBancario || []).slice().reverse().forEach(m => {
                let colorMonto = "var(--success)";
                tbN.innerHTML += `<tr><td>${m.fecha}</td><td><strong>${m.tipo}</strong></td><td><span style="color:${colorMonto}; font-weight:600;">$${m.monto.toFixed(2)}</span></td><td>${m.detalle}</td></tr>`;
            });
        }

        let citasAsignadas = [];
        app.usuarios.forEach(u => { 
            if (u.rol === "Cliente") { 
                u.historialCitas.forEach(c => { 
                    if (c.barbero.includes(app.currentUser.nombreCompleto)) { 
                        citasAsignadas.push({c, u});
                    } 
                }); 
            } 
        });
        
        citasAsignadas.sort((a,b) => b.c.fecha.localeCompare(a.c.fecha));
        
        citasAsignadas.forEach(item => {
            const {c, u} = item;
            let actionHtml = `<strong style="color:var(--neon)">${c.estado || 'Pendiente'}</strong>`;
            if (!c.estado || c.estado === "Pendiente") {
                actionHtml = `<button onclick="cambiarEstadoCita('${c.id}', '${u.usuario}', 'Aceptada')" class="btn-secondary btn-sm" style="color:var(--success); border-color:var(--success); margin-right:5px;">Aceptar</button>
                              <button onclick="cambiarEstadoCita('${c.id}', '${u.usuario}', 'Rechazada')" class="btn-secondary btn-sm" style="color:var(--error); border-color:var(--error);">Rechazar</button>`;
            } else if (c.estado === "Aceptada") {
                actionHtml = `<strong style="color:var(--success)">Aceptada</strong>`;
            } else if (c.estado === "Rechazada") {
                actionHtml = `<strong style="color:var(--error)">Rechazada</strong>`;
            }
            
            tb.innerHTML += `<tr><td>${c.fecha}</td><td><strong>${u.nombreCompleto}</strong></td><td><span style="color:var(--chrome)">${c.servicio}</span></td><td><span style="font-size:0.85rem; border:1px solid var(--border); padding:4px 8px; border-radius:6px; background:rgba(255,255,255,0.05);">$${c.montoPagado.toFixed(2)} (${c.metodoPago})</span></td><td>${actionHtml}</td></tr>`; 
        });
        if (citasAsignadas.length === 0) tb.innerHTML = `<tr><td colspan="5" style="text-align:center; padding:3rem; color:var(--text-muted);">El cronograma está libre de citas por ahora. ¡Disfruta tu día!</td></tr>`;
    }
});

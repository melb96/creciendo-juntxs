/**
 * Función centralizada para disparar alerta:
 * 1. Valida el teléfono del tutor.
 * 2. Prepara el mensaje de WhatsApp.
 * 3. Ejecuta el envío de WhatsApp.
 * 4. Envía el formulario al backend para registrar el incidente en BD y notificar.
 */
function ejecutarAlertaSegura(button) {
    // Leemos los datos desde los atributos data-* que pusimos en el botón
    const telefono = button.getAttribute('data-telefono');
    const nombre = button.getAttribute('data-nombre');
    const apellido = button.getAttribute('data-apellido');
    const asistenciaId = button.getAttribute('data-id');

    // 1. Validación de Teléfono
    if (!telefono || telefono.trim() === "" || telefono === "-") {
        alert("Error: El tutor de este infante no registra un número de teléfono válido en su legajo.");
        return;
    }

    let telefonoLimpio = telefono.replace(/[^0-9]/g, "");
    if (!telefonoLimpio.startsWith("54")) {
        telefonoLimpio = (telefonoLimpio.startsWith("0")) ? "549" + telefonoLimpio.substring(1) : "549" + telefonoLimpio;
    }

    // 2. Mensaje personalizado
    let mensaje = `🔔 *AVISO PRIORITARIO - GUARDERÍA CRECIENDO JUNTXS (UNLaR)* 🔔\n\nEstimado/a Tutor/a, nos comunicamos de forma urgente desde el establecimiento institucional. Solicitamos que se ponga en contacto o se acerque a la sala debido a una eventualidad/emergencia relacionada con el infante: *${nombre} ${apellido}*.\n\nQuedamos a su entera disposición.`;
    let mensajeCodificado = encodeURIComponent(mensaje);

    // 3. Confirmación
    if (confirm(`¿Confirmar generación de alerta e informe al tutor de ${nombre} ${apellido}?`)) {
        
        // 4. Abrir WhatsApp en pestaña nueva
        window.open(`https://api.whatsapp.com/send?phone=${telefonoLimpio}&text=${mensajeCodificado}`, '_blank');

        // 5. Enviar el formulario del backend para guardar en bitácora y notificar vía servicio
        const form = document.getElementById('form-alerta-' + asistenciaId);
        if (form) {
            form.submit();
        } else {
            console.error("No se encontró el formulario para la asistencia ID: " + asistenciaId);
        }
    }
}
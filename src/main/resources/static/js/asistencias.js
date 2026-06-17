function dispararAlertaWhatsApp(telefono, nombreNino, apellidoNino) {
    if (!telefono || telefono.trim() === "" || telefono === "-") {
        alert("Error: El tutor de este infante no registra un número de teléfono válido en su legajo.");
        return;
    }

    let telefonoLimpio = telefono.replace(/[^0-9]/g, "");

    if (!telefonoLimpio.startsWith("54")) {
        telefonoLimpio = (telefonoLimpio.startsWith("0")) ? "549" + telefonoLimpio.substring(1) : "549" + telefonoLimpio;
    }

    let mensaje = `🔔 *AVISO PRIORITARIO - GUARDERÍA CRECIENDO JUNTXS (UNLaR)* 🔔\n\nEstimado/a Tutor/a, nos comunicamos de forma urgente desde el establecimiento institucional. Solicitamos que se ponga en contacto o se acerque a la sala debido a una eventualidad/emergencia relacionada con el infante: *${nombreNino} ${apellidoNino}*.\n\nQuedamos a su entera disposición.`;
    let mensajeCodificado = encodeURIComponent(mensaje);

    if (confirm(`¿Confirmar el envío de Alerta de Emergencia por WhatsApp para el tutor de ${nombreNino}?`)) {
        window.open(`https://api.whatsapp.com/send?phone=${telefonoLimpio}&text=${mensajeCodificado}`, '_blank');
    }
}
const publicVapidKey = 'BInFhl3dZ4VqMQFLM5G4vSG9Gb9Kk14BYFpMliRFYloPuCaRQicLJ1O2eDSOnqkhsz5etNgcjcJz4tShF4a9xts'; // PEGA TU CLAVE AQUÍ

async function registrarSuscripcion() {
    if ('serviceWorker' in navigator) {
        try {
            const register = await navigator.serviceWorker.register('/js/sw.js');
            const subscription = await register.pushManager.subscribe({
                userVisibleOnly: true,
                applicationServerKey: urlBase64ToUint8Array(publicVapidKey)
            });
            await fetch('/api/push/guardar', {
                method: 'POST',
                body: JSON.stringify(subscription),
                headers: { 'content-type': 'application/json' }
            });
            alert('¡Notificaciones activadas correctamente!');
        } catch (err) {
            console.error('Error al suscribir:', err);
        }
    }
}

function urlBase64ToUint8Array(base64String) {
    const padding = '='.repeat((4 - base64String.length % 4) % 4);
    const base64 = (base64String + padding).replace(/-/g, '+').replace(/_/g, '/');
    const rawData = window.atob(base64);
    return Uint8Array.from([...rawData].map(char => char.charCodeAt(0)));
}
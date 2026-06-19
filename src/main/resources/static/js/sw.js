self.addEventListener('push', e => {
    console.log("Service Worker: ¡Evento PUSH recibido!");
    const data = e.data ? e.data.text() : 'Sin mensaje';
    self.registration.showNotification('Creciendo Juntxs', {
        body: data,
        icon: '/images/logo-unlar.png'
    }).catch(err => console.error("Error al mostrar notificación: ", err));
});
let localStream = null;

async function encenderWebcam() {
    const videoElement = document.getElementById('webcamPlayer');
    if (!videoElement) return;

    try {
        localStream = await navigator.mediaDevices.getUserMedia({ video: { width: 1280, height: 720 } });
        videoElement.srcObject = localStream;
    } catch (error) {
        console.error("Error al acceder a la webcam: ", error);
        const container = videoElement.parentElement;
        if (container) container.innerHTML = "<p style='color:white; text-align:center; padding:20px;'>Permiso de cámara denegado.</p>";
    }
}

function apagarWebcam() {
    if (localStream) {
        localStream.getTracks().forEach(track => track.stop());
        localStream = null;
    }
    const videoElement = document.getElementById('webcamPlayer');
    if (videoElement) videoElement.srcObject = null;
}

function cambiarCamara(tipo) {
    document.querySelectorAll('.cam-link').forEach(link => link.classList.remove('active'));
    event.currentTarget.classList.add('active');

    if (tipo === 'sala1') {
        encenderWebcam();
    } else if (tipo === 'patio') {
        apagarWebcam();
        alert("Cámara fuera de línea.");
    }
}

window.onload = encenderWebcam;
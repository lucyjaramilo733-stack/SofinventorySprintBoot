document.addEventListener("DOMContentLoaded", function () {

    // Muestra la fecha actual
    const fecha = document.getElementById('fecha');
    if (fecha) {
        const ahora = new Date();
        const opciones = {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        };
        fecha.textContent = ahora.toLocaleDateString('es-CO', opciones);
    }

    // Resalta el item activo del menu
    const navItems = document.querySelectorAll('.nav-item');
    const rutaActual = window.location.pathname;

    navItems.forEach(item => {
        item.classList.remove('active');
        if (item.getAttribute('href') === rutaActual) {
            item.classList.add('active');
        }
    });

});

// Función menú hamburguesa
function toggleMenu() {
    const sidebar = document.querySelector(".sidebar");
    sidebar.classList.toggle("active");
}


// Validación de contraseñas en tiempo real
const password = document.getElementById('password');
const confirmarPassword = document.getElementById('confirmarPassword');
const errorPassword = document.getElementById('errorPassword');
const form = document.getElementById('formUsuario');
const btnGuardar = document.querySelector('.btn-guardar');

confirmarPassword.addEventListener('input', () => {
    if (password.value !== confirmarPassword.value) {
        errorPassword.style.display = 'block';
        confirmarPassword.style.borderColor = '#e74c3c';
        btnGuardar.disabled = true;
    } else {
        errorPassword.style.display = 'none';
        confirmarPassword.style.borderColor = '#1a1a2e';
        btnGuardar.disabled = false;
    }
});

// Validación al enviar el formulario
form.addEventListener('submit', (e) => {
    if (password.value !== confirmarPassword.value) {
        e.preventDefault();
        errorPassword.style.display = 'block';
        return;
    }
    btnGuardar.textContent = 'Guardando...';
    btnGuardar.disabled = true;
});
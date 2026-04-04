// Efecto focus en los inputs
document.querySelectorAll('.input-group input').forEach(input => {
    input.addEventListener('focus', () => {
        input.style.borderColor = '#1a1a2e';
        input.style.boxShadow = '0 0 0 3px rgba(26, 26, 46, 0.1)';
    });

    input.addEventListener('blur', () => {
        input.style.borderColor = '#ddd';
        input.style.boxShadow = 'none';
    });
});

// Deshabilita el botón mientras carga
const form = document.querySelector('form');
const btn = document.querySelector('.btn-login');

form.addEventListener('submit', () => {
    btn.textContent = 'Ingresando...';
    btn.disabled = true;
});
// ── Datos inyectados desde Thymeleaf vía el HTML ──────────────────────────
// Los selects de producto y almacén se clonan de la primera fila,
// por eso es importante que la primera fila siempre exista en el HTML.

// ── Agregar nueva fila de producto ────────────────────────────────────────
function agregarFila() {
    const contenedor = document.getElementById('filasProductos');
    const primeraFila = contenedor.querySelector('.fila-producto');

    // Clonar la primera fila
    const nuevaFila = primeraFila.cloneNode(true);

    // Limpiar los valores de los inputs clonados
    nuevaFila.querySelectorAll('input').forEach(input => {
        input.value = '';
    });

    // Resetear los selects al primer option
    nuevaFila.querySelectorAll('select').forEach(select => {
        select.selectedIndex = 0;
    });

    // Asegurarse que el select de IVA quede en 19% por defecto
    const selectIva = nuevaFila.querySelector('select[name="ivasPorcentaje"]');
    if (selectIva) {
        selectIva.value = '19';
    }

    contenedor.appendChild(nuevaFila);
}

// ── Quitar fila de producto ───────────────────────────────────────────────
function quitarFila(boton) {
    const contenedor = document.getElementById('filasProductos');
    const filas = contenedor.querySelectorAll('.fila-producto');

    // Siempre debe quedar al menos una fila
    if (filas.length <= 1) {
        alert('Debe haber al menos un producto en la compra.');
        return;
    }

    // Eliminar la fila que contiene el botón presionado
    boton.closest('.fila-producto').remove();
}

// ── Validar formulario antes de enviar ───────────────────────────────────
document.getElementById('formCompra').addEventListener('submit', function (e) {
    const filas = document.querySelectorAll('.fila-producto');
    let valido = true;

    filas.forEach((fila, index) => {
        const producto  = fila.querySelector('select[name="productosIds"]').value;
        const almacen   = fila.querySelector('select[name="almacenesIds"]').value;
        const cantidad  = fila.querySelector('input[name="cantidades"]').value;
        const costo     = fila.querySelector('input[name="costosUnitarios"]').value;

        if (!producto || !almacen || !cantidad || !costo) {
            valido = false;
        }

        if (cantidad && parseInt(cantidad) <= 0) {
            valido = false;
        }

        if (costo && parseFloat(costo) < 0) {
            valido = false;
        }
    });

    if (!valido) {
        e.preventDefault();
        alert('Por favor completa todos los campos de productos correctamente.');
    }
});
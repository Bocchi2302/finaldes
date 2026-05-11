document.addEventListener('DOMContentLoaded', () => {
    const button = document.querySelector('[data-toggle-menu]');
    const sidebar = document.getElementById('sidebar');

    if (button && sidebar) {
        button.addEventListener('click', () => sidebar.classList.toggle('open'));
    }

    document.addEventListener('click', (event) => {
        if (!sidebar || !button) return;
        const clickedInsideSidebar = sidebar.contains(event.target);
        const clickedButton = button.contains(event.target);
        if (!clickedInsideSidebar && !clickedButton && window.innerWidth <= 1080) {
            sidebar.classList.remove('open');
        }
    });

    const api = async (url, options = {}) => {
        const response = await fetch(url, {
            headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
            ...options,
        });
        const payload = await response.json();
        if (!response.ok) {
            throw new Error(payload?.message || 'Error de API');
        }
        return payload.data;
    };

    const flash = (message, type = 'success') => {
        const area = document.getElementById('appFlash');
        if (!area) return;
        area.innerHTML = `<div class="flash ${type}">${message}</div>`;
    };

    const money = (value) => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP' }).format(value || 0);

    const pathname = window.location.pathname;

    if (pathname === '/app/login') {
        const form = document.getElementById('loginForm');
        form?.addEventListener('submit', async (event) => {
            event.preventDefault();
            const data = Object.fromEntries(new FormData(form).entries());
            try {
                const auth = await api('/auth/login', {
                    method: 'POST',
                    body: JSON.stringify(data),
                });
                localStorage.setItem('token', auth.token);
                flash('Sesión iniciada correctamente');
                window.location.href = '/app/dashboard';
            } catch (error) {
                flash(error.message, 'error');
            }
        });
    }

    const authToken = localStorage.getItem('token');
    const authHeaders = authToken ? { Authorization: `Bearer ${authToken}` } : {};

    if (pathname === '/app/dashboard') {
        Promise.all([
            api('/productos', { headers: authHeaders }),
            api('/ventas', { headers: authHeaders }),
        ]).then(([products, sales]) => {
            const stats = document.getElementById('dashboardStats');
            const lowStockList = document.getElementById('lowStockList');
            const recentSalesList = document.getElementById('recentSalesList');

            const lowStock = (products || []).filter((product) => product.stock <= product.stockMinimo && product.activo);
            const todaySales = (sales || []).filter((sale) => sale.fechaVenta === new Date().toISOString().slice(0, 10));
            const soldAmount = todaySales.reduce((sum, sale) => sum + Number(sale.total || 0), 0);

            if (stats) {
                stats.innerHTML = `
                    <article class="stat-card peach"><span>Ventas de hoy</span><strong>${money(soldAmount)}</strong><small>Total del día</small></article>
                    <article class="stat-card mint"><span>Productos activos</span><strong>${(products || []).filter((p) => p.activo).length}</strong><small>Catálogo disponible</small></article>
                    <article class="stat-card lavender"><span>Alertas de stock</span><strong>${lowStock.length}</strong><small>Productos por reponer</small></article>
                    <article class="stat-card sky"><span>Ventas registradas</span><strong>${(sales || []).length}</strong><small>Historial reciente</small></article>
                `;
            }

            if (lowStockList) {
                lowStockList.innerHTML = lowStock.length
                    ? lowStock.map((product) => `<div class="soft-item"><div><strong>${product.nombre}</strong><span>Stock ${product.stock} / mínimo ${product.stockMinimo}</span></div><b>${product.estadoStock}</b></div>`).join('')
                    : '<p class="empty">No hay alertas de stock.</p>';
            }

            if (recentSalesList) {
                recentSalesList.innerHTML = (sales || []).slice(0, 5).map((sale) => `
                    <div class="soft-item">
                        <div><strong>${sale.productoNombre}</strong><span>${sale.fechaVenta} · ${sale.cantidad} unidades</span></div>
                        <b>${money(sale.total)}</b>
                    </div>
                `).join('') || '<p class="empty">No hay ventas registradas.</p>';
            }
        }).catch((error) => flash(error.message, 'error'));
    }

    if (pathname === '/app/productos') {
        const tbody = document.getElementById('productsTableBody');
        const count = document.getElementById('productCount');
        const form = document.getElementById('productForm');

        const renderProducts = async () => {
            const products = await api('/productos', { headers: authHeaders });
            if (count) count.textContent = `${products.length} productos`;
            if (tbody) {
                tbody.innerHTML = products.map((product) => `
                    <tr>
                        <td><strong>${product.nombre}</strong></td>
                        <td>${product.categoria}</td>
                        <td>${money(product.precioUnitario)}</td>
                        <td>${product.stock} / mín. ${product.stockMinimo}</td>
                        <td><span class="pill ${String(product.estadoStock).toLowerCase()}">${product.estadoStock}</span></td>
                    </tr>
                `).join('');
            }
        };

        form?.addEventListener('submit', async (event) => {
            event.preventDefault();
            const data = Object.fromEntries(new FormData(form).entries());
            data.precioUnitario = Number(data.precioUnitario);
            data.stock = Number(data.stock);
            data.stockMinimo = Number(data.stockMinimo);
            try {
                await api('/productos', {
                    method: 'POST',
                    headers: authHeaders,
                    body: JSON.stringify(data),
                });
                form.reset();
                flash('Producto registrado correctamente');
                await renderProducts();
            } catch (error) {
                flash(error.message, 'error');
            }
        });

        renderProducts().catch((error) => flash(error.message, 'error'));
    }

    if (pathname === '/app/ventas') {
        const saleForm = document.getElementById('saleForm');
        const productSelect = document.getElementById('saleProductSelect');
        const tbody = document.getElementById('salesTableBody');

        const renderSales = async () => {
            const [products, sales] = await Promise.all([
                api('/productos', { headers: authHeaders }),
                api('/ventas', { headers: authHeaders }),
            ]);

            if (productSelect) {
                productSelect.innerHTML = '<option value="">Selecciona un producto</option>' + products.map((product) => `<option value="${product.id}">${product.nombre} · stock ${product.stock} · ${money(product.precioUnitario)}</option>`).join('');
            }

            if (tbody) {
                tbody.innerHTML = sales.map((sale) => `
                    <tr>
                        <td>${sale.fechaVenta}</td>
                        <td><strong>${sale.productoNombre}</strong></td>
                        <td>${sale.cantidad}</td>
                        <td>${money(sale.total)}</td>
                    </tr>
                `).join('');
            }
        };

        saleForm?.addEventListener('submit', async (event) => {
            event.preventDefault();
            const data = Object.fromEntries(new FormData(saleForm).entries());
            data.productoId = Number(data.productoId);
            data.cantidad = Number(data.cantidad);
            if (!data.fechaVenta) delete data.fechaVenta;
            try {
                await api('/ventas', {
                    method: 'POST',
                    headers: authHeaders,
                    body: JSON.stringify(data),
                });
                saleForm.reset();
                flash('Venta registrada correctamente');
                await renderSales();
            } catch (error) {
                flash(error.message, 'error');
            }
        });

        renderSales().catch((error) => flash(error.message, 'error'));
    }
});

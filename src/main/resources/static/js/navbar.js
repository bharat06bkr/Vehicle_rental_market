/* Dynamic Navbar Renderer */

document.addEventListener('DOMContentLoaded', () => {
    renderNavbar();
});

function renderNavbar() {
    const navElement = document.getElementById('main-navbar');
    if (!navElement) return;

    const user = Auth.getUser();
    const role = user ? user.role : null;
    const activePage = window.location.pathname.split('/').pop() || 'index.html';

    let linksHtml = `<li><a href="index.html" class="${activePage === 'index.html' ? 'active' : ''}">Home</a></li>
                     <li><a href="vehicles.html" class="${activePage === 'vehicles.html' ? 'active' : ''}">Vehicles</a></li>`;

    if (role === 'ROLE_CUSTOMER') {
        linksHtml += `<li><a href="customer-dashboard.html" class="${activePage === 'customer-dashboard.html' ? 'active' : ''}">Dashboard</a></li>
                      <li><a href="my-bookings.html" class="${activePage === 'my-bookings.html' ? 'active' : ''}">My Bookings</a></li>`;
    } else if (role === 'ROLE_OWNER') {
        linksHtml += `<li><a href="owner-dashboard.html" class="${activePage === 'owner-dashboard.html' ? 'active' : ''}">Dashboard</a></li>
                      <li><a href="owner-vehicles.html" class="${activePage === 'owner-vehicles.html' ? 'active' : ''}">My Vehicles</a></li>
                      <li><a href="owner-bookings.html" class="${activePage === 'owner-bookings.html' ? 'active' : ''}">Booking Requests</a></li>`;
    } else if (role === 'ROLE_ADMIN') {
        linksHtml += `<li><a href="admin-dashboard.html" class="${activePage === 'admin-dashboard.html' ? 'active' : ''}">Dashboard</a></li>
                      <li><a href="admin-vehicles.html" class="${activePage === 'admin-vehicles.html' ? 'active' : ''}">Pending Vehicles</a></li>
                      <li><a href="admin-users.html" class="${activePage === 'admin-users.html' ? 'active' : ''}">User Approvals</a></li>`;
    }

    let authButtons = '';
    if (user) {
        const roleLabel = role ? role.replace('ROLE_', '') : '';
        authButtons = `
            <div class="nav-user">
                <span class="badge-role">${roleLabel}</span>
                <span style="font-weight:600; font-size:0.9rem;">${user.name}</span>
                <button onclick="Auth.logout()" class="btn btn-sm btn-danger">Logout</button>
            </div>`;
    } else {
        authButtons = `
            <div style="display:flex; gap:0.75rem;">
                <a href="login.html" class="btn btn-sm btn-outline" style="color:white; border-color:rgba(255,255,255,0.3);">Login</a>
                <a href="register.html" class="btn btn-sm btn-primary">Register</a>
            </div>`;
    }

    navElement.innerHTML = `
        <a href="index.html" class="brand">
            🚗 RentalMarketplace
        </a>
        <ul class="navbar-nav">
            ${linksHtml}
        </ul>
        ${authButtons}
    `;
}

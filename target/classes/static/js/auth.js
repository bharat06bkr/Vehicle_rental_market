/* Authentication & Session Management Module */

const Auth = {
    getUser() {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    },

    getToken() {
        return localStorage.getItem('token');
    },

    isLoggedIn() {
        return !!this.getToken();
    },

    getRole() {
        const user = this.getUser();
        return user ? user.role : null;
    },

    setSession(token, user) {
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
    },

    logout() {
        localStorage.clear();
        showToast('Logged out successfully', 'info');
        setTimeout(() => window.location.href = 'index.html', 1000);
    },

    requireRole(expectedRole) {
        if (!this.isLoggedIn()) {
            window.location.href = 'login.html';
            return false;
        }

        const currentRole = this.getRole();
        if (expectedRole && currentRole !== expectedRole) {
            showToast('Access Denied: Unauthorized role', 'error');
            this.redirectToDashboard();
            return false;
        }
        return true;
    },

    redirectToDashboard() {
        const role = this.getRole();
        if (role === 'ROLE_ADMIN') {
            window.location.href = 'admin-dashboard.html';
        } else if (role === 'ROLE_OWNER') {
            window.location.href = 'owner-dashboard.html';
        } else if (role === 'ROLE_CUSTOMER') {
            window.location.href = 'customer-dashboard.html';
        } else {
            window.location.href = 'index.html';
        }
    }
};

/* Centralized API Helper for Vehicle Rental Marketplace */

const API_BASE = 'http://localhost:8080/api';

const API = {
    async request(endpoint, options = {}) {
        const token = localStorage.getItem('token');
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        try {
            const response = await fetch(`${API_BASE}${endpoint}`, {
                ...options,
                headers
            });

            const data = await response.json().catch(() => null);

            if (!response.ok) {
                if (response.status === 401 || response.status === 403) {
                    // Token expired or invalid
                    if (token && !endpoint.startsWith('/auth')) {
                        showToast('Session expired. Please login again.', 'error');
                        localStorage.clear();
                        setTimeout(() => window.location.href = 'login.html', 1500);
                        return null;
                    }
                }
                const errorMsg = data?.message || (data?.data ? JSON.stringify(data.data) : 'An error occurred');
                throw new Error(errorMsg);
            }

            return data;
        } catch (error) {
            console.error(`API Error [${endpoint}]:`, error);
            showToast(error.message, 'error');
            throw error;
        }
    },

    get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    },

    post(endpoint, body) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(body)
        });
    },

    put(endpoint, body = {}) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body)
        });
    },

    delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
};

function showToast(message, type = 'info') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerText = message;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

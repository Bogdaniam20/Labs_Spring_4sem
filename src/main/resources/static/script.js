if (typeof bootstrap === 'undefined') {
    console.error('Bootstrap не загружен!');
    window.bootstrap = {
        Modal: class {
            constructor(element) { this._element = element; }
            static getInstance() { return null; }
            show() { console.log('Modal show() called'); }
            hide() { console.log('Modal hide() called'); }
        },
        Toast: class {
            constructor(element) { this._element = element; }
            show() { console.log('Toast show() called'); }
            hide() { console.log('Toast hide() called'); }
        }
    };
}

const { Modal, Toast } = bootstrap;

let usersData = [];

async function loadUsers() {
    showLoading(true);

    try {
        console.log('Загрузка пользователей...');
        const response = await fetch('/users/getAll', {
            headers: {
                'Accept': 'application/json'
            }
        });

        console.log('Ответ сервера:', response);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        usersData = await response.json();
        console.log('Получены данные:', usersData);

        renderUsersTable();
    } catch (error) {
        console.error('Ошибка загрузки:', error);
        showToast(`Ошибка при загрузке данных: ${error.message}`, 'danger');
    } finally {
        showLoading(false);
    }
}

function renderUsersTable() {
    const tbody = document.getElementById('userTableBody');
    if (!tbody) {
        console.error('Элемент tbody не найден');
        return;
    }

    tbody.innerHTML = '';

    if (!usersData || usersData.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center py-4 text-muted">
                    <i class="fas fa-users-slash fa-2x mb-3"></i>
                    <p>Нет данных для отображения</p>
                </td>
            </tr>
        `;
        return;
    }

    usersData.forEach(user => {
        if (!user.id || !user.name || !user.surname) {
            console.warn('Некорректные данные пользователя:', user);
            return;
        }

        const initials = (user.name.charAt(0) + user.surname.charAt(0)).toUpperCase();
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${user.id}</td>
            <td>
                <div class="d-flex align-items-center">
                    <div class="user-avatar me-3">${initials}</div>
                    <div>${user.name} ${user.surname}</div>
                </div>
            </td>
            <td>${user.name}</td>
            <td>${user.surname}</td>
            <td>
                <span class="badge bg-success">Активен</span>
            </td>
            <td>
                <button class="btn btn-sm btn-warning me-2" onclick="openEditModal(${user.id})">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="confirmDelete(${user.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function openEditModal(id) {
    const user = usersData.find(u => u.id === id);
    if (!user) {
        console.error('Пользователь не найден, ID:', id);
        showToast('Пользователь не найден', 'danger');
        return;
    }

    document.getElementById('editId').value = user.id;
    document.getElementById('editName').value = user.name;
    document.getElementById('editSurname').value = user.surname;

    const modalElement = document.getElementById('editUserModal');
    if (modalElement) {
        const modal = new Modal(modalElement);
        modal.show();
    } else {
        console.error('Модальное окно редактирования не найдено');
    }
}

async function addUser() {
    const nameInput = document.getElementById('name');
    const surnameInput = document.getElementById('surname');

    if (!nameInput || !surnameInput) {
        console.error('Поля ввода не найдены');
        return;
    }

    const name = nameInput.value.trim();
    const surname = surnameInput.value.trim();

    if (!name || !surname) {
        showToast('Заполните все поля', 'warning');
        return;
    }

    showLoading(true);

    try {
        const response = await fetch('/users/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({ name, surname })
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Ошибка при добавлении');
        }

        const newUser = await response.json();
        usersData.push(newUser);
        renderUsersTable();

        const modalElement = document.getElementById('addUserModal');
        if (modalElement) {
            const modal = Modal.getInstance(modalElement) || new Modal(modalElement);
            modal.hide();
        }

        document.getElementById('addUserForm').reset();
        showToast('Пользователь успешно добавлен', 'success');
    } catch (error) {
        console.error('Ошибка:', error);
        showToast(error.message || 'Ошибка при добавлении', 'danger');
    } finally {
        showLoading(false);
    }
}

async function updateUser() {
    const id = parseInt(document.getElementById('editId').value);
    const name = document.getElementById('editName').value.trim();
    const surname = document.getElementById('editSurname').value.trim();

    if (!id || !name || !surname) {
        showToast('Заполните все поля', 'warning');
        return;
    }

    showLoading(true);

    try {
        const response = await fetch('/users/update', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({ id, name, surname })
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Ошибка при обновлении');
        }

        const updatedUser = await response.json();
        const index = usersData.findIndex(u => u.id === id);

        if (index !== -1) {
            usersData[index] = updatedUser;
        } else {
            usersData.push(updatedUser);
        }

        renderUsersTable();

        const modalElement = document.getElementById('editUserModal');
        if (modalElement) {
            const modal = Modal.getInstance(modalElement);
            if (modal) modal.hide();
        }

        showToast('Данные обновлены', 'success');
    } catch (error) {
        console.error('Ошибка:', error);
        showToast(error.message || 'Ошибка при обновлении', 'danger');
    } finally {
        showLoading(false);
    }
}

function confirmDelete(id) {
    if (confirm('Вы уверены, что хотите удалить этого пользователя?')) {
        deleteUser(id);
    }
}

async function deleteUser(id) {
    showLoading(true);

    try {
        const response = await fetch(`/users/deleteById/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Ошибка при удалении');
        }

        usersData = usersData.filter(user => user.id !== id);
        renderUsersTable();
        showToast('Пользователь удален', 'success');
    } catch (error) {
        console.error('Ошибка:', error);
        showToast(error.message || 'Ошибка при удалении', 'danger');
    } finally {
        showLoading(false);
    }
}

function showLoading(show) {
    const loader = document.getElementById('loading');
    if (loader) {
        loader.style.display = show ? 'flex' : 'none';
    }
}

function showToast(message, type) {
    const toastContainer = document.querySelector('.toast-container');
    if (!toastContainer) {
        console.error('Контейнер для уведомлений не найден');
        return;
    }

    const toastEl = document.createElement('div');
    toastEl.className = `toast show align-items-center text-white bg-${type} border-0`;
    toastEl.role = 'alert';
    toastEl.setAttribute('aria-live', 'assertive');
    toastEl.setAttribute('aria-atomic', 'true');

    toastEl.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${message}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    `;

    toastContainer.appendChild(toastEl);

    if (typeof Toast !== 'undefined') {
        const toast = new Toast(toastEl);
        toast.show();

        setTimeout(() => {
            toast.hide();
            toastEl.addEventListener('hidden.bs.toast', () => {
                toastEl.remove();
            });
        }, 5000);
    } else {
        setTimeout(() => {
            toastEl.remove();
        }, 5000);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    if (typeof bootstrap === 'undefined') {
        showToast('Не удалось загрузить необходимые библиотеки', 'danger');
    }

    loadUsers();

    window.loadUsers = loadUsers;
    window.openEditModal = openEditModal;
    window.addUser = addUser;
    window.updateUser = updateUser;
    window.confirmDelete = confirmDelete;
});
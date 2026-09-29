const api = '/api';
let machines = [], technicians = [], tasks = [], usageLogs = [];

document.querySelectorAll('.nav-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
        btn.classList.add('active');
        const section = document.getElementById(btn.dataset.section);
        section.classList.add('active');
        document.getElementById('pageTitle').textContent = btn.textContent;
    });
});

async function request(url, options = {}) {
    const response = await fetch(url, {
        headers: {'Content-Type': 'application/json', ...(options.headers || {})},
        ...options
    });
    if (!response.ok) {
        let message = `Request failed (${response.status})`;
        try {
            const data = await response.json();
            message = data.error || message;
        } catch {}
        throw new Error(message);
    }
    if (response.status === 204) return null;
    return response.json();
}

function toast(message) {
    const el = document.getElementById('toast');
    el.textContent = message;
    el.classList.add('show');
    setTimeout(() => el.classList.remove('show'), 2800);
}

function showError(error) {
    console.error(error);
    toast(error.message || 'Something went wrong');
}

async function loadAll() {
    try {
        [machines, technicians, tasks] = await Promise.all([
            request(api + '/machines'),
            request(api + '/technicians'),
            request(api + '/maintenance-tasks')
        ]);

        renderDashboard();
        renderMachines();
        renderTechnicians();
        renderTaskOptions();
        renderTasks();

        const logs = [];
        for (const machine of machines) {
            try {
                const data = await request(`${api}/usage-logs/machine/${machine.id}`);
                logs.push(...data);
            } catch {}
        }
        usageLogs = logs;
        renderUsage();
    } catch (e) {
        showError(e);
    }
}

function renderDashboard() {
    document.getElementById('machineCount').textContent = machines.length;
    document.getElementById('technicianCount').textContent = technicians.length;
    const open = tasks.filter(t => t.status === 'OPEN');
    document.getElementById('openTaskCount').textContent = open.length;

    request(api + '/maintenance-tasks/overdue')
        .then(data => document.getElementById('overdueCount').textContent = data.length)
        .catch(() => document.getElementById('overdueCount').textContent = 0);

    const taskBox = document.getElementById('dashboardTasks');
    taskBox.innerHTML = open.slice(0, 5).map(t => `
        <div class="list-item">
            <div><strong>${esc(t.title)}</strong><p>${esc(t.machine?.name || 'Machine')}</p></div>
            <span class="badge open">OPEN</span>
        </div>`).join('') || '<div class="empty">No open maintenance tasks.</div>';

    const machineBox = document.getElementById('dashboardMachines');
    machineBox.innerHTML = machines.slice(0, 5).map(m => `
        <div class="list-item">
            <div><strong>${esc(m.name)}</strong><p>${esc(m.model || 'No model')}</p></div>
            <span>${m.currentUsageHours ?? 0} hrs</span>
        </div>`).join('') || '<div class="empty">No machines registered.</div>';
}

function renderMachines() {
    const tbody = document.getElementById('machineTable');
    tbody.innerHTML = machines.map(m => {
        const interval = `${m.maintenanceIntervalValue} ${m.maintenanceIntervalType === 'HOURS' ? 'hrs' : 'days'}`;
        const near = m.maintenanceIntervalType === 'HOURS' &&
            (m.currentUsageHours - (m.usageHoursAtLastMaintenance || 0)) >= m.maintenanceIntervalValue * .8;
        return `<tr>
            <td>${m.id}</td><td><strong>${esc(m.name)}</strong></td><td>${esc(m.model || '-')}</td>
            <td>${interval}</td><td>${m.currentUsageHours ?? 0} hrs</td>
            <td><span class="badge ${near ? 'overdue' : 'completed'}">${near ? 'MAINTENANCE DUE' : 'OK'}</span></td>
        </tr>`;
    }).join('') || emptyRow(6, 'No machines found.');
}

function renderTechnicians() {
    document.getElementById('technicianTable').innerHTML = technicians.map(t => `
        <tr><td>${t.id}</td><td><strong>${esc(t.name)}</strong></td><td>${esc(t.email || '-')}</td><td>${esc(t.phone || '-')}</td></tr>
    `).join('') || emptyRow(4, 'No technicians found.');
}

function renderTaskOptions() {
    const options = '<option value="">Select machine</option>' +
        machines.map(m => `<option value="${m.id}">${esc(m.name)} (#${m.id})</option>`).join('');
    document.getElementById('uMachine').innerHTML = options;
    document.getElementById('taskMachine').innerHTML = options;
}

function renderUsage() {
    const rows = usageLogs.sort((a,b) => new Date(b.loggedAt) - new Date(a.loggedAt)).map(l => `
        <tr><td>${l.id}</td><td>${esc(l.machine?.name || findMachine(l.machine?.id)?.name || '-')}</td>
        <td>${formatDate(l.loggedAt)}</td><td>${l.hoursUsed}</td></tr>
    `).join('');
    document.getElementById('usageTable').innerHTML = rows || emptyRow(4, 'No usage logs found.');
}

function renderTasks() {
    document.getElementById('taskTable').innerHTML = tasks.sort((a,b) => new Date(b.createdAt) - new Date(a.createdAt)).map(t => `
        <tr>
            <td>${t.id}</td>
            <td>${esc(t.machine?.name || '-')}</td>
            <td><strong>${esc(t.title)}</strong><br><small>${esc(t.description || '')}</small></td>
            <td><span class="badge ${t.status === 'OPEN' ? 'open' : 'completed'}">${t.status}</span></td>
            <td>${formatDate(t.createdAt)}</td>
            <td>${t.status === 'OPEN' ? `<button class="action-btn" onclick="completeTask(${t.id})">Complete</button>` : '—'}</td>
        </tr>
    `).join('') || emptyRow(6, 'No maintenance tasks found.');
}

async function createMachine(e) {
    e.preventDefault();
    try {
        await request(api + '/machines', {
            method: 'POST',
            body: JSON.stringify({
                name: val('mName'), model: val('mModel'),
                maintenanceIntervalType: val('mType'),
                maintenanceIntervalValue: Number(val('mInterval')),
                currentUsageHours: 0,
                usageHoursAtLastMaintenance: 0
            })
        });
        toast('Machine added');
        hideMachineForm();
        e.target.reset();
        await loadAll();
    } catch (e) { showError(e); }
}

async function createTechnician(e) {
    e.preventDefault();
    try {
        await request(api + '/technicians', {
            method: 'POST',
            body: JSON.stringify({name: val('tName'), email: val('tEmail'), phone: val('tPhone')})
        });
        toast('Technician added');
        hideTechnicianForm();
        e.target.reset();
        await loadAll();
    } catch (e) { showError(e); }
}

async function logUsage(e) {
    e.preventDefault();
    try {
        await request(`${api}/usage-logs/machine/${val('uMachine')}`, {
            method: 'POST',
            body: JSON.stringify({hoursUsed: Number(val('uHours'))})
        });
        toast('Usage logged');
        e.target.reset();
        await loadAll();
    } catch (e) { showError(e); }
}

async function createTask(e) {
    e.preventDefault();
    try {
        await request(api + '/maintenance-tasks', {
            method: 'POST',
            body: JSON.stringify({
                machineId: Number(val('taskMachine')),
                title: val('taskTitle'),
                description: val('taskDescription')
            })
        });
        toast('Maintenance task created');
        hideTaskForm();
        e.target.reset();
        await loadAll();
    } catch (e) { showError(e); }
}

async function completeTask(id) {
    if (!technicians.length) {
        toast('Add a technician first');
        return;
    }
    const technicianId = prompt('Enter Technician ID:', technicians[0].id);
    if (!technicianId) return;
    const notes = prompt('Completion notes:', 'Maintenance completed and machine inspected.');
    try {
        await request(`${api}/maintenance-tasks/${id}/complete`, {
            method: 'PUT',
            body: JSON.stringify({technicianId: Number(technicianId), notes: notes || ''})
        });
        toast('Task completed');
        await loadAll();
    } catch (e) { showError(e); }
}

function showMachineForm(){document.getElementById('machineForm').classList.remove('hidden')}
function hideMachineForm(){document.getElementById('machineForm').classList.add('hidden')}
function showTechnicianForm(){document.getElementById('technicianForm').classList.remove('hidden')}
function hideTechnicianForm(){document.getElementById('technicianForm').classList.add('hidden')}
function showTaskForm(){document.getElementById('taskForm').classList.remove('hidden')}
function hideTaskForm(){document.getElementById('taskForm').classList.add('hidden')}

function val(id){return document.getElementById(id).value}
function findMachine(id){return machines.find(m => m.id === id)}
function emptyRow(cols, text){return `<tr><td colspan="${cols}" class="empty">${text}</td></tr>`}
function formatDate(value){return value ? new Date(value).toLocaleString() : '-'}
function esc(value){
    return String(value ?? '').replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
}

loadAll();

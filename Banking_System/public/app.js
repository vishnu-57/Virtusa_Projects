let loggedInUser = null;

// ---- UI helpers ----

function toast(msg, isErr = false) {
    const el = document.getElementById('notification');
    el.textContent = msg;
    el.className = 'notification' + (isErr ? ' error' : '');
    setTimeout(() => el.classList.add('hidden'), 3000);
}

function switchTab(tab) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    document.querySelectorAll('.auth-form').forEach(f => f.classList.remove('active'));
    document.querySelector(`.tab[onclick="switchTab('${tab}')"]`).classList.add('active');
    document.getElementById(`${tab}-form`).classList.add('active');
}

function showModal(id)  { document.getElementById(id).classList.remove('hidden'); }

function closeModal(id) {
    const modal = document.getElementById(id);
    modal.classList.add('hidden');
    modal.querySelector('form').reset();
}

// ---- API wrapper ----

async function callApi(endpoint, method, payload = null) {
    const opts = { method, headers: {} };

    if (payload && method === 'POST') {
        opts.body = new URLSearchParams(payload).toString();
        opts.headers['Content-Type'] = 'application/x-www-form-urlencoded';
    }

    const res  = await fetch('/api' + endpoint, opts);
    const json = await res.json();

    if (!res.ok) throw new Error(json.error || 'Request failed');
    return json;
}

// ---- Auth ----

async function login(e) {
    e.preventDefault();
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;

    try {
        const res = await callApi('/login', 'POST', { username, password });
        if (res.status === 'success') {
            loggedInUser = res.username;
            document.getElementById('auth-section').classList.add('hidden');
            document.getElementById('dashboard-section').classList.remove('hidden');
            loadAccount();
            toast('Welcome back!');
        }
    } catch (err) {
        toast(err.message, true);
    }
}

async function register(e) {
    e.preventDefault();
    const username      = document.getElementById('reg-username').value;
    const password      = document.getElementById('reg-password').value;
    const accountType   = document.getElementById('reg-type').value;
    const initialDeposit = document.getElementById('reg-deposit').value;

    try {
        const res = await callApi('/register', 'POST', { username, password, accountType, initialDeposit });
        if (res.status === 'success') {
            toast('Account created! You can log in now.');
            switchTab('login');
        }
    } catch (err) {
        toast(err.message, true);
    }
}

function logout() {
    loggedInUser = null;
    document.getElementById('auth-section').classList.remove('hidden');
    document.getElementById('dashboard-section').classList.add('hidden');
    document.getElementById('login-form').reset();
    document.getElementById('register-form').reset();
}

// ---- Dashboard ----

async function loadAccount() {
    if (!loggedInUser) return;

    try {
        const data = await callApi(`/account?username=${loggedInUser}`, 'GET');

        document.getElementById('user-display').textContent    = loggedInUser;
        document.getElementById('acc-no-display').textContent  = data.accountNumber;
        document.getElementById('acc-type-display').textContent = data.type;
        document.getElementById('balance-display').textContent = '$' + data.balance.toFixed(2);

        renderTransactions(data.transactions);
    } catch (err) {
        toast(err.message, true);
    }
}

function renderTransactions(txns) {
    const tbody = document.getElementById('transactions-body');
    tbody.innerHTML = '';

    if (!txns || txns.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" style="text-align:center; opacity:0.5">No transactions yet</td></tr>';
        return;
    }

    // Show newest first
    [...txns].reverse().forEach(tx => {
        const isCredit = tx.type.includes('DEPOSIT') || tx.type.includes('IN');
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${tx.timestamp}</td>
            <td><strong style="color: ${isCredit ? 'var(--success)' : 'var(--danger)'}">${tx.type}</strong></td>
            <td>$${tx.amount.toFixed(2)}</td>
            <td>$${tx.balance.toFixed(2)}</td>
        `;
        tbody.appendChild(row);
    });
}

// ---- Transactions ----

async function handleTransaction(e, action, amountFieldId) {
    e.preventDefault();
    const amount = document.getElementById(amountFieldId).value;

    try {
        await callApi('/transaction', 'POST', { action, username: loggedInUser, amount });
        toast(`${action.charAt(0) + action.slice(1).toLowerCase()} successful`);
        const modalId = amountFieldId.replace('-amount', '-modal');
        closeModal(modalId);
        loadAccount();
    } catch (err) {
        toast(err.message, true);
    }
}

async function handleTransfer(e) {
    e.preventDefault();
    const targetAccNo = document.getElementById('transfer-target').value;
    const amount      = document.getElementById('transfer-amount').value;

    try {
        await callApi('/transaction', 'POST', {
            action: 'TRANSFER',
            username: loggedInUser,
            amount,
            targetAccNo
        });
        toast('Transfer complete');
        closeModal('transfer-modal');
        loadAccount();
    } catch (err) {
        toast(err.message, true);
    }
}

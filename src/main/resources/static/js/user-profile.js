function togglePasswordVisibility(inputId) {
	const input = document.getElementById(inputId);
	if (!input) return;

	const btn = input.nextElementSibling;
	const img = btn.querySelector('img');

	if (input.type === 'password') {
		input.type = 'text';
		img.src = '/images/view.png';
	} else {
		input.type = 'password';
		img.src = '/images/eye.png';
	}
}

async function updatePassword(button) {
	if (!validPasswordInputs()) {
		alert('Preencha todos os campos');
		return;
	}

	if (!validNewPassword()) {
		alert('A nova senha não coencide');
		return;
	}



	const currentPassword = document.getElementById('currentPassword').value;
	const newPassword = document.getElementById('newPassword').value;

	const data = {
		currentPassword: currentPassword,
		newPassword: newPassword
	}

	const userId = button.getAttribute('data-user-id');
	const csrfToken = document.getElementById('csrfToken').value;
	const url = `/users/change-password/${userId}`;

	const request = await fetch(url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'X-CSRF-TOKEN': csrfToken
		},
		body: JSON.stringify(data)
	})

	if (!request.ok) {
		throw new Error('Erro ao salvar nova senha');
	}

	const modal = bootstrap.Modal.getInstance(document.getElementById('updatePassword'));
	modal.hide();

	alert('Senha alterada com sucesso!');
	window.location.href = `/users/${userId}`;
}

function validNewPassword() {
	const newPassword = document.getElementById('newPassword').value;
	const newPasswordConfirm = document.getElementById('newPasswordConfirm').value;
	return (newPassword === newPasswordConfirm) && newPassword.trim() !== '' && newPasswordConfirm.trim() !== '';
}

function validPasswordInputs() {
	const currentPassword = document.getElementById('currentPassword').value;
	const newPassword = document.getElementById('newPassword').value;
	const newPasswordConfirm = document.getElementById('newPasswordConfirm').value;

	return currentPassword.trim() !== '' && newPassword.trim() !== '' && newPasswordConfirm.trim() !== '';
}



async function updateUserInfos(button) {
	if (!validUserInputs) {
		alert('As informaçôes nao podem estar vazies');
		return;
	}

	const userId = button.getAttribute('data-user-id');
	const csrfToken = document.getElementById('csrfToken').value;
	const url = `/users/update/${userId}`;

	const usernameInput = document.getElementById('usernameInput').value;
	const emailInput = document.getElementById('emailInput').value;

	const data = {
		usernameInput: usernameInput,
		emailInput: emailInput
	}

	const request = await fetch(url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'X-CSRF-TOKEN': csrfToken
		},
		body: JSON.stringify(data)
	})

	if (!request.ok) {
		throw new Error('Erro ao alterar dados senha');
	}

	alert('Dados alterados com sucesso!');
	window.location.href = `/users/${userId}`;
}

async function saveNewUser() {
	const username = document.getElementById('newUsername').value;
	const email = document.getElementById('newEmail').value;
	const password = document.getElementById('newPassword').value;
	const role = document.getElementById('newRole').value;
	const csrfToken = document.getElementById('csrfToken').value;



	const data = {
		username: username,
		email: email,
		password: password,
		role: role,
	}

	if (!nonNullFields(data)) {
		alert('É necessário preencher todos os campos');
		return;
	}
	
	console.log('passou da conferencia');

	try {
		const request = await fetch('/adm/save/user', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				'X-CSRF-TOKEN': csrfToken
			},
			body: JSON.stringify(data)
		});

		if (!request.ok) {
			throw new Error('Erro ao salvar Despesa');
		}

		alert('Cadastro realizado com sucesso');
		const modal = bootstrap.Modal.getInstance(document.getElementById('newUser'));
		if (modal) modal.hide();
	}
	catch (error) {
		console.error('Erro:', error);
		alert('Falha ao salvar a Despesa: ' + error.message);
	}

}

function nonNullFields(data) {
    return Object.values(data).every(value => value !== null && value !== '' && value !== undefined && value !== 'default');
}












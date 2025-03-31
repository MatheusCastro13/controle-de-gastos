async function saveNewUser() {
	const url = '/users/register'
	const username = document.getElementById('username').value;
	const email = document.getElementById('email').value;
	const password = document.getElementById('password').value;

	const csrfToken = document.getElementById('csrfToken').value;

	const data = {
		username: username,
		email: email,
		password: password
	}

	if (!validUserInputs()) {
		alert('Preencha todos os campos');
		return;
	}

	try {
		const request = await fetch(url, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				'X-CSRF-TOKEN': csrfToken
			},
			body: JSON.stringify(data)
		});

		if (!request.ok) {
			const errorText = await request.text();
			console.log(errorText);
			throw new Error(errorText || 'Erro ao cadastrar usuario');
		}
		
		console.log('Não deu erro');
		alert('Usuario cadastrado com suscesso!');
		window.location.href = '/auth/login';
	}
	catch (error) {
		console.log('deu erro');
		alert('Erro ao cadastrar usuario');
		window.location.href = "/users/register?error=true";
	}
}

function validUserInputs() {
	const username = document.getElementById('username').value;
	const email = document.getElementById('email').value;
	const password = document.getElementById('password').value;

	return username.trim() !== '' && email.trim() !== '' && password.trim() !== '';
}

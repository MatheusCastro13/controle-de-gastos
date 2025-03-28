async function saveNewRevenue() {
	const url = '/revenues/save';
	const description = document.getElementById('newDescription').value;
	const value = document.getElementById('newValue').value;
	const date = document.getElementById('newDate').value;
	const csrfToken = document.getElementById('csrfToken').value;

	const data = {
		description: description,
		value: value,
		date: date
	}
	
	console.log(data);

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
			throw new Error('Erro ao salvar receita');
		}

		const modal = bootstrap.Modal.getInstance(document.getElementById('newRevenue'));
		modal.hide();
		alert('Receita salva com sucesso!');
		window.location.href= "/revenues";
	}
	catch (error) {
		console.error('Erro:', error);
		alert('Falha ao salvar a receita: ' + error.message);
	}

}

async function openEditRevenueModal(button) {
	const revenueId = button.getAttribute('data-edit-revenue-id');
	const url = `/revenues/${revenueId}`

	const request = await fetch(url);

	if (!request.ok) {
		throw new Error('Erro ao buscar receita');
	}

	const response = await request.json();
	console.log(response);
	
	const date = response.date;
	const description = response.description;
	const value = response.value;
	
	const formattedDate = new Date(date).toISOString().split('T')[0];

	const editDateInput = document.getElementById('editDate');
	const editDescriptionInput = document.getElementById('editDescription');
	const editValueInput = document.getElementById('editValue');

	editDateInput.value = formattedDate;
	editDescriptionInput.value = description;
	editValueInput.value = value;

	const editRevenueButton = document.getElementById('editRevenueButton');
	editRevenueButton.setAttribute('data-edit-revenue-id', revenueId);

	const editRevenueModal = new bootstrap.Modal(document.getElementById('editRevenueModal'));
	editRevenueModal.show();

}

async function openDeleteRevenueModal(button) {
	const revenueId = button.getAttribute('data-delete-revenue-id');
	const url = `/revenues/${revenueId}`

	const request = await fetch(url);

	if (!request.ok) {
		throw new Error('Erro ao buscar receita');
	}

	const response = await request.json();
	
	const date = response.date;
	const description = response.description;
	const value = response.value;
	
	const formattedDate = new Date(date).toISOString().split('T')[0];
	
	const deleteDateInput = document.getElementById('deleteDate');
	const deleteDescriptionInput = document.getElementById('deleteDescription');
	const deleteValue = document.getElementById('deleteValue');

	deleteDateInput.value = formattedDate;
	deleteDescriptionInput.value = description;
	deleteValue.value = value;

	const deleteRevenueButton = document.getElementById('deleteRevenueButton');
	deleteRevenueButton.setAttribute('data-delete-revenue-id', revenueId);

	const deleteRevenueModal = new bootstrap.Modal(document.getElementById('deleteRevenueModal'));
	deleteRevenueModal.show();

}


async function saveRevenueUpdates(button) {
	const revenueId = button.getAttribute('data-edit-revenue-id');
	const url = `/revenues/update/${revenueId}`;
	const description = document.getElementById('editDescription').value;
	const value = document.getElementById('editValue').value;
	const date = document.getElementById('editDate').value;
	const csrfToken = document.getElementById('csrfToken').value;


	const data = {
		description: description,
		value: value,
		date: date
	}
	
	console.log(data);

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
			throw new Error('Erro ao editar receita');
		}

		const modal = bootstrap.Modal.getInstance(document.getElementById('editRevenueModal'));
		modal.hide();
		alert('Receita atualizada com sucesso!');
		window.location.href= "/revenues";
	}
	catch (error) {
		console.error('Erro:', error);
		alert('Falha ao editar a receita: ' + error.message);
	}

}

async function deleteRevenue(button) {
	const revenueId = button.getAttribute('data-delete-revenue-id');
	const url = `/revenues/delete/${revenueId}`;
	const csrfToken = document.getElementById('csrfToken').value;
	

	try {
		const request = await fetch(url, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				'X-CSRF-TOKEN': csrfToken
			},
			body: JSON.stringify(revenueId)
		});

		if (!request.ok) {
			throw new Error('Erro ao deletar receita');
		}

		const modal = bootstrap.Modal.getInstance(document.getElementById('deleteRevenueModal'));
		modal.hide();
		alert('Receita deletada com sucesso!');
		window.location.href= "/revenues";
	}
	catch (error) {
		console.error('Erro:', error);
		alert('Falha ao deletar a receita: ' + error.message);
	}

}






























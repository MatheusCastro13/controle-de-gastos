async function saveNewExpense() {
	const url = '/expenses/save';
	const description = document.getElementById('newDescription').value;
	const value = document.getElementById('newValue').value;
	const date = document.getElementById('newDate').value;
	const csrfToken = document.getElementById('csrfToken').value;

	if(!validExpenseInputs()){
		alert('preencha todos os campos');
		return;
	}
	
	const data = {
		description: description,
		value: value,
		date: date
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
			throw new Error('Erro ao salvar Despesa');
		}

		const modal = bootstrap.Modal.getInstance(document.getElementById('newExpense'));
		modal.hide();
		alert('Despesa salva com sucesso!');
		window.location.href = "/expenses";
	}
	catch (error) {
		console.error('Erro:', error);
		alert('Falha ao salvar a Despesa: ' + error.message);
	}

}

async function openEditExpenseModal(button) {
	const expenseId = button.getAttribute('data-edit-expense-id');
	const url = `/expenses/${expenseId}`

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

	const editExpenseButton = document.getElementById('editExpenseButton');
	editExpenseButton.setAttribute('data-edit-expense-id', expenseId);

	const editExpenseModal = new bootstrap.Modal(document.getElementById('editExpenseModal'));
	editExpenseModal.show();

}

async function openDeleteExpenseModal(button) {
	const expenseId = button.getAttribute('data-delete-expense-id');
	const url = `/expenses/${expenseId}`

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

	const deleteExpenseButton = document.getElementById('deleteExpenseButton');
	deleteExpenseButton.setAttribute('data-delete-expense-id', expenseId);

	const deleteExpenseModal = new bootstrap.Modal(document.getElementById('deleteExpenseModal'));
	deleteExpenseModal.show();

}


async function saveExpenseUpdates(button) {
	const expenseId = button.getAttribute('data-edit-expense-id');
	const url = `/expenses/update/${expenseId}`;
	const description = document.getElementById('editDescription').value;
	const value = document.getElementById('editValue').value;
	const date = document.getElementById('editDate').value;
	const csrfToken = document.getElementById('csrfToken').value;

	if(!validUpdateExpenseInputs()){
			alert('preencha todos os campos');
			return;
		}

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

		const modal = bootstrap.Modal.getInstance(document.getElementById('editExpenseModal'));
		modal.hide();
		alert('Despesa atualizada com sucesso!');
		window.location.href = "/expenses";
	}
	catch (error) {
		console.error('Erro:', error);
		alert('Falha ao editar a Despesa: ' + error.message);
	}

}

async function deleteExpense(button) {
	const expenseId = button.getAttribute('data-delete-expense-id');
	const url = `/expenses/delete/${expenseId}`;
	const csrfToken = document.getElementById('csrfToken').value;


	try {
		const request = await fetch(url, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				'X-CSRF-TOKEN': csrfToken
			},
			body: JSON.stringify(expenseId)
		});

		if (!request.ok) {
			throw new Error('Erro ao deletar receita');
		}

		const modal = bootstrap.Modal.getInstance(document.getElementById('deleteExpenseModal'));
		modal.hide();
		alert('Despesa deletada com sucesso!');
		window.location.href = "/expenses";
	}
	catch (error) {
		console.error('Erro:', error);
		alert('Falha ao Despesa a receita: ' + error.message);
	}

}

function validExpenseInputs() {
	const description = document.getElementById('newDescription').value;
	const value = document.getElementById('newValue').value;
	const date = document.getElementById('newDate').value;

	return description !== '' && value !== '' && date !== ''; 
}

function validUpdateExpenseInputs() {
	const description = document.getElementById('editDescription').value;
	const value = document.getElementById('editValue').value;
	const date = document.getElementById('editDate').value;

	return description !== '' && value !== '' && date !== ''; 
}


























package matheusresio.controle_de_gastos.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import matheusresio.controle_de_gastos.model.Expense;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.ExpenseDto;
import matheusresio.controle_de_gastos.model.dto.ExpenseResponse;
import matheusresio.controle_de_gastos.repository.ExpenseRepository;
import matheusresio.controle_de_gastos.repository.UserRepository;

@Service
public class ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final UserRepository userRepository;
	
	@Autowired
	public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
		this.expenseRepository = expenseRepository;
		this.userRepository = userRepository;
	}
	
	@Transactional
	public void save(ExpenseDto expenseDto, User user) throws IllegalArgumentException{
		Expense expense = new Expense();
		expense.setDescription(expenseDto.getDescription());
		expense.setExpenseDate(expenseDto.getDate());
		expense.setExpenseValue(new BigDecimal(expenseDto.getValue()));
		expenseAssociations(expense, user);
	}
	
	private void expenseAssociations(Expense expense, User user) {
		user.addExpense(expense);
		userRepository.save(user);
	}

	@Transactional
	public void update(Long id, ExpenseDto expenseDto, User user) throws EntityNotFoundException, IllegalArgumentException{
		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Receita não encontrada"));
		expense.setDescription(expenseDto.getDescription());
		expense.setExpenseDate(expenseDto.getDate());
		expense.setExpenseValue(new BigDecimal(expenseDto.getValue()));
		expenseAssociations(expense, user);
	}

	@Transactional
	public void delete(Long id, User user) throws EntityNotFoundException{
		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Receita não encontrada"));
		user.removeExpense(expense);
	}

	public Expense findById(Long id) {
		return expenseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Receita não encontrada"));
	}
	
	public ExpenseResponse findResponseById(Long id) throws EntityNotFoundException{
		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Receita não encontrada"));
		
		return new ExpenseResponse(expense.getId(), expense.getExpenseDate(),
				expense.getDescription(), expense.getExpenseValue());
	}
}














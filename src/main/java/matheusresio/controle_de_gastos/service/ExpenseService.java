package matheusresio.controle_de_gastos.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import matheusresio.controle_de_gastos.model.Expense;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.ExpenseDto;
import matheusresio.controle_de_gastos.model.dto.ExpenseResponse;
import matheusresio.controle_de_gastos.repository.ExpenseRepository;
import matheusresio.controle_de_gastos.repository.UserRepository;
import matheusresio.controle_de_gastos.repository.specifications.ExpenseSpecifications;

@Service
public class ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final UserRepository userRepository;
	private final TransactionService transactionService;
	
	@Autowired
	public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, 
			TransactionService transactionService) {
		this.expenseRepository = expenseRepository;
		this.userRepository = userRepository;
		this.transactionService = transactionService;
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
		user.addTransaction(transactionService.byExpense(expense));
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
		transactionService.deleteFromExpenseId(id);
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

	public Page<Expense> findByUser(User user, Pageable pageable) {
		return expenseRepository.findAllByUser(user, pageable);
	}

	public Page<Expense> findByUser(User user, Pageable pageable, DateRangeFilter dateRangeFilter,
			MonthYearFilter monthYearFilter) {
		Specification<Expense> spec = ExpenseSpecifications.belongsTo(user);

	    if (dateRangeFilter != null) {
	        spec = spec.and(ExpenseSpecifications.byDateRange(dateRangeFilter.getStartDate(), dateRangeFilter.getEndDate()));
	        Page<Expense> expenses = expenseRepository.findAll(spec, pageable);
	        return expenses;
	        
	    } else if (monthYearFilter != null) {
	        spec = spec.and(ExpenseSpecifications.byMonthAndYear(monthYearFilter.getMonth(), monthYearFilter.getYear()));
	        Page<Expense> expenses = expenseRepository.findAll(spec, pageable);
	        expenses.forEach(System.out::println);
		    return expenses;
		    
	    } else {
	    	
	        LocalDate now = LocalDate.now();
	        spec = spec.and(ExpenseSpecifications.byMonthAndYear(now.getMonthValue(), now.getYear()));
	        Page<Expense> expenses = expenseRepository.findAll(spec, pageable);
	        expenses.forEach(System.out::println);
		    return expenses;
	    }
	}
}














package matheusresio.controle_de_gastos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import matheusresio.controle_de_gastos.model.Expense;
import matheusresio.controle_de_gastos.model.Revenue;
import matheusresio.controle_de_gastos.model.dto.Transaction;
import matheusresio.controle_de_gastos.model.enums.TransactionType;
import matheusresio.controle_de_gastos.repository.TransactionRepository;

@Service
public class TransactionService {

	private final TransactionRepository transactionRepository;
	
	@Autowired
	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public Transaction byExpense(Expense expense) {
		Transaction transaction = transactionRepository.findByTransactionTypeIdAndTransactionType(expense.getId(), TransactionType.EXPENSE)
				.orElse(new Transaction());
		
		transaction.setDescription(expense.getDescription());
		transaction.setDate(expense.getExpenseDate());
		transaction.setValue(expense.getExpenseValue());
		transaction.setTransactionType(TransactionType.EXPENSE);
		transaction.setTransactionTypeId(expense.getId());
		
		return transaction;
	}
	
	public Transaction byRevenue(Revenue revenue) {
		Transaction transaction = transactionRepository.findByTransactionTypeIdAndTransactionType(revenue.getId(), TransactionType.REVENUE)
				.orElse(new Transaction());
		transaction.setDescription(revenue.getDescription());
		transaction.setDate(revenue.getRevenueDate());
		transaction.setValue(revenue.getRevenueValue());
		transaction.setTransactionType(TransactionType.REVENUE);
		transaction.setTransactionTypeId(revenue.getId());
		
		return transaction;
	}

	public void deleteFromRevenueId(Long id) {
		Transaction transaction = transactionRepository.findByTransactionTypeIdAndTransactionType(id, TransactionType.REVENUE)
				.orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));
		transactionRepository.delete(transaction);
	}
	
	public void deleteFromExpenseId(Long id) {
		Transaction transaction = transactionRepository.findByTransactionTypeIdAndTransactionType(id, TransactionType.EXPENSE)
				.orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));
		transactionRepository.delete(transaction);
	}
	
}

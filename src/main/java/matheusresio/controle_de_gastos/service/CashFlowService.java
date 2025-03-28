package matheusresio.controle_de_gastos.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import matheusresio.controle_de_gastos.model.Expense;
import matheusresio.controle_de_gastos.model.Revenue;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.CashFlow;
import matheusresio.controle_de_gastos.model.dto.Transaction;
import matheusresio.controle_de_gastos.model.enums.TransactionType;

@Service
public class CashFlowService {

	public CashFlow userCashFlow(User user) {
		return generateCashFlow(user.getRevenues(), user.getExpenses());
	}
	
	private CashFlow generateCashFlow(List<Revenue> revenues, List<Expense> expenses) {
		List<Transaction> transactions = generateTransactions(revenues, expenses);
		CashFlow cashFlow = setCashFlowIndicators(transactions);
		
		return cashFlow;
	}

	private List<Transaction> generateTransactions(List<Revenue> revenues, List<Expense> expenses) {
		List<Transaction> transactions = new ArrayList<>();
		
		transactions.addAll(revenues.stream().map(rev -> 
								new Transaction(TransactionType.REVENUE,
										rev.getRevenueValue(),
										rev.getDescription(),
										rev.getId(),
										rev.getRevenueDate())).toList());
		
		transactions.addAll(expenses.stream().map(exp -> 
								new Transaction(TransactionType.EXPENSE,
										exp.getExpenseValue().multiply(new BigDecimal("-1")),
										exp.getDescription(),
										exp.getId(),
										exp.getExpenseDate())).toList());
		
		transactions.sort(Comparator
			    .comparing(Transaction::getDate, Comparator.reverseOrder())
			    .thenComparing(Transaction::getTransactionId)
			    .thenComparing(t -> t.getTransactionType() == TransactionType.REVENUE ? -1 : 1)
			);
		
		return transactions;
	}
	
	private CashFlow setCashFlowIndicators(List<Transaction> transactions) {
		BigDecimal totalRevenue = totalTransactionValue(transactions, TransactionType.REVENUE);
		BigDecimal totalExpense = totalTransactionValue(transactions, TransactionType.EXPENSE);
		BigDecimal result = totalRevenue.add(totalExpense);
		BigDecimal biggestRevenue = biggestOf(transactions, TransactionType.REVENUE);
		BigDecimal biggestExpense = biggestOf(transactions, TransactionType.EXPENSE);
		BigDecimal averageRevenue = averageOf(transactions, TransactionType.REVENUE);
		BigDecimal averageExpense = averageOf(transactions, TransactionType.EXPENSE);
		
		return new CashFlow(transactions, 
							totalRevenue, 
							totalExpense, 
							result, 
							biggestRevenue, 
							biggestExpense, 
							averageRevenue, 
							averageExpense);
		
	}
	
	private BigDecimal totalTransactionValue(List<Transaction> transactions, TransactionType criteria) {
		return transactions.stream()
				.filter(t -> t.getTransactionType().equals(criteria))
				.map(Transaction::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	private BigDecimal biggestOf(List<Transaction> transactions, TransactionType criteria) {
	    return transactions.stream()
	            .filter(t -> t.getTransactionType().equals(criteria))
	            .map(Transaction::getValue)
	            .reduce((t1, t2) -> criteria == TransactionType.REVENUE ? t1.max(t2) : t1.min(t2))
	            .orElse(BigDecimal.ZERO);
	}

	
	private BigDecimal averageOf(List<Transaction> transactions, TransactionType criteria) {
		int size = transactions.stream()
				.filter(t -> t.getTransactionType().equals(criteria))
				.toList()
				.size();
		
		BigDecimal totalTransactionValue = totalTransactionValue(transactions, criteria);
		
		if(size == 0) return BigDecimal.ZERO;
		
		BigDecimal average = totalTransactionValue.divide(new BigDecimal(size), 2, RoundingMode.HALF_UP); 

		return average;
	}
	
	
}

























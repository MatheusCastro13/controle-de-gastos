package matheusresio.controle_de_gastos.model.dto;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CashFlow {

	@ToString.Exclude
	private Page<Transaction> transactions;
	private BigDecimal totalRevenue;
	private BigDecimal totalExpense;
	private BigDecimal result;
	private BigDecimal biggestRevenue;
	private BigDecimal biggestExpense;
	private BigDecimal averageRevenue;
	private BigDecimal averageExpense;
}

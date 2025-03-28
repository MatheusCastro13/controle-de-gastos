package matheusresio.controle_de_gastos.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matheusresio.controle_de_gastos.model.enums.TransactionType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {

	private TransactionType transactionType;
	private BigDecimal value;
	private String description;
	private Long transactionId;
	private Date date;
	
		
	
}

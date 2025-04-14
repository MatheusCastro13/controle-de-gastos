package matheusresio.controle_de_gastos.service;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class DateRangeFilter{

	private LocalDate startDate;
	private LocalDate endDate;
	
}

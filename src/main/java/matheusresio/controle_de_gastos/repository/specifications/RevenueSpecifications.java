package matheusresio.controle_de_gastos.repository.specifications;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import matheusresio.controle_de_gastos.model.Revenue;
import matheusresio.controle_de_gastos.model.User;

public class RevenueSpecifications{

	public static Specification<Revenue> belongsTo(User user) {
        return (root, query, cb) -> cb.equal(root.get("user"), user);
    }

    public static Specification<Revenue> byDateRange(LocalDate start, LocalDate end) {
        return (root, query, cb) -> cb.between(root.get("revenueDate"), start, end);
    }

    public static Specification<Revenue> byMonthAndYear(int month, int year) {
        System.out.println("mes: " + month);
        List<Integer> thirtyDaysMonth = List.of(4, 6, 9, 11);
        List<Integer> thirtyOneDaysMonth = List.of(1, 3, 5, 7, 8, 10, 12);
        List<Integer> twentyEightDaysMonth = List.of(2);
        
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end;

        if (thirtyDaysMonth.contains(month)) {
            end = LocalDate.of(year, month, 30);
        } else if (thirtyOneDaysMonth.contains(month)) {
            end = LocalDate.of(year, month, 31);
        } else if (twentyEightDaysMonth.contains(month)) {
            if (IsoChronology.INSTANCE.isLeapYear(year)) {
                end = LocalDate.of(year, month, 29);
            } else {
                end = LocalDate.of(year, month, 28);
            }
        } else {
        	return (root, query, cb) -> cb.between(root.get("revenueDate"), LocalDate.now(), LocalDate.now());
        }

        return (root, query, cb) -> cb.between(root.get("revenueDate"), start, end);
    }
	
	
}

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
import matheusresio.controle_de_gastos.model.Revenue;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.RevenueDto;
import matheusresio.controle_de_gastos.model.dto.RevenueResponse;
import matheusresio.controle_de_gastos.repository.RevenueRepository;
import matheusresio.controle_de_gastos.repository.UserRepository;
import matheusresio.controle_de_gastos.repository.specifications.RevenueSpecifications;

@Service
public class RevenueService {

	private final RevenueRepository revenueRepository;
	private final UserRepository userRepository;
	private final TransactionService transactionService;
	
	@Autowired
	public RevenueService(RevenueRepository revenueRepository, UserRepository userRepository, 
			TransactionService transactionService) {
		this.revenueRepository = revenueRepository;
		this.userRepository = userRepository;
		this.transactionService = transactionService;
	}
	
	@Transactional
	public void save(RevenueDto revenueDto, User user) throws IllegalArgumentException{
		Revenue revenue = new Revenue();
		revenue.setDescription(revenueDto.getDescription());
		revenue.setRevenueDate(revenueDto.getDate());
		revenue.setRevenueValue(new BigDecimal(revenueDto.getValue()));
		revenueAssociations(revenue, user);
	}
	
	private void revenueAssociations(Revenue revenue, User user) {
		user.addRevenue(revenue);
		user.addTransaction(transactionService.byRevenue(revenue));
		userRepository.save(user);
	}

	@Transactional
	public void update(Long id, RevenueDto revenueDto, User user) throws EntityNotFoundException, IllegalArgumentException{
		Revenue revenue = revenueRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Receita não encontrada"));
		revenue.setDescription(revenueDto.getDescription());
		revenue.setRevenueDate(revenueDto.getDate());
		revenue.setRevenueValue(new BigDecimal(revenueDto.getValue()));
		revenueAssociations(revenue, user);
	}

	@Transactional
	public void delete(Long id, User user) throws EntityNotFoundException{
		Revenue revenue = revenueRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Receita não encontrada"));
		user.removeRevenue(revenue);
		transactionService.deleteFromRevenueId(id);
	}

	public Revenue findById(Long id) {
		return revenueRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Receita não encontrada"));
	}
	
	public RevenueResponse findResponseById(Long id) throws EntityNotFoundException{
		Revenue revenue = revenueRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Receita não encontrada"));
		
		return new RevenueResponse(revenue.getId(), revenue.getRevenueDate(),
										revenue.getDescription(), revenue.getRevenueValue());
	}

	public Page<Revenue> findAllByUser(User user, Pageable pageable) {
		return revenueRepository.findAllByUser(user, pageable);
	}

	public Page<Revenue> findAllByUser(User user, Pageable pageable, DateRangeFilter dateRangeFilter,
			MonthYearFilter monthYearFilter) {
		Specification<Revenue> spec = RevenueSpecifications.belongsTo(user);

	    if (dateRangeFilter != null) {
	        spec = spec.and(RevenueSpecifications.byDateRange(dateRangeFilter.getStartDate(), dateRangeFilter.getEndDate()));
	        Page<Revenue> revenues = revenueRepository.findAll(spec, pageable);
	        return revenues;
	        
	    } else if (monthYearFilter != null) {
	        spec = spec.and(RevenueSpecifications.byMonthAndYear(monthYearFilter.getMonth(), monthYearFilter.getYear()));
	        Page<Revenue> revenues = revenueRepository.findAll(spec, pageable);
		    return revenues;
		    
	    } else {
	    	
	        LocalDate now = LocalDate.now();
	        spec = spec.and(RevenueSpecifications.byMonthAndYear(now.getMonthValue(), now.getYear()));
	        Page<Revenue> revenues = revenueRepository.findAll(spec, pageable);
		    return revenues;
	    }
	}
}














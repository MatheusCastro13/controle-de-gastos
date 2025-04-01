package matheusresio.controle_de_gastos.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import matheusresio.controle_de_gastos.model.Revenue;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.RevenueDto;
import matheusresio.controle_de_gastos.model.dto.RevenueResponse;
import matheusresio.controle_de_gastos.repository.RevenueRepository;
import matheusresio.controle_de_gastos.repository.UserRepository;

@Service
public class RevenueService {

	private final RevenueRepository revenueRepository;
	private final UserRepository userRepository;
	
	@Autowired
	public RevenueService(RevenueRepository revenueRepository, UserRepository userRepository) {
		this.revenueRepository = revenueRepository;
		this.userRepository = userRepository;
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
}














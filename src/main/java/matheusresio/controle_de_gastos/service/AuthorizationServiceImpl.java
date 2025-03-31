package matheusresio.controle_de_gastos.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.repository.UserRepository;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

	private final UserRepository userRepository;

	public AuthorizationServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void verifyUserAccess(User userAuthenticated, UUID targetUserId)
			throws AccessDeniedException, EntityNotFoundException {
		
		Optional<User> optionalUser = userRepository.findById(targetUserId);
		if (optionalUser.isEmpty()) {
			throw new EntityNotFoundException("User com id: " + targetUserId + " não foi encontrado");
		}

		if ("ROLE_USER".equals(userAuthenticated.getRole().getName())
				&& !userAuthenticated.getId().equals(targetUserId)) {
			throw new AccessDeniedException("Você não tem permissão para ver os dados desse usuário");
		}
	}
}

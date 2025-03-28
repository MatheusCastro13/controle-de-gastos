package matheusresio.controle_de_gastos.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import matheusresio.controle_de_gastos.exceptions.CredentialsAlreadyUsedException;
import matheusresio.controle_de_gastos.model.Role;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.UserRegister;
import matheusresio.controle_de_gastos.model.dto.UserRegisterAdm;
import matheusresio.controle_de_gastos.repository.RoleRepository;
import matheusresio.controle_de_gastos.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository, 
			BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Optional<User> findByEmail(String email) {
		Optional<User> user =  userRepository.findByEmail(email);
		return user;
		
	}

	@Transactional
	public void save(UserRegister userRegister) throws CredentialsAlreadyUsedException {
		Role role = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new EntityNotFoundException("Role não encontrada"));
		
		if(!validCredentials(userRegister.username(), userRegister.email())) {
			throw new CredentialsAlreadyUsedException("Username ou email já utilizado");
		}
		
		User user = new User();
		user.setUsername(userRegister.username());
		user.setEmail(userRegister.email());
		user.setPassword(passwordEncoder.encode(userRegister.password()));
		user.setRole(role);
		
		userRepository.save(user);
	}
	
	@Transactional
	public void save(UserRegisterAdm userDto) throws CredentialsAlreadyUsedException {
		System.out.println("userDto.role() = " + userDto.role());
		Role role = roleRepository.findByName("ROLE_" + userDto.role())
				.orElseThrow(() -> new EntityNotFoundException("Role não encontrada"));
		
		if(!validCredentials(userDto.username(), userDto.email())) {
			System.out.println("credenciais invalidas");
			throw new CredentialsAlreadyUsedException("Username ou email já utilizado");
		}
		
		User user = new User();
		user.setUsername(userDto.username());
		user.setEmail(userDto.email());
		user.setPassword(passwordEncoder.encode(userDto.password()));
		user.setRole(role);
		System.out.println("user a ser salvo: " + user);
		userRepository.save(user);
	}

	private boolean validCredentials(String username, String email) {
		Optional<User> userByUsername = userRepository.findByUsername(username);
		Optional<User> userByEmail = userRepository.findByEmail(email);
		
		if(userByEmail.isPresent() || userByUsername.isPresent()) {
			return false;
		}
		
		return true;
	}

	public void userProfileCredentials(User user, UUID id) throws AccessDeniedException{
		if(!user.getId().equals(id) ) {
			throw new AccessDeniedException("Você não tem permissão para ver os dados desse usuário");
		}
		
		User userSearched = userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("User com id: " + id + "não foi encontrado"));
		
		if(!userSearched.equals(user)) {
			throw new AccessDeniedException("Você não tem permissão para ver os dados desse usuário : "+
											"\nSeu id: " + user.getId() + ", id que esta tentando acessar: " + userSearched.getId());
		}
		
	}
	
}







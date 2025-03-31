package matheusresio.controle_de_gastos.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import matheusresio.controle_de_gastos.exceptions.CredentialsAlreadyUsedException;
import matheusresio.controle_de_gastos.exceptions.SameCredentialsException;
import matheusresio.controle_de_gastos.exceptions.SamePasswordException;
import matheusresio.controle_de_gastos.exceptions.WrongPasswordException;
import matheusresio.controle_de_gastos.model.Role;
import matheusresio.controle_de_gastos.model.User;
import matheusresio.controle_de_gastos.model.dto.PasswordChange;
import matheusresio.controle_de_gastos.model.dto.UserRegister;
import matheusresio.controle_de_gastos.model.dto.UserRegisterAdm;
import matheusresio.controle_de_gastos.model.dto.UserUpdateDto;
import matheusresio.controle_de_gastos.repository.RoleRepository;
import matheusresio.controle_de_gastos.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final AuthorizationService authorizationService;
	

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			BCryptPasswordEncoder passwordEncoder, AuthorizationService authorizationService) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.authorizationService = authorizationService;
	}

	public Optional<User> findByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		return user;

	}

	@Transactional
	public void save(UserRegister userRegister) throws CredentialsAlreadyUsedException {
		
		checkCredentialsAvailability(null, userRegister.username(), userRegister.email());
		
		Role role = roleRepository.findByName("ROLE_USER")
				.orElseThrow(() -> new EntityNotFoundException("Role não encontrada"));
		
		User user = new User();
		user.setUsername(userRegister.username());
		user.setEmail(userRegister.email());
		user.setPassword(passwordEncoder.encode(userRegister.password()));
		user.setRole(role);

		userRepository.save(user);
	}

	@Transactional
	public void save(UserRegisterAdm userDto) throws CredentialsAlreadyUsedException {
		checkCredentialsAvailability(null, userDto.username(), userDto.email());

		Role role = roleRepository.findByName("ROLE_" + userDto.role())
				.orElseThrow(() -> new EntityNotFoundException("Role não encontrada"));

		User user = new User();
		user.setUsername(userDto.username());
		user.setEmail(userDto.email());
		user.setPassword(passwordEncoder.encode(userDto.password()));
		user.setRole(role);
		System.out.println("user a ser salvo: " + user);
		userRepository.save(user);
	}
	

	@Transactional
	public void changePassword(UUID id, User user, PasswordChange password) throws SamePasswordException, WrongPasswordException {
		authorizationService.verifyUserAccess(user, id);
		this.verifyPassword(user, password);
		user.setPassword(passwordEncoder.encode(password.getNewPassword()));

	}
	
	private void verifyPassword(User user, PasswordChange password) throws SamePasswordException, WrongPasswordException {
		if(!passwordEncoder.matches(password.getCurrentPassword(), user.getPassword())){
			throw new WrongPasswordException("A senha atual não confere");
		}
		
		if(passwordEncoder.matches(password.getNewPassword(), user.getPassword())) {
			throw new SamePasswordException("As senhas são idênticas");
		}
	}

	@Transactional
	public void update(UUID id, User user, UserUpdateDto userDto) throws SameCredentialsException, CredentialsAlreadyUsedException{
		authorizationService.verifyUserAccess(user, id);
		checkSameCredentials(user, userDto.getUsername(), userDto.getEmail());
	    checkCredentialsAvailability(user, userDto.getUsername(), userDto.getEmail());
	    
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
	}

	private void checkSameCredentials(User user, String username, String email){
	    if(user.getUsername().equals(username) && user.getEmail().equals(email)) {
	        throw new SameCredentialsException("Não é possível atualizar com os mesmos dados");
	    }
	}

	private void checkCredentialsAvailability(User user, String username, String email) throws CredentialsAlreadyUsedException{
	    Optional<User> userByEmail = userRepository.findByEmail(email);
	    Optional<User> userByUsername = userRepository.findByUsername(username);

	    if (userByEmail.isPresent() && !userByEmail.get().equals(user)) {
	        throw new CredentialsAlreadyUsedException("O email já está sendo utilizado por outro usuário");
	    }

	    if (userByUsername.isPresent() && !userByUsername.get().equals(user)) {
	        throw new CredentialsAlreadyUsedException("O username já está sendo utilizado por outro usuário");
	    }
	}


	public User findById(UUID id) {
		return userRepository.findById(id).get();
	}
}















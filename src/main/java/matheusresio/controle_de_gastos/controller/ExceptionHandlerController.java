package matheusresio.controle_de_gastos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import matheusresio.controle_de_gastos.exceptions.CredentialsAlreadyUsedException;
import matheusresio.controle_de_gastos.exceptions.SamePasswordException;
import matheusresio.controle_de_gastos.exceptions.WrongPasswordException;

@RestControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler(CredentialsAlreadyUsedException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(CredentialsAlreadyUsedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
	
	@ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<String> handleSamePassword(SamePasswordException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
	
	@ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<String> handleWrongPassword(WrongPasswordException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}



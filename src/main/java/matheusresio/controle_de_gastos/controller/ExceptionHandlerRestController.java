package matheusresio.controle_de_gastos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import matheusresio.controle_de_gastos.exceptions.CredentialsAlreadyUsedException;
import matheusresio.controle_de_gastos.exceptions.SameCredentialsException;
import matheusresio.controle_de_gastos.exceptions.SamePasswordException;
import matheusresio.controle_de_gastos.exceptions.WrongPasswordException;

@RestControllerAdvice
public class ExceptionHandlerRestController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerRestController.class);

    @ExceptionHandler({ AccessDeniedException.class, EntityNotFoundException.class })
    public ResponseEntity<String> handleAccessDenied(Exception ex) {
        logger.error("Erro de autorização ou entidade não encontrada: ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
    
    @ExceptionHandler({ CredentialsAlreadyUsedException.class, SameCredentialsException.class })
    public ResponseEntity<String> handleCredentialsException(Exception ex) {
        logger.error("Erro nas credenciais: ", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    
    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<String> handleSamePassword(Exception ex) {
        logger.error("As senhas são iguais: ", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    
    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<String> handleWrongPassword(Exception ex) {
        logger.error("Senha incorreta: ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
    
}
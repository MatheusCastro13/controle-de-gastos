package matheusresio.controle_de_gastos.exceptions;

public class SameCredentialsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SameCredentialsException(String message) {
		super(message);
	}

}

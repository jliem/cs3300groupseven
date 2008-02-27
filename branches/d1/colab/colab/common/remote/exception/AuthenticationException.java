package colab.common.remote.exception;

import java.rmi.RemoteException;

public class AuthenticationException extends RemoteException {

	public AuthenticationException() {
		super();
	}
	
	public AuthenticationException(Throwable cause) {
		super("Authentication error", cause);
	}
}

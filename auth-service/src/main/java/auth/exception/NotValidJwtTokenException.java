package auth.exception;

public class NotValidJwtTokenException extends RuntimeException {
    public NotValidJwtTokenException(String message) {
        super(message);
    }
}

package exceptions;

public class InvalidRequestException extends Exception {

    public InvalidRequestException(String error) {
        super(error);
    }

}

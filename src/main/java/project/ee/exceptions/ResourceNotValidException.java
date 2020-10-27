package project.ee.exceptions;

public class ResourceNotValidException extends RuntimeException {

    public ResourceNotValidException() {
    }

    public ResourceNotValidException(String s) {
        super(s);
    }
}

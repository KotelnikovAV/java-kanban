package exceptions;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String massage) {
        super(massage);
    }

    public ManagerSaveException(String massage, Throwable cause) {
        super(massage, cause);
    }
}
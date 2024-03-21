package exceptions;

import java.io.IOException;

public class ManagerSaveException extends IOException {

    public ManagerSaveException(String massage) {
        super(massage);
    }
}
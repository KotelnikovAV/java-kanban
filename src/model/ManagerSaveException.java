package model;

public class ManagerSaveException extends Exception {

    private final String file;

    public ManagerSaveException(String massage, String file) {
        super(massage);
        this.file = file;
    }

    public String getDetailMessage() {
        return getMessage() + "(" + file + ")";
    }
}
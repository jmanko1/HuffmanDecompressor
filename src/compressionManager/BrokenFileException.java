package compressionmanager;

public class BrokenFileException extends Exception {
    public BrokenFileException() {
        super();
    }

    public BrokenFileException(String message) {
        super(message);
    }
}

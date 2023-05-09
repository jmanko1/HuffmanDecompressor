package compressionmanager;

public class InvalidFileExtensionException extends Exception {
    public InvalidFileExtensionException() {
        super();
    }

    public InvalidFileExtensionException(String message) {
        super(message);
    }
}

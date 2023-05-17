package compressionManager;

import java.io.IOException;

public class InvalidFileExtensionException extends IOException {
    public InvalidFileExtensionException() {
        super();
    }

    public InvalidFileExtensionException(String message) {
        super(message);
    }
}

package compressionManager;

import java.io.IOException;

public class BrokenFileException extends IOException {
    public BrokenFileException() {
        super();
    }

    public BrokenFileException(String message) {
        super(message);
    }
}

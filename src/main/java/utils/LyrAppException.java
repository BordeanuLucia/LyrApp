package utils;

/**
 * Class for my custom exception for the application
 */
public class LyrAppException extends Exception {
    public LyrAppException() {
    }

    public LyrAppException(String message) {
        super(message);
    }

    public LyrAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
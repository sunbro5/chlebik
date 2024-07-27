package cz.jan.exception;

public class ResourceDoesNotExist extends RuntimeException{

    public ResourceDoesNotExist(String message) {
        super(message);
    }
}

package ru.DTF98.TaskServiceWithAspect.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

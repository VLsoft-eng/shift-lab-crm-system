package ru.shift.task.domain.exception;

public class PeriodNotFoundException extends RuntimeException {
    public PeriodNotFoundException(String message) {
        super(message);
    }
}

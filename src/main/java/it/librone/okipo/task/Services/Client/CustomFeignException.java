package it.librone.okipo.task.Services.Client;

public class CustomFeignException extends RuntimeException {
    public CustomFeignException(String message) {
        super(message);
    }
}

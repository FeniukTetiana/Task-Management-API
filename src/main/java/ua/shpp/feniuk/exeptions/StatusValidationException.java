package ua.shpp.feniuk.exeptions;

public class StatusValidationException extends BaseLocalizedException {
    public StatusValidationException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}

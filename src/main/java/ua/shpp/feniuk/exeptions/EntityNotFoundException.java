package ua.shpp.feniuk.exeptions;

public class EntityNotFoundException extends BaseLocalizedException {
    public EntityNotFoundException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}

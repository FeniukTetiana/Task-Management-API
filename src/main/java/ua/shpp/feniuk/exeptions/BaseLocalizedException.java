package ua.shpp.feniuk.exeptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class BaseLocalizedException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;
}

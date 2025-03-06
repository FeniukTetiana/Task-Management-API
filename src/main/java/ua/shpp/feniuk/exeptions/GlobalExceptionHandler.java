package ua.shpp.feniuk.exeptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "task.notFound");
    }

    @ExceptionHandler(StatusValidationException.class)
    public ResponseEntity<String> handleStatusValidationException(StatusValidationException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "error.invalid.status.transition");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(
                "error.generic.argument",
                new Object[]{ex.getMessage()},
                "Invalid argument: " + ex.getMessage(),
                locale
        );
        return ResponseEntity.badRequest().body(message);
    }

    private ResponseEntity<String> buildErrorResponse(BaseLocalizedException ex, HttpStatus status,
                                                      String defaultMessage) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(
                ex.getMessageKey(),
                ex.getArgs(),
                defaultMessage,
                locale
        );

        log.error("{}: {}", ex.getClass().getSimpleName(), message);
        return ResponseEntity.status(status).body(message);
    }
}

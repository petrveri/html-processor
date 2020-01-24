package org.petrveri.util.htmlprocessor.exceptions;

/**
 * @author Petro Veriienko
 */
public class ApplicationException extends RuntimeException {
    private Throwable throwable;
    private String customMessage;

    public ApplicationException(Throwable throwable)
    {
        this.throwable = throwable;
    }

    public ApplicationException(String customMessage) {
        this.customMessage = customMessage;
    }

    public ApplicationException(Throwable throwable, String customMessage)
    {
        this.throwable = throwable;
        this.customMessage = customMessage;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getCustomMessage() {
        return customMessage;
    }
}

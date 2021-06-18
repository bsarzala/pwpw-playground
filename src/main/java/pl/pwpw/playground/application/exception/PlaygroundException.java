package pl.pwpw.playground.application.exception;

import lombok.Getter;

@Getter
public class PlaygroundException extends RuntimeException {

    private final int errorCode;
    private final String errorMessage;

    public PlaygroundException(final FaultBuilder faultBuilder) {
        this.errorCode = faultBuilder.getErrorCode();
        this.errorMessage = faultBuilder.getErrorMessage();
    }
}
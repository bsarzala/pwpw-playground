package pl.pwpw.playground.application.exception.controller.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.pwpw.playground.application.exception.PlaygroundException;

import java.util.Map;

@RestControllerAdvice
public class ResponseExceptionHandler extends DefaultErrorAttributes {

    private final ObjectMapper mapper = new ObjectMapper();

    @ExceptionHandler(PlaygroundException.class)
    ResponseEntity<ErrorResponse> handlePlaygroundException(final PlaygroundException exception) {
        final ErrorResponse errorRsp = new ErrorResponse(exception.getErrorCode(), exception.getErrorMessage());
        return new ResponseEntity<>(errorRsp, HttpStatus.valueOf(errorRsp.getCode()));
    }

    @Override
    public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final ErrorAttributeOptions options) {
        final Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        return mapper.convertValue(new ErrorResponse((int) errorAttributes.get("status"), (String) errorAttributes.get("error")), Map.class);
    }

    @Data
    @RequiredArgsConstructor
    static class ErrorResponse {
        private final int code;
        private final String message;
    }
}

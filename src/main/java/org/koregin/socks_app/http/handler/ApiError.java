package org.koregin.socks_app.http.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiError {
    private HttpStatus status;
    private String message;
    private Object errors;

    public ApiError(HttpStatus status, String message, Object errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
}

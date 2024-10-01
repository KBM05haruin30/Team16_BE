package org.cookieandkakao.babting.common.exception;

import org.cookieandkakao.babting.common.apiresponse.ApiResponseBody;
import org.cookieandkakao.babting.common.apiresponse.ApiResponseBody.FailureBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 모든 Exception을 처리하는 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureBody> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(
            new ApiResponseBody.FailureBody(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}

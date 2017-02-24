package pl.poznan.lolx.rest.shared

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.poznan.lolx.rest.shared.jwt.JwtExpiredException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity handleConflict() {
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}

package pl.poznan.lolx.rest.shared

import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.poznan.lolx.rest.shared.jwt.JwtExpiredException

@Slf4j
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity handleJwtExpiration() {
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleExcetpion(Exception ex) {
        log.warn("uncaught exception", ex)
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }
}

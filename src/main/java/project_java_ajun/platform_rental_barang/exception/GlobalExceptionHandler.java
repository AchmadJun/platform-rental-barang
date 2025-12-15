package project_java_ajun.platform_rental_barang.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;
import project_java_ajun.platform_rental_barang.response.WebResponse;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Validasi @NotBlank, @NotNull, @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<?>> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + " : " + err.getDefaultMessage())
                .findFirst()
                .orElse("Request tidak valid");

        return ResponseEntity.badRequest()
                .body(WebResponse.error(message));
    }


    //Username atau User tidak ditemukan
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<WebResponse<?>> handleUserNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(WebResponse.error(ex.getMessage()));
    }

    //Password Salah (BadCredentials)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<WebResponse<?>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(WebResponse.error("Password salah"));
    }

    //JWT Expired
    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<WebResponse<?>> handleExpiredJwt(io.jsonwebtoken.ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(WebResponse.error("Token sudah kadaluarsa, silahkan login ulang"));
    }

    //JWT Invalid
    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<WebResponse<?>> handleJwtException(io.jsonwebtoken.JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(WebResponse.error("Token tidak valid"));
    }

    //File Upload too Large
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<WebResponse<?>> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.error("Ukuran file maksimal 5MB"));
    }

    //Invalid File Upload (CloudinaryService)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<WebResponse<?>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.error(ex.getMessage()));
    }

    //Response Status Code
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<?>> handleResponseStatus(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(WebResponse.error(ex.getReason()));
    }

    //Role Denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<WebResponse<?>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(WebResponse.error(ex.getMessage()));
    }

    //Error umum lainnya
    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<?>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.error("Terjadi kesalahan pada server: " + ex.getMessage()));
    }


}

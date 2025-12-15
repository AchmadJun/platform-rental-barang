package project_java_ajun.platform_rental_barang.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_java_ajun.platform_rental_barang.request.AuthRequest;
import project_java_ajun.platform_rental_barang.response.AuthResponse;
import project_java_ajun.platform_rental_barang.response.WebResponse;
import project_java_ajun.platform_rental_barang.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<WebResponse<AuthResponse>> login(
            @Valid @RequestBody AuthRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(WebResponse.success("Login berhasil", response)
        );
    }
}

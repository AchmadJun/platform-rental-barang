package project_java_ajun.platform_rental_barang.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import project_java_ajun.platform_rental_barang.entity.Admin;
import project_java_ajun.platform_rental_barang.entity.User;
import project_java_ajun.platform_rental_barang.repository.AdminRepository;
import project_java_ajun.platform_rental_barang.repository.UserRepository;
import project_java_ajun.platform_rental_barang.request.AuthRequest;
import project_java_ajun.platform_rental_barang.response.AuthResponse;
import project_java_ajun.platform_rental_barang.security.JwtService;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;

    public AuthResponse login(AuthRequest request) {

        // Validasi manual (fallback jika @Valid di controller tidak aktif)
        if (request.getIdentifier() == null || request.getIdentifier().isBlank()) {
            throw new IllegalArgumentException("Identifier tidak boleh kosong");
        }

        boolean isEmail = request.getIdentifier().contains("@");

        Object loggedUser;
        String role;
        String id;

        if (isEmail) {
            User user = userRepository.findByEmail(request.getIdentifier())
                    .orElseThrow(() -> new RuntimeException("Email tidak ditemukan"));

            authenticate(request);

            loggedUser = user;
            id = user.getId();
            role = user.getRole().name();

        } else {
            // Login ADMIN
            Admin admin = adminRepository.findByUsername(request.getIdentifier())
                    .orElseThrow(() -> new RuntimeException("Username tidak ditemukan"));

            authenticate(request);

            loggedUser = admin;
            id = admin.getId();
            role = admin.getRole().name();
        }

        // Generate token
        String token = jwtService.generateToken(id, role);

        return AuthResponse.builder()
                .token(token)
                .data(loggedUser)
                .build();
    }

    private void authenticate(AuthRequest request) {
        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getIdentifier(),
                        request.getPassword()
                )
        );
    }
}



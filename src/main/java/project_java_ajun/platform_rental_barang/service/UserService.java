package project_java_ajun.platform_rental_barang.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project_java_ajun.platform_rental_barang.entity.User;
import project_java_ajun.platform_rental_barang.repository.UserRepository;
import project_java_ajun.platform_rental_barang.request.UserRequest;
import project_java_ajun.platform_rental_barang.request.UserUpdateRequest;
import project_java_ajun.platform_rental_barang.response.UserResponse;
import project_java_ajun.platform_rental_barang.security.CustomUserDetails;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeoapifyService geoapifyService;

    //Method untuk Geoapify + fallback
    private String resolveGoogleMapsUrl(String alamat) {
        String safeAlamat = alamat != null ? alamat : "Indonesia";
        try {
            Map<String, Object> mapResult = geoapifyService.getCoordinates(safeAlamat);
            return mapResult.get("googleMapsUrl").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "https://www.google.com/maps/search/?api=1&query=" +
                    URLEncoder.encode(safeAlamat, StandardCharsets.UTF_8);
        }
    }

    //Create user
    public UserResponse registerUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email sudah terdaftar");
        }

        // Panggil Geoapify API untuk dapatkan URL Google Maps
        String googleMapsUrl = resolveGoogleMapsUrl(request.getAlamat());

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .nama(request.getNama())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .noTelp(request.getNoTelp())
                .alamat(request.getAlamat())
                .role(request.getRole())
                .mapUrl(googleMapsUrl)
                .build();

        userRepository.save(user);
        return toResponse(user);
    }

    //Get User by id
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan"));
        return toResponse(user);
    }

    //Get All Users
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    //Update User
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        //Ambil user yang Login
        CustomUserDetails userDetails = getCurrentUserDetails();

        // Validasi hanya user sendiri yang boleh update
        if (!userDetails.getId().equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Anda tidak memiliki akses untuk mengupdate data user ini");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan"));

        if (request.getNama() != null) user.setNama(request.getNama());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getAlamat() != null) {
            user.setAlamat(request.getAlamat());
            user.setMapUrl(resolveGoogleMapsUrl(request.getAlamat()));
        }
        if (request.getNoTelp() != null) user.setNoTelp(request.getNoTelp());
        if (request.getRole() != null) user.setRole(request.getRole());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
        return toResponse(user);
    }

    //Delete User
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        userRepository.delete(user);
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNama(user.getNama());
        response.setEmail(user.getEmail());
        response.setAlamat(user.getAlamat());
        response.setNoTelp(user.getNoTelp());
        response.setMapUrl(user.getMapUrl());
        response.setRole(user.getRole());
        return response;
    }

    private CustomUserDetails getCurrentUserDetails() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}

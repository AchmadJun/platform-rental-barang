package project_java_ajun.platform_rental_barang.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project_java_ajun.platform_rental_barang.entity.Admin;
import project_java_ajun.platform_rental_barang.repository.AdminRepository;
import project_java_ajun.platform_rental_barang.request.AdminRequest;
import project_java_ajun.platform_rental_barang.response.AdminResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    //Create Admin
    public AdminResponse registerAdmin(AdminRequest request){
        if (adminRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username sudah terdaftar");
        }

        Admin admin = Admin.builder()
                .id(UUID.randomUUID().toString())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nama(request.getNama())
                .role(request.getRole())
                .build();

        adminRepository.save(admin);
        return toResponse(admin);
    }

    //Get Admin By Id
    public AdminResponse getUserById(String id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin tidak ditemukan"));
        return toResponse(admin);
    }

    //Update Admin
    public AdminResponse updateAdmin(String id, AdminRequest request) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin tidak ditemukan"));

        admin.setUsername(request.getNama());
        admin.setNama(request.getNama());
        admin.setRole(request.getRole());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        adminRepository.save(admin);
        return toResponse(admin);
    }

    //Delete Admin
    public void deleteUser(String id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        adminRepository.delete(admin);
    }

    private AdminResponse toResponse(Admin admin) {
        AdminResponse response = new AdminResponse();
        response.setId(admin.getId());
        response.setUsername(admin.getUsername());
        response.setNama(admin.getNama());
        response.setRole(admin.getRole());
        return response;
    }
}

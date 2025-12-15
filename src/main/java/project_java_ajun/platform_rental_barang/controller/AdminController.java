package project_java_ajun.platform_rental_barang.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project_java_ajun.platform_rental_barang.request.AdminRequest;
import project_java_ajun.platform_rental_barang.response.AdminResponse;
import project_java_ajun.platform_rental_barang.response.WebResponse;
import project_java_ajun.platform_rental_barang.service.AdminService;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //Create
    @PostMapping("/register")
    public ResponseEntity<WebResponse<AdminResponse>> register(@Valid @RequestBody AdminRequest request) {
        AdminResponse response = adminService.registerAdmin(request);
        return ResponseEntity.ok(WebResponse.success("Admin registered successfully", response));
    }

    // Get User By ID (ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WebResponse<AdminResponse>> getAdminById(@PathVariable String id) {
        AdminResponse response = adminService.getUserById(id);
        return ResponseEntity.ok(WebResponse.success(response));
    }

    // Update Admin (ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WebResponse<AdminResponse>> updateAdmin(
            @PathVariable String id,
            @Valid @RequestBody AdminRequest request
    ) {
        AdminResponse response = adminService.updateAdmin(id, request);
        return ResponseEntity.ok(WebResponse.success("Admin updated successfully", response));
    }

    // Delete Admin (ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WebResponse<String>> deleteUser(@PathVariable String id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(WebResponse.success("Admin deleted successfully", null));
    }
}

package project_java_ajun.platform_rental_barang.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project_java_ajun.platform_rental_barang.request.UserRequest;
import project_java_ajun.platform_rental_barang.request.UserUpdateRequest;
import project_java_ajun.platform_rental_barang.response.UserResponse;
import project_java_ajun.platform_rental_barang.response.WebResponse;
import project_java_ajun.platform_rental_barang.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create
    @PostMapping("/register")
    public ResponseEntity<WebResponse<UserResponse>> register(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(WebResponse.success("User registered successfully", response));
    }

    // Get User By ID (RENTER,OWNER,ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RENTER','ADMIN','OWNER')")
    public ResponseEntity<WebResponse<UserResponse>> getUserById(@PathVariable String id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(WebResponse.success(response));
    }

    // Get All User
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WebResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(
                WebResponse.success("Success Retrieving data", users)
        );
    }

    // Update User (RENTER,OWNER)
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('RENTER','OWNER')")
    public ResponseEntity<WebResponse<UserResponse>> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(WebResponse.success("User updated successfully", response));
    }

    // Delete User (hanya ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WebResponse<String>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(WebResponse.success("User deleted successfully", null));
    }
}



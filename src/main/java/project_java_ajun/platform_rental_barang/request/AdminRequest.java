package project_java_ajun.platform_rental_barang.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project_java_ajun.platform_rental_barang.entity.Role;

@Data
public class AdminRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String nama;

    @NotNull
    private Role role;
}

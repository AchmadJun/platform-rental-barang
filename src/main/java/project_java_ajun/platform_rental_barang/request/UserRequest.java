package project_java_ajun.platform_rental_barang.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project_java_ajun.platform_rental_barang.entity.Role;

@Data
public class UserRequest {

    @NotBlank
    private String nama;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String alamat;

    @NotBlank
    private String noTelp;

    @NotNull
    private Role role;
}

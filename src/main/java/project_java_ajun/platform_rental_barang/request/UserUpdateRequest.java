package project_java_ajun.platform_rental_barang.request;

import lombok.Data;
import project_java_ajun.platform_rental_barang.entity.Role;

@Data
public class UserUpdateRequest {

    private String nama;

    private String password;

    private String email;

    private String alamat;

    private String noTelp;

    private Role role;
}

package project_java_ajun.platform_rental_barang.response;

import lombok.Data;
import project_java_ajun.platform_rental_barang.entity.Role;

@Data
public class UserResponse {

    private String id;

    private String nama;

    private String email;

    private String alamat;

    private String noTelp;

    private Role role;

    private String mapUrl;
}

package project_java_ajun.platform_rental_barang.response;

import lombok.Data;
import project_java_ajun.platform_rental_barang.entity.Role;

@Data
public class AdminResponse {

    private String id;

    private String username;

    private String nama;

    private Role role;
    
}

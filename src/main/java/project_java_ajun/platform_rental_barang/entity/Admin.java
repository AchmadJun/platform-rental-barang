package project_java_ajun.platform_rental_barang.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "admins")
public class Admin {

    @Id
    private String id;

    private String username;

    @JsonIgnore
    private String password;

    private String nama;

    @Enumerated(EnumType.STRING)
    private Role role;
}

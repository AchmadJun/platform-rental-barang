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
@Table (name = "users")
public class User {

    @Id
    private String id;

    private String nama;

    @JsonIgnore
    private String password;

    private String email;

    private String alamat;

    @Column(name = "no_telp")
    private String noTelp;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "urlmaps")
    private String mapUrl;
}

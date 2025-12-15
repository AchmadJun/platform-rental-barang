package project_java_ajun.platform_rental_barang.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotBlank
    private String identifier;

    @NotBlank
    private String password;
}

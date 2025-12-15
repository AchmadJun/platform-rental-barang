package project_java_ajun.platform_rental_barang.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenterShortResponse {

    private String id;

    private String nama;

    private String email;

    private String noTelp;
}

package project_java_ajun.platform_rental_barang.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnerShortResponse {

    private String id;

    private String nama;

    private String noTelp;
}

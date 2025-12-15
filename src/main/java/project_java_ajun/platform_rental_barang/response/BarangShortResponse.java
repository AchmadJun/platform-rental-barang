package project_java_ajun.platform_rental_barang.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BarangShortResponse {

    private String id;

    private String nama;

    private Long hargaPerHari;

    private String gambarBarang;
}

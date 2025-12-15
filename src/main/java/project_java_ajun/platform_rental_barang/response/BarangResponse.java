package project_java_ajun.platform_rental_barang.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_java_ajun.platform_rental_barang.entity.KondisiBarang;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BarangResponse {

    private String id;

    private OwnerShortResponse owner;

    private String nama;

    private KondisiBarang kondisi;

    private String deskripsi;

    private Long hargaPerHari;

    private String gambarBarang;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}

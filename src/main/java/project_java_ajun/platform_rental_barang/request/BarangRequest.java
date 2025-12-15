package project_java_ajun.platform_rental_barang.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import project_java_ajun.platform_rental_barang.entity.KondisiBarang;

@Data
public class BarangRequest {

    @NotBlank
    private String nama;

    @NotNull
    private KondisiBarang kondisi;

    @NotBlank
    private String deskripsi;

    @NotNull
    @Positive
    private Long hargaPerHari;

}

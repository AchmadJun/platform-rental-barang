package project_java_ajun.platform_rental_barang.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_java_ajun.platform_rental_barang.entity.StatusPemesanan;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PemesananResponse {

    private String id;

    private RenterShortResponse renter;

    private BarangShortResponse barang;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate tanggalRental;

    private Integer durasiHari;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate tanggalKembali; // dihitung otomatis

    private StatusPemesanan statusPemesanan;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}

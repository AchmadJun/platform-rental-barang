package project_java_ajun.platform_rental_barang.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_java_ajun.platform_rental_barang.entity.StatusPemesanan;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PemesananShortResponse {

    private String id;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate tanggalRental;

    private Integer durasiHari;

    private StatusPemesanan statusPemesanan;
}

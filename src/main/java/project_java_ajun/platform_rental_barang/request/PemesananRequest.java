package project_java_ajun.platform_rental_barang.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PemesananRequest {

    @NotBlank
    private String barangId;

    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate tanggalRental;

    @NotNull
    private Integer durasiHari;

}

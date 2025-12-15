package project_java_ajun.platform_rental_barang.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PembayaranRequest {

    @NotBlank
    private String pemesananId;

    @NotBlank
    private String metodeBayar;
}

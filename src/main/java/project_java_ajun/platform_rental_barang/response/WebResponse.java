package project_java_ajun.platform_rental_barang.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {

    private String status;        // "success" / "error"
    private String message;       // pesan singkat
    private T data;               // isi data (generic)

    public static <T> WebResponse<T> success(String message, T data) {
        return WebResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .build();
    }

    public static <T> WebResponse<T> success(T data) {
        return WebResponse.<T>builder()
                .status("success")
                .data(data)
                .build();
    }

    public static <T> WebResponse<T> error(String message) {
        return WebResponse.<T>builder()
                .status("error")
                .message(message)
                .build();
    }
}

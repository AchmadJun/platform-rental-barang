package project_java_ajun.platform_rental_barang.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse<T> {

    private List<T> data;
    private Paging paging;

    public PagingResponse(List<T> data, int currentPage, int totalPages, int size) {
        this.data = data;
        this.paging = new Paging(currentPage, totalPages, size);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Paging {
        private int currentPage;
        private int totalPages;
        private int size;
    }
}

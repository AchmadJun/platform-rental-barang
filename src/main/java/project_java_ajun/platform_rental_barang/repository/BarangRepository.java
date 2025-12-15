package project_java_ajun.platform_rental_barang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_java_ajun.platform_rental_barang.entity.Barang;
import project_java_ajun.platform_rental_barang.entity.KondisiBarang;

@Repository
public interface BarangRepository extends JpaRepository<Barang, String> {

    //Search By nama barang atau Deskripsi Barang
    Page<Barang> findByNamaContainingIgnoreCaseOrDeskripsiContainingIgnoreCase(String nama, String deskripsi, Pageable pageable);

    //Search By Rentang Harga
    Page<Barang> findByHargaPerHariBetween(Double minHarga, Double maxHarga, Pageable pageable);

    //Search By Status Barang
    Page<Barang> findByKondisi(KondisiBarang kondisi, Pageable pageable);
}

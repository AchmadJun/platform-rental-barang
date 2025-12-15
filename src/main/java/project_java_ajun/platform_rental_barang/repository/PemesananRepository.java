package project_java_ajun.platform_rental_barang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_java_ajun.platform_rental_barang.entity.Pemesanan;
import project_java_ajun.platform_rental_barang.entity.StatusPemesanan;

@Repository
public interface PemesananRepository extends JpaRepository<Pemesanan, String> {

    boolean existsByBarangIdAndStatusPemesanan(String barangId, StatusPemesanan statusPemesanan);
}

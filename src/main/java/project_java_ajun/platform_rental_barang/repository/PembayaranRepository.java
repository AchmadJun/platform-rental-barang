package project_java_ajun.platform_rental_barang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_java_ajun.platform_rental_barang.entity.Pembayaran;

import java.util.List;
import java.util.Optional;

@Repository
public interface PembayaranRepository extends JpaRepository<Pembayaran, String> {

    Optional<Pembayaran> findByPesanan_Id(String pemesananId);

    Optional<Pembayaran> findByOrderId(String orderId);
}

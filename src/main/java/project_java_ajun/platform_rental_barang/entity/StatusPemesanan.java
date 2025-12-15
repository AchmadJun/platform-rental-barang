package project_java_ajun.platform_rental_barang.entity;

public enum StatusPemesanan {
    PENDING, // renter baru buat pesanan
    APPROVED, // owner setuju, tetapi belum dibayar
    RENTED, // sedang dipinjam, sudah dibayar
    RETURNED, // rental barang selesai
    CANCELED // dibatalkan sebelum meminjam
}

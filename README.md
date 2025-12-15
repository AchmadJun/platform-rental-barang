# Platform Rental Barang API - Spring Boot

Project ini merupakan project Portofolio berupa aplikasi Rental Barang berbasis Spring Boot yang memiliki fitur utama untuk <br>
memesan barang antar pengguna owner si pemilik barang dan renter yang ingin meminjam barang, lalu fitur <br>
lainnya yaitu dapat melakukan pembayaran lewat payment gateway(gerbang pembayaran) midtrans antara penjual <br>
dan pembeli secara aman dan praktis juga menggunakan media cloud based gambar maupun video, pada project ini author<br>
menggunakan cloudinary sebagai media upload gambar untuk gambar barang



## Fitur 

Berikut fitur yang ada pada project aplikasi ini diantaranya,

- Authentication(Verifikasi Identitas) & Authorization (Hak Akses)
  - Register dan Login User
  - Role Based Access Control (ROLE_ADMIN,ROLE_OWNER,ROLE_RENTER)
  - JWT Authentication dengan Spring Security


- User Management
  - CRUD User
  - Geoapify untuk alamat dan lokasi barang


- Barang Management
  - CRUD Barang
  - Upload Barang (Cloudinary)
  - Filtering & Pagination (RENTER dapat menggunakan Filtering)


- Pemesanan Barang
  - Create Booking/Pemesanan
  - Validasi Owner & Renter
  - Status Pemesanan Flow :  Accept Flow **PENDING** -> **APPROVED** -> **RENTED** -> **RETURNED** <br>
                   Cancelled Flow(Renter) **PENDING** -> **CANCELLED**


- Pembayaran Barang (Midtrans Snap)
  - Membuat Transaksi midtrans snap
  - Tanggal Kembali autogenerate dari perhitungan **tanggal_rental + durasi_hari**
  - WebHook Callback, sebagai alat bantu simulasi uji coba pembayaran secara otomatis menggunakan switch case<br>
    diantaranya **settlement**, **pending**, **cancel**, **deny**
  - Transaction Status autoupdate saat menggunakan webhook, berikut beberapa contohnya
    - **settlement** -> Transaction Status = Succes, Update Status Pemesanan = RENTED
    - **pending** -> Transaction Status = Pending
    - **cancel** -> Transaction Status = Cancel, Update Status Pemesanan = CANCELLED
    - **deny** -> Transaction Status = Failed, Update Status Pemesanan = CANCELLED


## Tech Stack


- Versi Java 21
- Spring Boot 3+
- Spring Data JPA (Hibernate)
- Spring Security + JWT
- Cloudinary
- Geoapify
- Midtrans Snap
- MySql


## Security


- Password hashing pada user menggunakan Bcrypt
- Setiap endpoint terproteksi dengan JWT(Jason Web Token)
- Validasi kepemilikan data (Owner,Renter)

## Database


- User (berdasarkan role OWNER dan RENTER)
- Barang (Identitas OWNER) relasi OnetoMany terhadap User
- Pemesanan
- Pembayaran relasi OnetoOne terhadap Pemesanan


<img width="856" height="571" alt="database relasi" src="https://github.com/user-attachments/assets/6d65ccee-599a-4f51-b785-2cbebabe3a4a" />


## Cara menjalankan aplikasi


```shell
#Build
mvn build package
```

```shell
#Run
mvn spring-boot:run
```


## Application Configuration


Project ini menggunakan file application.properties.example sebagai contoh konfigurasi.
Sebelum menjalankan aplikasi, lakukan,

1. Salin file example
2. Ubah nama menjadi application.properties
3. Sesuaikan nilai konfigurasi dengan mencopy file example ke properties masing-masing

```shell
cp src/main/resources/application.properties.example \ src/main/resources/application.properties
```

## API Documentation


Project ini tidak menggunakan Swagger/OpenAPI melainkan menggunakan Postman Collection untuk <br>
pengujian dan dokumentasi endpoint

- Postman Collection
  - Authentication
  - User
  - Admin
  - Barang
  - Pemesanan
  - Pembayaran


Berikut link Postman API Documentation

https://documenter.getpostman.com/view/43812367/2sB3dTsTK8


## Future Improvement

- Profile Picture Cloudinary untuk Untuk User
- Notification Integration(WhatsApp/Email)
- Refund Midtrans
- Refresh Token JWT
- Riwayat Pemesanan dan Pembayaran


# Author


**Nama** : Achmad Junaedi <br>
**Jurusan/Universitas** : Teknik Informatika,Universitas Indraprasta PGRI <br>
**Tujuan Project** : Portofolio <br>

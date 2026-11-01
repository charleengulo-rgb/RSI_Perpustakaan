# Latihan Implementasi RSI - Perpustakaan BCE

Implementasi mandiri berdasarkan latihan halaman 57 PDF **10a. Implementasi RSI Oke (3)**. Proyek ini tidak mengubah proyek ordering-system sebelumnya. Kelola Stok Buku dan Ajukan Reservasi menjadi fitur utama; Pinjam Buku disertakan agar alur extend bisa digunakan dari awal.

## Mulai dari sini (Windows / VS Code)

1. Ekstrak ZIP. Buka folder `rsi-perpustakaan` yang berisi `pom.xml` melalui File > Open Folder.
2. Pastikan JDK 17 atau 21 dan Maven 3.6.3+ terpasang. Di terminal baru, periksa `java -version` dan `mvn -version`. Jika `mvn` tidak dikenali, pasang Apache Maven dan tambahkan folder `bin`-nya ke PATH, lalu buka terminal baru.
3. Jalankan `mvn clean test` untuk pengujian. Koneksi internet diperlukan pada build pertama.
4. Jalankan `mvn spring-boot:run`. Tunggu log `Started PerpustakaanApplication`.
5. Buka http://localhost:8080. Login menggunakan akun demo di bawah.
6. Hentikan dengan Ctrl+C. Data H2 ada di memori: seluruh perubahan hilang saat aplikasi dihentikan, dan data awal dibuat ulang saat dijalankan.

Jika port 8080 dipakai proyek lain: `mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"`, lalu buka http://localhost:8081.

## Akun demo

| Peran | Username | Password | Halaman |
|---|---|---|---|
| Anggota (batas 2 pinjaman) | A001 | anggota123 | /pinjam |
| Anggota (batas 1 pinjaman) | A002 | anggota123 | /pinjam |
| Pustakawan | pustakawan | pustaka123 | /stok |

Akun di `config/SecurityConfig.java` hanya untuk demonstrasi lokal. Identitas anggota diperoleh dari login, bukan dari input form. Password dikodekan BCrypt, CSRF aktif, dan `/stok` dibatasi untuk pustakawan. Aplikasi mengikat localhost.

## Demo berurutan

1. Login A001. Pinjam B001 -> bukti DIPINJAM, stok 3 menjadi 2.
2. Pinjam B002 -> pesan stok habis dan tautan **Ajukan Reservasi**.
3. Klik tautan -> klik **Konfirmasi reservasi** -> bukti MENUNGGU; stok tetap 0.
4. Ulangi reservasi B002 -> ditolak karena duplikat.
5. Klik Keluar. Login pustakawan. Ubah stok B002 menjadi 4 -> tersimpan.
6. Keluar lalu login A001 -> B002 sekarang dapat dipinjam. Reservasi lama tetap MENUNGGU; pemenuhan antrean tidak termasuk latihan ini.
7. Untuk mengulang dari data awal, hentikan dan jalankan ulang aplikasi.

Buka `docs/Laporan_RSI.pdf` untuk penjelasan, diagram, pemetaan nomor, dan skenario uji. `docs/sequence-*.svg` dapat dibuka langsung di browser; versi `.mmd` dapat diedit pada editor Mermaid.

## Struktur

- `boundary/`: controller web, penerima input, pemilihan tampilan.
- `control/`: satu service per use case, transaksi, koordinasi aturan.
- `entity/`: entity domain beserta repository persistensi.
- `config/`: autentikasi dan pembatasan peran.
- `resources/templates/`: stok.html, pinjam.html, reservasi.html, input-error.html.
- `resources/schema.sql`: empat tabel, foreign key, check constraint, unique constraint.
- `resources/data.sql`: tiga buku dan dua anggota.
- `src/test/`: unit test entity, integration test database, dan pengujian web.

## Aturan dan asumsi

- Kelola stok berarti mengganti **stok tersedia**, bukan menambahkan jumlah dan bukan menghitung total koleksi. Bilangan bulat 0 sampai 2147483647 diterima. Nilai negatif ditolak pada entity dan database.
- Reservasi hanya untuk buku yang stoknya 0; tidak mengurangi stok dan tidak dihitung sebagai pinjaman aktif.
- Satu anggota hanya dapat membuat satu reservasi untuk satu buku dalam lingkup latihan ini. Pembatalan/pemenuhan reservasi dan pengembalian buku tidak diimplementasikan. Bila fitur tersebut ditambahkan, aturan duplikat harus diperluas ke reservasi aktif saja.
- Kondisi stok dan duplikat diperiksa kembali saat POST. Halaman konfirmasi tidak dianggap sebagai jaminan stok tetap sama.
- Lock database dipakai saat memodifikasi data. Pinjam dan reservasi selalu mengunci anggota dahulu, lalu buku. StokService mengunci buku saja. Belum dilakukan uji beban paralel.
- `@Transactional` menjaga simpan pinjaman dan pengurangan stok sebagai satu transaksi. `ddl-auto=validate` memeriksa kesesuaian entity dengan schema.sql saat startup.
- Nomor pesan S untuk stok, R untuk reservasi, P untuk peminjaman tercantum di diagram dan komentar kode. Diagram mengelompokkan entity/repository pada satu lifeline agar terbaca; nama method menunjukkan objek sebenarnya.

## Build dan Git

`mvn clean package` menghasilkan `target/rsi-perpustakaan-1.0.0.jar`; jalankan dengan `java -jar target/rsi-perpustakaan-1.0.0.jar`.

Untuk menyimpan di repository milikmu, jalankan dari folder proyek:

```bash
git init
git add .
git commit -m "Implementasi BCE stok buku dan reservasi"
```

Kemudian hubungkan ke repository GitHub yang kamu pilih. Paket ini belum dipush ke akun GitHub mana pun.

## Referensi

- Dr. Achmad Arwan, materi Implementasi Software, versi 1.0, 25 Oktober 2026, terutama halaman 57.
- https://spring.io/guides/gs/spring-boot
- https://docs.spring.io/spring-data/jpa/reference/jpa/locking.html
- https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html

Versi dependency dipatok pada pom.xml (Spring Boot 3.5.7, Java 17) agar build dapat direproduksi; bukan klaim versi terbaru.

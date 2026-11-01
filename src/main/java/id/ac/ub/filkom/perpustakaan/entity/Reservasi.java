package id.ac.ub.filkom.perpustakaan.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
    name = "reservasi",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_reservasi_anggota_buku",
            columnNames = {"id_anggota", "id_buku"}))
public class Reservasi {
  public static final String MENUNGGU = "MENUNGGU";

  @Id
  @Column(name = "id_reservasi", length = 36)
  private String idReservasi;

  @ManyToOne(optional = false)
  @JoinColumn(name = "id_anggota")
  private Anggota anggota;

  @ManyToOne(optional = false)
  @JoinColumn(name = "id_buku")
  private Buku buku;

  @Column(name = "tanggal_reservasi", nullable = false)
  private LocalDate tanggalReservasi;

  @Column(nullable = false, length = 20)
  private String status;

  protected Reservasi() {}

  /** R8: reservasi hanya dibuat untuk buku yang stoknya habis. */
  public static Reservasi buat(Anggota a, Buku b) {
    if (a == null || b == null) throw new IllegalArgumentException("Anggota dan buku wajib diisi");
    if (b.cekStok())
      throw new IllegalArgumentException("Buku masih tersedia, silakan lakukan peminjaman");
    Reservasi r = new Reservasi();
    r.idReservasi = UUID.randomUUID().toString();
    r.anggota = a;
    r.buku = b;
    r.tanggalReservasi = LocalDate.now();
    r.status = MENUNGGU;
    return r;
  }

  public String getIdReservasi() {
    return idReservasi;
  }

  public Anggota getAnggota() {
    return anggota;
  }

  public Buku getBuku() {
    return buku;
  }

  public LocalDate getTanggalReservasi() {
    return tanggalReservasi;
  }

  public String getStatus() {
    return status;
  }
}

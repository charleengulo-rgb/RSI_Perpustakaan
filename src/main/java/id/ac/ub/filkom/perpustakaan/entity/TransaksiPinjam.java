package id.ac.ub.filkom.perpustakaan.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transaksi_pinjam")
public class TransaksiPinjam {
  public static final String DIPINJAM = "DIPINJAM";

  @Id
  @Column(name = "id_transaksi", length = 36)
  private String idTransaksi;

  @ManyToOne(optional = false)
  @JoinColumn(name = "id_anggota")
  private Anggota anggota;

  @ManyToOne(optional = false)
  @JoinColumn(name = "id_buku")
  private Buku buku;

  @Column(name = "tanggal_pinjam", nullable = false)
  private LocalDate tanggalPinjam;

  @Column(nullable = false, length = 20)
  private String status;

  protected TransaksiPinjam() {}

  public static TransaksiPinjam buat(Anggota a, Buku b) {
    if (a == null || b == null) throw new IllegalArgumentException("Anggota dan buku wajib diisi");
    TransaksiPinjam t = new TransaksiPinjam();
    t.idTransaksi = UUID.randomUUID().toString();
    t.anggota = a;
    t.buku = b;
    t.tanggalPinjam = LocalDate.now();
    t.status = DIPINJAM;
    a.catatPinjaman(t);
    return t;
  }

  public boolean isDipinjam() {
    return DIPINJAM.equals(status);
  }

  public String getIdTransaksi() {
    return idTransaksi;
  }

  public Anggota getAnggota() {
    return anggota;
  }

  public Buku getBuku() {
    return buku;
  }

  public LocalDate getTanggalPinjam() {
    return tanggalPinjam;
  }

  public String getStatus() {
    return status;
  }
}

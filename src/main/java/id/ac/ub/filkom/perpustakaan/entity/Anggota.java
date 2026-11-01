package id.ac.ub.filkom.perpustakaan.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "anggota")
public class Anggota {
  @Id
  @Column(name = "id_anggota", length = 10)
  private String idAnggota;

  @Column(nullable = false, length = 100)
  private String nama;

  @Column(name = "batas_pinjam", nullable = false)
  private int batasPinjam;

  @OneToMany(mappedBy = "anggota")
  private java.util.List<TransaksiPinjam> daftarPinjam = new ArrayList<>();

  protected Anggota() {}

  public Anggota(String id, String nama, int batas) {
    if (id == null || id.isBlank() || nama == null || nama.isBlank() || batas < 1)
      throw new IllegalArgumentException("Data anggota tidak valid");
    this.idAnggota = id;
    this.nama = nama;
    this.batasPinjam = batas;
  }

  public boolean cekBatasPinjam() {
    return daftarPinjam.stream().filter(TransaksiPinjam::isDipinjam).count() < batasPinjam;
  }

  void catatPinjaman(TransaksiPinjam t) {
    daftarPinjam.add(t);
  }

  public String getIdAnggota() {
    return idAnggota;
  }

  public String getNama() {
    return nama;
  }
}

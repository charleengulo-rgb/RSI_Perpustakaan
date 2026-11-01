package id.ac.ub.filkom.perpustakaan.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "buku")
public class Buku {
  @Id
  @Column(name = "id_buku", length = 10)
  private String idBuku;

  @Column(nullable = false, length = 200)
  private String judul;

  @Column(nullable = false)
  private int stok;

  protected Buku() {}

  public Buku(String id, String judul, int stok) {
    if (id == null || id.isBlank() || judul == null || judul.isBlank())
      throw new IllegalArgumentException("Identitas buku wajib diisi");
    this.idBuku = id;
    this.judul = judul;
    ubahStok(stok);
  }

  public boolean cekStok() {
    return stok > 0;
  }

  public void kurangiStok() {
    if (!cekStok()) throw new IllegalStateException("Stok buku habis");
    stok--;
  }

  /** S5-S6: stok adalah jumlah eksemplar tersedia, tidak termasuk yang dipinjam. */
  public void ubahStok(int stokBaru) {
    if (stokBaru < 0) throw new IllegalArgumentException("Stok tidak boleh negatif");
    stok = stokBaru;
  }

  public String getIdBuku() {
    return idBuku;
  }

  public String getJudul() {
    return judul;
  }

  public int getStok() {
    return stok;
  }
}

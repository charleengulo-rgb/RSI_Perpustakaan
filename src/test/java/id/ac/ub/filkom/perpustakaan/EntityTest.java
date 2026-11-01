package id.ac.ub.filkom.perpustakaan;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.ub.filkom.perpustakaan.entity.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class EntityTest {
  @Test
  void stokDapatDiubah() {
    Buku b = new Buku("B1", "Java", 3);
    b.ubahStok(8);
    assertEquals(8, b.getStok());
    assertEquals("B1", b.getIdBuku());
    assertEquals("Java", b.getJudul());
  }

  @Test
  void stokNolValid() {
    Buku b = new Buku("B1", "Java", 3);
    b.ubahStok(0);
    assertFalse(b.cekStok());
  }

  @Test
  void stokNegatifTidakMengubahNilai() {
    Buku b = new Buku("B1", "Java", 3);
    assertThrows(IllegalArgumentException.class, () -> b.ubahStok(-1));
    assertEquals(3, b.getStok());
  }

  @Test
  void konstruktorMenolakStokNegatif() {
    assertThrows(IllegalArgumentException.class, () -> new Buku("B1", "Java", -1));
  }

  @Test
  void bukuWajibMemilikiIdentitas() {
    assertThrows(IllegalArgumentException.class, () -> new Buku(" ", "Java", 0));
    assertThrows(IllegalArgumentException.class, () -> new Buku("B1", null, 0));
  }

  @Test
  void kurangiStokTidakBolehNegatif() {
    Buku b = new Buku("B1", "Java", 1);
    b.kurangiStok();
    assertEquals(0, b.getStok());
    assertThrows(IllegalStateException.class, b::kurangiStok);
  }

  @Test
  void batasPinjamMenghitungTransaksi() {
    Anggota a = new Anggota("A1", "Budi", 1);
    assertTrue(a.cekBatasPinjam());
    TransaksiPinjam t = TransaksiPinjam.buat(a, new Buku("B1", "Java", 1));
    assertFalse(a.cekBatasPinjam());
    assertTrue(t.isDipinjam());
    assertEquals(a, t.getAnggota());
    assertEquals(LocalDate.now(), t.getTanggalPinjam());
    assertNotNull(t.getIdTransaksi());
    assertEquals("B1", t.getBuku().getIdBuku());
  }

  @Test
  void anggotaTidakValidDitolak() {
    assertThrows(IllegalArgumentException.class, () -> new Anggota("A1", "Budi", 0));
    assertThrows(IllegalArgumentException.class, () -> new Anggota(null, "Budi", 2));
  }

  @Test
  void pinjamTanpaDataDitolak() {
    assertThrows(
        IllegalArgumentException.class,
        () -> TransaksiPinjam.buat(null, new Buku("B1", "Java", 1)));
  }

  @Test
  void reservasiBerisiDataBenarDanTidakMengubahStok() {
    Anggota a = new Anggota("A1", "Budi", 2);
    Buku b = new Buku("B1", "Java", 0);
    Reservasi r = Reservasi.buat(a, b);
    assertNotNull(r.getIdReservasi());
    assertSame(a, r.getAnggota());
    assertSame(b, r.getBuku());
    assertEquals(LocalDate.now(), r.getTanggalReservasi());
    assertEquals(Reservasi.MENUNGGU, r.getStatus());
    assertEquals(0, b.getStok());
    assertTrue(a.cekBatasPinjam());
  }

  @Test
  void reservasiBukuTersediaDitolak() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Reservasi.buat(new Anggota("A1", "Budi", 2), new Buku("B1", "Java", 1)));
  }

  @Test
  void reservasiTanpaAnggotaDitolak() {
    assertThrows(
        IllegalArgumentException.class, () -> Reservasi.buat(null, new Buku("B1", "Java", 0)));
  }

  @Test
  void reservasiTanpaBukuDitolak() {
    assertThrows(
        IllegalArgumentException.class, () -> Reservasi.buat(new Anggota("A1", "Budi", 2), null));
  }

  @Test
  void nomorReservasiUnik() {
    Anggota a = new Anggota("A1", "Budi", 2);
    Buku b = new Buku("B1", "Java", 0);
    assertNotEquals(Reservasi.buat(a, b).getIdReservasi(), Reservasi.buat(a, b).getIdReservasi());
  }
}

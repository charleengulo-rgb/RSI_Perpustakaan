package id.ac.ub.filkom.perpustakaan;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.ub.filkom.perpustakaan.control.*;
import id.ac.ub.filkom.perpustakaan.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = "/reset.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class IntegrationTest {
  @Autowired StokService stok;
  @Autowired PinjamService pinjam;
  @Autowired ReservasiService reservasi;
  @Autowired BukuRepository buku;
  @Autowired ReservasiRepository rs;
  @Autowired TransaksiPinjamRepository ts;

  @Test
  void updateStokTersimpan() {
    stok.ubahStok("B001", 7);
    assertEquals(7, buku.findById("B001").orElseThrow().getStok());
  }

  @Test
  void stokNegatifRollback() {
    assertThrows(IllegalArgumentException.class, () -> stok.ubahStok("B001", -2));
    assertEquals(3, buku.findById("B001").orElseThrow().getStok());
  }

  @Test
  void stokBukuTidakDitemukan() {
    assertThrows(IllegalArgumentException.class, () -> stok.ubahStok("X", 2));
  }

  @Test
  void reservasiBerhasil() {
    Bukti b = reservasi.ajukanReservasi("A001", "B002");
    assertEquals("MENUNGGU", b.status());
    assertEquals(1, rs.count());
    assertEquals(0, buku.findById("B002").orElseThrow().getStok());
  }

  @Test
  void reservasiGandaDitolak() {
    reservasi.ajukanReservasi("A001", "B002");
    assertThrows(IllegalArgumentException.class, () -> reservasi.ajukanReservasi("A001", "B002"));
    assertEquals(1, rs.count());
  }

  @Test
  void anggotaBerbedaBolehReservasiBukuSama() {
    reservasi.ajukanReservasi("A001", "B002");
    reservasi.ajukanReservasi("A002", "B002");
    assertEquals(2, rs.count());
    assertEquals(1, reservasi.daftarReservasi("A001").size());
  }

  @Test
  void reservasiTersediaDitolak() {
    assertThrows(IllegalArgumentException.class, () -> reservasi.ajukanReservasi("A001", "B001"));
    assertEquals(0, rs.count());
  }

  @Test
  void reservasiDataTidakValid() {
    assertThrows(IllegalArgumentException.class, () -> reservasi.ajukanReservasi("X", "B002"));
    assertThrows(IllegalArgumentException.class, () -> reservasi.ajukanReservasi("A001", "X"));
    assertEquals(0, rs.count());
  }

  @Test
  void reservasiDicekUlangSetelahStokBerubah() {
    stok.ubahStok("B002", 1);
    assertThrows(IllegalArgumentException.class, () -> reservasi.ajukanReservasi("A001", "B002"));
  }

  @Test
  void pinjamBerhasil() {
    Bukti b = pinjam.pinjamBuku("A001", "B001");
    assertEquals("DIPINJAM", b.status());
    assertEquals(2, buku.findById("B001").orElseThrow().getStok());
    assertEquals(1, ts.count());
  }

  @Test
  void pinjamHabisTidakMembuatTransaksi() {
    PinjamGagalException e =
        assertThrows(PinjamGagalException.class, () -> pinjam.pinjamBuku("A001", "B002"));
    assertTrue(e.isBisaReservasi());
    assertEquals(0, ts.count());
  }

  @Test
  void batasPinjamDitolak() {
    pinjam.pinjamBuku("A002", "B001");
    PinjamGagalException e =
        assertThrows(PinjamGagalException.class, () -> pinjam.pinjamBuku("A002", "B003"));
    assertFalse(e.isBisaReservasi());
    assertEquals(2, buku.findById("B003").orElseThrow().getStok());
    assertEquals(1, ts.count());
  }

  @Test
  void pinjamBukuTidakDitemukan() {
    assertThrows(IllegalArgumentException.class, () -> pinjam.pinjamBuku("A001", "X"));
    assertEquals(0, ts.count());
  }
}

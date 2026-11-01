package id.ac.ub.filkom.perpustakaan.control;

import id.ac.ub.filkom.perpustakaan.entity.*;
import java.time.LocalDate;

public record Bukti(String nomor, String anggota, String buku, LocalDate tanggal, String status) {
  public static Bukti dari(TransaksiPinjam t) {
    return new Bukti(
        t.getIdTransaksi(),
        t.getAnggota().getNama(),
        t.getBuku().getJudul(),
        t.getTanggalPinjam(),
        t.getStatus());
  }

  public static Bukti dari(Reservasi r) {
    return new Bukti(
        r.getIdReservasi(),
        r.getAnggota().getNama(),
        r.getBuku().getJudul(),
        r.getTanggalReservasi(),
        r.getStatus());
  }
}

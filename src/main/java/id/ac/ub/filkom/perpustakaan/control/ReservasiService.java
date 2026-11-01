package id.ac.ub.filkom.perpustakaan.control;

import id.ac.ub.filkom.perpustakaan.entity.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservasiService {
  private final AnggotaRepository anggotaRepository;
  private final BukuRepository bukuRepository;
  private final ReservasiRepository reservasiRepository;

  public ReservasiService(AnggotaRepository a, BukuRepository b, ReservasiRepository r) {
    anggotaRepository = a;
    bukuRepository = b;
    reservasiRepository = r;
  }

  @Transactional(readOnly = true)
  public List<Bukti> daftarReservasi(String id) {
    return reservasiRepository.findByAnggota_IdAnggotaOrderByTanggalReservasiDesc(id).stream()
        .map(Bukti::dari)
        .toList();
  }

  @Transactional(readOnly = true)
  public Buku detailBuku(String id) {
    return bukuRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Buku tidak ditemukan"));
  }

  @Transactional
  public Bukti ajukanReservasi(String idAnggota, String idBuku) {
    // R3: urutan lock sama dengan peminjaman (anggota lalu buku).
    Anggota a =
        anggotaRepository
            .cariUntukUpdate(idAnggota)
            .orElseThrow(() -> new IllegalArgumentException("Anggota tidak ditemukan"));
    Buku b =
        bukuRepository
            .cariUntukUpdate(idBuku)
            .orElseThrow(() -> new IllegalArgumentException("Buku tidak ditemukan"));
    // R4-R5: alur alternatif buku tersedia.
    if (b.cekStok())
      throw new IllegalArgumentException("Buku masih tersedia, silakan lakukan peminjaman");
    // R6-R7: hanya satu reservasi per anggota dan buku dalam lingkup latihan.
    if (reservasiRepository.existsByAnggota_IdAnggotaAndBuku_IdBuku(idAnggota, idBuku))
      throw new IllegalArgumentException("Anda sudah memiliki reservasi untuk buku ini");
    Reservasi r = Reservasi.buat(a, b); // R8
    reservasiRepository.save(r); // R9
    return Bukti.dari(r); // R10
  }
}

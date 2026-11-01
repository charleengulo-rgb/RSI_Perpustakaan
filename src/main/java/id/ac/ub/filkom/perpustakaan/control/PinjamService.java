package id.ac.ub.filkom.perpustakaan.control;

import id.ac.ub.filkom.perpustakaan.entity.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PinjamService {
  private final AnggotaRepository anggotaRepository;
  private final BukuRepository bukuRepository;
  private final TransaksiPinjamRepository transaksiRepository;

  public PinjamService(AnggotaRepository a, BukuRepository b, TransaksiPinjamRepository t) {
    anggotaRepository = a;
    bukuRepository = b;
    transaksiRepository = t;
  }

  @Transactional(readOnly = true)
  public List<Buku> daftarBuku() {
    return bukuRepository.findAll(org.springframework.data.domain.Sort.by("idBuku"));
  }

  @Transactional
  public Bukti pinjamBuku(String idAnggota, String idBuku) {
    // P3-P4: lock anggota terlebih dahulu agar batas pinjam konsisten.
    Anggota a =
        anggotaRepository
            .cariUntukUpdate(idAnggota)
            .orElseThrow(() -> new IllegalArgumentException("Anggota tidak ditemukan"));
    Buku b =
        bukuRepository
            .cariUntukUpdate(idBuku)
            .orElseThrow(() -> new IllegalArgumentException("Buku tidak ditemukan"));
    if (!a.cekBatasPinjam()) throw new PinjamGagalException("Melewati batas pinjam", false);
    // P5-P6; P13: exception stok habis membawa opsi reservasi.
    if (!b.cekStok()) throw new PinjamGagalException("Stok buku habis", true);
    TransaksiPinjam t = TransaksiPinjam.buat(a, b); // P7
    transaksiRepository.save(t); // P8
    b.kurangiStok(); // P9
    bukuRepository.save(b); // P10
    return Bukti.dari(t); // P11
  }
}

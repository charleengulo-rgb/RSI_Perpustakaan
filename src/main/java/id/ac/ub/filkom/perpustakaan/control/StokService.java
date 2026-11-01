package id.ac.ub.filkom.perpustakaan.control;

import id.ac.ub.filkom.perpustakaan.entity.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StokService {
  private final BukuRepository bukuRepository;

  public StokService(BukuRepository r) {
    bukuRepository = r;
  }

  @Transactional(readOnly = true)
  public List<Buku> daftarBuku() {
    return bukuRepository.findAll(org.springframework.data.domain.Sort.by("idBuku"));
  }

  @Transactional
  public void ubahStok(String idBuku, int stokBaru) {
    // S3-S4: ambil buku; alur alternatif ID tidak ditemukan.
    Buku buku =
        bukuRepository
            .cariUntukUpdate(idBuku)
            .orElseThrow(() -> new IllegalArgumentException("Buku tidak ditemukan"));
    // S5-S6: validasi dan perubahan stok dilakukan entity.
    buku.ubahStok(stokBaru);
    // S7: simpan; S8: return normal jika berhasil, exception jika gagal.
    bukuRepository.save(buku);
  }
}

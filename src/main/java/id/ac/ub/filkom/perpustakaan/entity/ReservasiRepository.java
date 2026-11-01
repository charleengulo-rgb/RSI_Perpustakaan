package id.ac.ub.filkom.perpustakaan.entity;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservasiRepository extends JpaRepository<Reservasi, String> {
  boolean existsByAnggota_IdAnggotaAndBuku_IdBuku(String idAnggota, String idBuku);

  List<Reservasi> findByAnggota_IdAnggotaOrderByTanggalReservasiDesc(String idAnggota);
}

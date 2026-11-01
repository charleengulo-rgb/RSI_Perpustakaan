package id.ac.ub.filkom.perpustakaan.entity;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface AnggotaRepository extends JpaRepository<Anggota, String> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select e from Anggota e where e.idAnggota = :id")
  Optional<Anggota> cariUntukUpdate(@Param("id") String id);
}

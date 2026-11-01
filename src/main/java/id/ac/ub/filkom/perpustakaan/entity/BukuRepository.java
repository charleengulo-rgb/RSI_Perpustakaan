package id.ac.ub.filkom.perpustakaan.entity;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface BukuRepository extends JpaRepository<Buku, String> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select e from Buku e where e.idBuku = :id")
  Optional<Buku> cariUntukUpdate(@Param("id") String id);
}

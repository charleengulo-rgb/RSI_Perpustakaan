package id.ac.ub.filkom.perpustakaan.control;

public class PinjamGagalException extends RuntimeException {
  private final boolean bisaReservasi;

  public PinjamGagalException(String pesan, boolean bisa) {
    super(pesan);
    bisaReservasi = bisa;
  }

  public boolean isBisaReservasi() {
    return bisaReservasi;
  }
}

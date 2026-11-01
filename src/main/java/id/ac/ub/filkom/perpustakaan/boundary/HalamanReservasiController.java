package id.ac.ub.filkom.perpustakaan.boundary;

import id.ac.ub.filkom.perpustakaan.control.*;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservasi")
public class HalamanReservasiController {
  private final ReservasiService reservasiService;

  public HalamanReservasiController(ReservasiService s) {
    reservasiService = s;
  }

  @GetMapping
  public String tampil(@RequestParam(required = false) String idBuku, Principal p, Model m) {
    m.addAttribute("reservasi", reservasiService.daftarReservasi(p.getName()));
    if (idBuku != null) {
      try {
        m.addAttribute("buku", reservasiService.detailBuku(idBuku));
      } catch (IllegalArgumentException e) {
        m.addAttribute("pesan", e.getMessage());
      }
    }
    return "reservasi";
  }

  @PostMapping // R1: anggota menekan tombol konfirmasi; GET tidak mengubah data.
  public String ajukan(@RequestParam String idBuku, Principal p, RedirectAttributes r) {
    try {
      r.addFlashAttribute(
          "bukti", reservasiService.ajukanReservasi(p.getName(), idBuku)); // R2, R10
    } catch (IllegalArgumentException e) {
      r.addFlashAttribute("pesan", e.getMessage());
    }
    return "redirect:/reservasi"; // R11
  }
}

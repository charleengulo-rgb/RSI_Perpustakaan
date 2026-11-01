package id.ac.ub.filkom.perpustakaan.boundary;

import id.ac.ub.filkom.perpustakaan.control.*;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pinjam")
public class HalamanPinjamController {
  private final PinjamService pinjamService;

  public HalamanPinjamController(PinjamService s) {
    pinjamService = s;
  }

  @GetMapping
  public String tampil(Model m, Principal p) {
    m.addAttribute("daftarBuku", pinjamService.daftarBuku());
    m.addAttribute("idAnggota", p.getName());
    return "pinjam";
  }

  @PostMapping // P1
  public String pilihBuku(@RequestParam String idBuku, Principal p, RedirectAttributes r) {
    try {
      Bukti b = pinjamService.pinjamBuku(p.getName(), idBuku); // P2
      r.addFlashAttribute("bukti", b); // P12
    } catch (PinjamGagalException e) { // P13-P14
      r.addFlashAttribute("pesan", e.getMessage());
      r.addFlashAttribute("bisaReservasi", e.isBisaReservasi());
      r.addFlashAttribute("idBukuGagal", idBuku);
    } catch (IllegalArgumentException e) {
      r.addFlashAttribute("pesan", e.getMessage());
    }
    return "redirect:/pinjam";
  }
}

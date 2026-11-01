package id.ac.ub.filkom.perpustakaan.boundary;

import id.ac.ub.filkom.perpustakaan.control.StokService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/stok")
public class HalamanStokController {
  private final StokService stokService;

  public HalamanStokController(StokService s) {
    stokService = s;
  }

  @GetMapping
  public String tampil(Model m) {
    m.addAttribute("daftarBuku", stokService.daftarBuku());
    return "stok";
  }

  @PostMapping // S1: request pustakawan.
  public String ubah(
      @RequestParam String idBuku, @RequestParam int stokBaru, RedirectAttributes r) {
    try {
      stokService.ubahStok(idBuku, stokBaru); // S2, S8
      r.addFlashAttribute("pesan", "Stok berhasil diperbarui");
    } catch (IllegalArgumentException e) {
      r.addFlashAttribute("pesan", e.getMessage());
    }
    return "redirect:/stok"; // S9
  }
}

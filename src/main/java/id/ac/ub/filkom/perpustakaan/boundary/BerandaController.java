package id.ac.ub.filkom.perpustakaan.boundary;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BerandaController {
  @GetMapping("/")
  public String beranda(Authentication a) {
    return a.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_PUSTAKAWAN"))
        ? "redirect:/stok"
        : "redirect:/pinjam";
  }
}

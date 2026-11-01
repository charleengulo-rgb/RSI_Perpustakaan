package id.ac.ub.filkom.perpustakaan;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/reset.sql")
class WebTest {
  @Autowired MockMvc mvc;

  @Test
  void loginDitampilkan() throws Exception {
    mvc.perform(get("/login")).andExpect(status().isOk());
  }

  @Test
  void tamuHarusLogin() throws Exception {
    mvc.perform(get("/stok")).andExpect(status().is3xxRedirection());
  }

  @Test
  void anggotaTidakBisaKelolaStok() throws Exception {
    mvc.perform(get("/stok").with(user("A001").roles("ANGGOTA"))).andExpect(status().isForbidden());
  }

  @Test
  void pustakawanMelihatStok() throws Exception {
    mvc.perform(get("/stok").with(user("pustakawan").roles("PUSTAKAWAN")))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Kelola stok buku")));
  }

  @Test
  void anggotaMelihatPinjam() throws Exception {
    mvc.perform(get("/pinjam").with(user("A001").roles("ANGGOTA")))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Pemrograman Java")));
  }

  @Test
  void stokHabisMenawarkanReservasi() throws Exception {
    mvc.perform(
            post("/pinjam")
                .with(user("A001").roles("ANGGOTA"))
                .with(csrf())
                .param("idBuku", "B002"))
        .andExpect(redirectedUrl("/pinjam"))
        .andExpect(flash().attribute("bisaReservasi", true));
  }

  @Test
  void formReservasiDitampilkan() throws Exception {
    mvc.perform(get("/reservasi").param("idBuku", "B002").with(user("A001").roles("ANGGOTA")))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Konfirmasi reservasi")));
  }

  @Test
  void konfirmasiReservasiBerhasil() throws Exception {
    mvc.perform(
            post("/reservasi")
                .with(user("A001").roles("ANGGOTA"))
                .with(csrf())
                .param("idBuku", "B002"))
        .andExpect(redirectedUrl("/reservasi"))
        .andExpect(flash().attributeExists("bukti"));
    mvc.perform(get("/reservasi").with(user("A001").roles("ANGGOTA")))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("MENUNGGU")));
  }

  @Test
  void stokBukanAngkaDitolak() throws Exception {
    mvc.perform(
            post("/stok")
                .with(user("pustakawan").roles("PUSTAKAWAN"))
                .with(csrf())
                .param("idBuku", "B001")
                .param("stokBaru", "abc"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void postTanpaCsrfDitolak() throws Exception {
    mvc.perform(
            post("/stok")
                .with(user("pustakawan").roles("PUSTAKAWAN"))
                .param("idBuku", "B001")
                .param("stokBaru", "5"))
        .andExpect(status().isForbidden());
  }

  @Test
  void stokValidRedirect() throws Exception {
    mvc.perform(
            post("/stok")
                .with(user("pustakawan").roles("PUSTAKAWAN"))
                .with(csrf())
                .param("idBuku", "B001")
                .param("stokBaru", "5"))
        .andExpect(redirectedUrl("/stok"))
        .andExpect(flash().attribute("pesan", "Stok berhasil diperbarui"));
  }

  @Test
  void parameterWajibDitolak() throws Exception {
    mvc.perform(post("/reservasi").with(user("A001").roles("ANGGOTA")).with(csrf()))
        .andExpect(status().isBadRequest());
  }
}

package album_dinossauros.controller;

import album_dinossauros.model.Usuario;
import album_dinossauros.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/usuario/trocar-senha")
    public String trocarSenha(@RequestParam String senhaAtual,
                              @RequestParam String novaSenha,
                              @RequestParam String confirmarSenha,
                              HttpSession session,
                              RedirectAttributes ra) {
        
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        try {
            usuarioService.atualizarSenha(usuarioLogado, senhaAtual, novaSenha, confirmarSenha);
            ra.addFlashAttribute("sucesso", "Senha alterada com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/colecionador/album";
    }
}
package album_dinossauros.controller;

import album_dinossauros.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String exibirHome(HttpSession session, Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        model.addAttribute("sessionUsuario", usuarioLogado);
        
        return "home";
    }

    @GetMapping("/sobre")
    public String sobre() {
        return "sobre";
    }


    @GetMapping("/acessar-album")
public String acessarAlbum(HttpSession session) {
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

    if (usuarioLogado == null) {
        return "redirect:/login"; 
    }

    switch (usuarioLogado.getPerfil()) {
        case "ADMIN":
            return "redirect:/admin/dashboard";
        case "AUTOR":
            return "redirect:/autor/dashboard";
        case "COLECIONADOR":
            return "redirect:/colecionador/album";
        default:
            return "redirect:/home";
    }
}

    @GetMapping("/sair")
    public String sair(HttpSession session) {
        session.invalidate(); 
        return "redirect:/"; 
}
}
package album_dinossauros.controller;

import album_dinossauros.model.Usuario;
import album_dinossauros.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- IMPORTANTE
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String telaLogin(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado != null) {
            switch (usuarioLogado.getPerfil()) {
                case "ADMIN":
                    return "redirect:/admin/dashboard";
                case "AUTOR":
                    return "redirect:/autor/dashboard";
                case "COLECIONADOR":
                    return "redirect:/colecionador/album";
            }
        }
        return "login";
    }

    @PostMapping("/login")
    public String autenticar(
            @RequestParam("login") String login,  
            @RequestParam("senha") String senha,  
            HttpSession session,
            Model model) {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByLogin(login);

        if (usuarioOpt.isEmpty()) {
            model.addAttribute("erro", "A credencial informada não existe no sistema!");
            return "login"; 
        }

        Usuario usuarioLogado = usuarioOpt.get();

        if (!passwordEncoder.matches(senha, usuarioLogado.getSenha())) {
            model.addAttribute("erro", "Chave de criptografia inválida para este Ranger!");
            return "login";
        }

        session.setAttribute("usuarioLogado", usuarioLogado);

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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/home"; 
    }
}
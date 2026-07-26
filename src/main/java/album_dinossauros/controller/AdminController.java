package album_dinossauros.controller;

import album_dinossauros.model.Usuario;
import album_dinossauros.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Importado para criptografia
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequestMapping("/admin") 
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository; 

    @Autowired
    private PasswordEncoder passwordEncoder; 
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
         List<Usuario> listaUsuarios = usuarioRepository.findAll();
         model.addAttribute("usuarios", listaUsuarios);
        
        return "admin/dashboard";
    }

    @GetMapping("/usuarios/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuario-form";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        
        if (usuario.getId() != null) {
            Usuario usuarioBanco = usuarioRepository.findById(usuario.getId()).orElse(null);
            
            if (usuarioBanco != null) {
                if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
                    usuario.setSenha(usuarioBanco.getSenha());
                } else {
                    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
                }
            }
        } else {
            if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
                usuario.setSenha(passwordEncoder.encode("123456"));
            } else {
                usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            }
        }

        usuarioRepository.save(usuario); 
        return "redirect:/admin/dashboard"; 
    }

    @GetMapping("/usuarios/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido id: " + id));
        
        model.addAttribute("usuario", usuario);
        return "admin/usuario-form"; 
    }

    @GetMapping("/usuarios/excluir/{id}")
    public String excluirUsuario(@PathVariable("id") Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido id: " + id));
        
        usuarioRepository.delete(usuario);
        return "redirect:/admin/dashboard"; 
    }
}
package album_dinossauros;

import album_dinossauros.model.Usuario;
import album_dinossauros.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void atualizarSenha(Usuario usuarioInstavel, String senhaAtual, String novaSenha, String confirmarSenha) throws Exception {
        
        Usuario usuarioBanco = usuarioRepository.findById(usuarioInstavel.getId())
            .orElseThrow(() -> new Exception("Usuário não encontrado no banco de dados."));

        if (!novaSenha.equals(confirmarSenha)) {
            throw new Exception("As novas senhas não coincidem.");
        }

        usuarioBanco.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuarioBanco);
    }
}
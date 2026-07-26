package album_dinossauros.controller;

import album_dinossauros.model.Colecao;
import album_dinossauros.model.Figurinha;
import album_dinossauros.model.Usuario;
import album_dinossauros.model.TagEscavacao;
import album_dinossauros.model.Album;
import album_dinossauros.repository.AlbumRepository;
import album_dinossauros.repository.ColecaoRepository;
import album_dinossauros.repository.FigurinhaRepository;
import album_dinossauros.repository.TagEscavacaoRepository;
import album_dinossauros.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.UUID;
import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Comparator;

@Controller
public class ColecionadorController {

    @Autowired
    private FigurinhaRepository figurinhaRepository;

    @Autowired
    private ColecaoRepository colecaoRepository;

    @Autowired
    private TagEscavacaoRepository tagEscavacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @PostMapping("/album/adicionar")
    public String adicionarFigurinha(@RequestParam("tagCodigo") String tagCodigo, 
                                    HttpSession session, 
                                    RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (tagCodigo == null || !tagCodigo.startsWith("ESC-")) {
            redirectAttributes.addFlashAttribute("erro", "Código inválido!");
            return "redirect:/colecionador/escavacao"; 
        }

        Optional<TagEscavacao> tagEscavacaoOpt = tagEscavacaoRepository.findByCodigoTag(tagCodigo);

        if (tagEscavacaoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Este código de DNA não existe!");
            return "redirect:/colecionador/escavacao";
        }

        TagEscavacao tagEscavacao = tagEscavacaoOpt.get();

        if (tagEscavacao.getResgatado() != null && tagEscavacao.getResgatado()) {
            redirectAttributes.addFlashAttribute("erro", "Este código de DNA já foi clonado e não pode ser reutilizado!");
            return "redirect:/colecionador/escavacao";
        }

        Figurinha figurinhaParaAdicionar = tagEscavacao.getFigurinha();

        Optional<Colecao> colecaoExistente = colecaoRepository.findByIdUsuarioIdAndIdFigurinhaId(
                usuarioLogado.getId(), 
                figurinhaParaAdicionar.getId()
        );

        if (colecaoExistente.isPresent()) {
            redirectAttributes.addFlashAttribute("erro", "Já possuis o espécime " + figurinhaParaAdicionar.getNome() + " no teu álbum!");
            return "redirect:/colecionador/escavacao";
        }

        Colecao novaColecao = new Colecao();
        novaColecao.setUsuario(usuarioLogado);
        novaColecao.setFigurinhas(figurinhaParaAdicionar);
        novaColecao.setQuantidade(1);
        novaColecao.setId(new Colecao.ColecaoId(usuarioLogado.getId(), figurinhaParaAdicionar.getId()));
        novaColecao.setCodigoResgate(tagEscavacao.getCodigoTag());
        
        colecaoRepository.save(novaColecao);

        tagEscavacao.setResgatado(true);
        tagEscavacaoRepository.save(tagEscavacao);

        redirectAttributes.addFlashAttribute("idNovaFigurinha", figurinhaParaAdicionar.getNumero());
        redirectAttributes.addFlashAttribute("sucesso", "Espécime " + figurinhaParaAdicionar.getNome() + " clonado com sucesso!");
        
        int paginaDino = figurinhaParaAdicionar.getPagina();
        return "redirect:/colecionador/album?pagina=" + paginaDino;
    }

    @GetMapping("/colecionador/album")
    public String exibirAlbum(HttpSession session, Model model, @RequestParam(value = "pagina", required = false) Integer pagina) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }
        
        Usuario usuarioAtual = usuarioRepository.findById(usuarioLogado.getId()).orElse(usuarioLogado);
        model.addAttribute("sessionUsuario", usuarioAtual);

        int paginaAtual = (pagina != null) ? pagina : 0;
        model.addAttribute("paginaAtiva", paginaAtual); 

        String imagemCapa = null;
        try {
            Album albumAtivo = albumRepository.findAll().stream().findFirst().orElse(null);
            if (albumAtivo != null) {
                imagemCapa = albumAtivo.getCapaPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (imagemCapa == null || imagemCapa.trim().isEmpty()) {
            imagemCapa = "/img/capas/default-capa.png";
        }
        model.addAttribute("imagemCapa", imagemCapa);

        List<Colecao> colecaoUsuario = colecaoRepository.findByUsuarioId(usuarioAtual.getId());
        if (colecaoUsuario == null) {
            colecaoUsuario = new java.util.ArrayList<>();
        }
        model.addAttribute("colecaoCompleta", colecaoUsuario);

        Set<Integer> idsObtidos = colecaoUsuario.stream()
                .filter(c -> c != null && c.getFigurinha() != null && c.getFigurinha().getNumero() != null)
                .map(c -> c.getFigurinha().getNumero())
                .collect(Collectors.toSet());
        model.addAttribute("idsObtidos", idsObtidos);

        return "colecionador/album"; 
    }

    @GetMapping("/colecionador/escavacao")
    public String exibirCanteiroEscavacao(HttpSession session, Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Usuario usuarioAtual = usuarioRepository.findById(usuarioLogado.getId()).orElse(usuarioLogado);
        model.addAttribute("sessionUsuario", usuarioAtual);

        List<TagEscavacao> tagsDisponiveis = tagEscavacaoRepository.findByUsuarioId(usuarioAtual.getId());
        if (tagsDisponiveis != null) {
            tagsDisponiveis.sort(Comparator.comparing(t -> t.getFigurinha().getNumero()));
        }
        model.addAttribute("tagsDisponiveis", tagsDisponiveis);

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime ultimaVez = usuarioAtual.getUltimaEscavacao();

        boolean liberado = true;
        String tempoRestanteFormatado = "";

        if (ultimaVez != null) {
            Duration duracao = Duration.between(ultimaVez, agora);
            if (duracao.toHours() < 24) {
                liberado = false;
                long totalSegundosRestantes = (24 * 3600) - duracao.toSeconds();
                long horas = totalSegundosRestantes / 3600;
                long minutos = (totalSegundosRestantes % 3600) / 60;
                long segundos = totalSegundosRestantes % 60;
                tempoRestanteFormatado = String.format("%02dh %02dm %02ds", horas, minutos, segundos);
            }
        }

        model.addAttribute("liberado", liberado);
        model.addAttribute("tempoRestante", tempoRestanteFormatado);
        
        return "colecionador/escavacao";
    }

    @PostMapping("/colecionador/escavar")
    public String processarEscavacao(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Usuario usuarioAtual = usuarioRepository.findById(usuarioLogado.getId()).orElse(usuarioLogado);
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime ultimaVez = usuarioAtual.getUltimaEscavacao();

        if (ultimaVez != null && Duration.between(ultimaVez, agora).toHours() < 24) {
            redirectAttributes.addFlashAttribute("erro", "O canteiro de fósseis está instável.");
            return "redirect:/colecionador/escavacao";
        }

        List<Figurinha> todasFigurinhas = figurinhaRepository.findAll();
        if (todasFigurinhas.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Nenhuma figurinha cadastrada!");
            return "redirect:/colecionador/escavacao";
        }

        Collections.shuffle(todasFigurinhas);
        int quantidadeParaPegar = Math.min(3, todasFigurinhas.size());
        List<Figurinha> figurinhasSorteadas = todasFigurinhas.subList(0, quantidadeParaPegar);

        List<TagEscavacao> tagsGeradas = new ArrayList<>();

        for (Figurinha fig : figurinhasSorteadas) {
            String sufixoAleatorio = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            String codigoUnico = "ESC-" + String.format("%03d", fig.getNumero()) + "-" + sufixoAleatorio;

            TagEscavacao novaTag = new TagEscavacao(codigoUnico, fig);
            novaTag.setUsuario(usuarioAtual);
            tagEscavacaoRepository.save(novaTag);
            tagsGeradas.add(novaTag);
        }

        usuarioAtual.setUltimaEscavacao(agora);
        usuarioRepository.save(usuarioAtual);
        session.setAttribute("usuarioLogado", usuarioAtual); 

        redirectAttributes.addFlashAttribute("figurinhasEncontradas", tagsGeradas);
        redirectAttributes.addFlashAttribute("sucesso", "Escavação concluída!");

        return "redirect:/colecionador/escavacao";
    }
}
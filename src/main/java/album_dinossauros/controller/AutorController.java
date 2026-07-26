package album_dinossauros.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import album_dinossauros.model.Album;
import album_dinossauros.model.Figurinha;
import album_dinossauros.repository.AlbumRepository;
import album_dinossauros.repository.FigurinhaRepository;

@Controller
@RequestMapping("/autor")
public class AutorController {

    @Autowired
    private FigurinhaRepository figurinhaRepository;

    @Autowired
    private AlbumRepository albumRepository;

    private static final String CAMINHO_IMAGENS = "src/main/resources/static/img/figurinhas/";
    private static final String CAMINHO_CAPAS = "src/main/resources/static/img/capas/";

    @GetMapping("/figurinhas/buscar-descricao")
    @ResponseBody
    public Map<String, String> buscarDescricaoPorTag(@RequestParam("tag") String tag) {
        Map<String, String> resposta = new HashMap<>();
        try {
            InputStream inputStream = new ClassPathResource("dinossauros-base.json").getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            
            Map<String, String> dadosDinos = mapper.readValue(inputStream, new TypeReference<Map<String, String>>(){});
            String descricao = dadosDinos.get(tag.trim().toUpperCase());
            
            if (descricao != null) {
                resposta.put("sucesso", "true");
                resposta.put("descricao", descricao);
            } else {
                resposta.put("sucesso", "false");
                resposta.put("mensagem", "Código de espécime não catalogado no arquivo base.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resposta.put("sucesso", "false");
            resposta.put("mensagem", "Erro interno ao processar o arquivo base.");
        }
        return resposta;
    }

    @GetMapping({"", "/dashboard"})
    public String dashboard() {
        return "autor/dashboard"; 
    }

    @GetMapping("/figurinhas")
    public String listar(Model model) {
        model.addAttribute("figurinhas", figurinhaRepository.findAllByOrderByNumeroAsc());
        return "autor/figurinha"; 
    }

    @GetMapping("/figurinhas/nova")
    public String nova(Model model) {
        model.addAttribute("figurinha", new Figurinha());
        model.addAttribute("albuns", albumRepository.findAll());
        return "autor/figurinha-form";
    }

    @GetMapping("/figurinhas/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        Figurinha figurinha = figurinhaRepository.findById(id).orElse(null);
        if (figurinha == null) {
            return "redirect:/autor/figurinhas";
        }
        model.addAttribute("figurinha", figurinha);
        model.addAttribute("albuns", albumRepository.findAll());
        return "autor/figurinha-form";
    }

    @PostMapping("/figurinhas/salvar")
    public String salvar(@ModelAttribute("figurinha") Figurinha figurinha, 
                         @RequestParam("file") MultipartFile file,
                         @RequestParam("albumId") Long albumId,
                         RedirectAttributes redirectAttributes) {
        
        Optional<Figurinha> figurinhaPorNumero = figurinhaRepository.findByNumero(figurinha.getNumero());
        if (figurinhaPorNumero.isPresent()) {
            if (figurinha.getId() == null || !figurinhaPorNumero.get().getId().equals(figurinha.getId())) {
                redirectAttributes.addFlashAttribute("erro", "Não foi possível registar: Já existe um espécime catalogado com o número " + figurinha.getNumero() + "!");
                if (figurinha.getId() == null) {
                    return "redirect:/autor/figurinhas/nova";
                } else {
                    return "redirect:/autor/figurinhas/editar/" + figurinha.getId();
                }
            }
        }

        Optional<Figurinha> figurinhaPorTag = figurinhaRepository.findByTag(figurinha.getTag());
        if (figurinhaPorTag.isPresent()) {
            if (figurinha.getId() == null || !figurinhaPorTag.get().getId().equals(figurinha.getId())) {
                redirectAttributes.addFlashAttribute("erro", "Não foi possível registar: A tag '" + figurinha.getTag() + "' já está associada a outro dinossauro!");
                if (figurinha.getId() == null) {
                    return "redirect:/autor/figurinhas/nova";
                } else {
                    return "redirect:/autor/figurinhas/editar/" + figurinha.getId();
                }
            }
        }
        
        if (figurinha.getId() != null && file.isEmpty()) {
            Figurinha figurinhaBanco = figurinhaRepository.findById(figurinha.getId()).orElse(null);
            if (figurinhaBanco != null) {
                figurinha.setFoto(figurinhaBanco.getFoto());
            }
        }

        if (!file.isEmpty()) {
            try {
                String nomeArquivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                Path path = Paths.get(CAMINHO_IMAGENS + nomeArquivo);
                
                Files.createDirectories(path.getParent());
                Files.write(path, bytes);
                
                figurinha.setFoto(nomeArquivo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        Album albumSelecionado = albumRepository.findById(albumId).orElse(null);
        if (albumSelecionado != null) {
            figurinha.setAlbum(albumSelecionado);
        }
        
        figurinhaRepository.save(figurinha);
        redirectAttributes.addFlashAttribute("sucesso", "Espécime '" + figurinha.getNome() + "' catalogado perfeitamente!");
        return "redirect:/autor/figurinhas";
    }

    @GetMapping("/figurinhas/excluir/{id}")
    public String excluir(@PathVariable("id") Long id) {
        figurinhaRepository.deleteById(id);
        return "redirect:/autor/albuns";
    }

    @GetMapping("/figurinhas/excluir-todas")
    public String excluirTodasFigurinhas(RedirectAttributes redirectAttributes) {
        try {
            figurinhaRepository.deleteAll();
            redirectAttributes.addFlashAttribute("erro", "Sucesso: O acervo global de figurinhas foi completamente resetado.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erro", "Falha técnica ao tentar limpar o acervo de dados.");
        }
        return "redirect:/autor/albuns";
    }

    @GetMapping("/albuns")
    public String gerenciarAlbuns(Model model) {
        model.addAttribute("album", new Album());
        model.addAttribute("albuns", albumRepository.findAll());
        model.addAttribute("todasFigurinhas", figurinhaRepository.findAllByOrderByNumeroAsc());
        return "autor/album-gerenciamento";
    }

    @GetMapping("/albuns/editar/{id}")
    public String editarAlbum(@PathVariable("id") Long id, Model model) {
        Album album = albumRepository.findById(id).orElse(null);
        if (album == null) {
            return "redirect:/autor/albuns";
        }
        model.addAttribute("album", album);
        model.addAttribute("albuns", albumRepository.findAll());
        model.addAttribute("todasFigurinhas", figurinhaRepository.findAllByOrderByNumeroAsc());
        return "autor/album-gerenciamento";
    }

    @PostMapping("/albuns/salvar")
    public String salvarAlbum(@ModelAttribute("album") Album album, 
                              @RequestParam("imagemCapa") MultipartFile imagemCapa,
                              RedirectAttributes redirectAttributes) {
        
        if (album.getId() != null) {
            Album albumBanco = albumRepository.findById(album.getId()).orElse(null);
            if (albumBanco != null) {
                if (imagemCapa.isEmpty()) {
                    album.setCapaPath(albumBanco.getCapaPath());
                } else {
                    try {
                        String nomeCapa = UUID.randomUUID().toString() + "_" + imagemCapa.getOriginalFilename();
                        Path path = Paths.get(CAMINHO_CAPAS + nomeCapa);
                        Files.createDirectories(path.getParent());
                        Files.write(path, imagemCapa.getBytes());
                        album.setCapaPath("/img/capas/" + nomeCapa);
                    } catch (IOException e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("erro", "Falha técnica ao atualizar arquivo da capa.");
                        return "redirect:/autor/albuns";
                    }
                }
            }
        } 
        else {
            List<Album> albunsExistentes = albumRepository.findAll();
            if (!albunsExistentes.isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "O ecossistema já possui um álbum ativo! Exclua ou edite o atual.");
                return "redirect:/autor/albuns";
            }

            if (!imagemCapa.isEmpty()) {
                try {
                    String nomeCapa = UUID.randomUUID().toString() + "_" + imagemCapa.getOriginalFilename();
                    Path path = Paths.get(CAMINHO_CAPAS + nomeCapa);
                    Files.createDirectories(path.getParent());
                    Files.write(path, imagemCapa.getBytes());
                    album.setCapaPath("/img/capas/" + nomeCapa);
                } catch (IOException e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("erro", "Falha técnica ao salvar arquivo da capa.");
                    return "redirect:/autor/albuns";
                }
            }
        }

        albumRepository.save(album);
        return "redirect:/autor/albuns";
    }

    @GetMapping("/albuns/excluir/{id}")
    public String excluirAlbum(@PathVariable("id") Long id) {
        albumRepository.deleteById(id);
        return "redirect:/autor/albuns";
    }
}
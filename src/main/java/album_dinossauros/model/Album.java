package album_dinossauros.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "albuns")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(name = "quantidade_paginas", nullable = false)
    private Integer quantidadePaginas;

    @Column(name = "capa_path")
    private String capaPath; // Guardará o caminho de acesso da imagem de capa

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Figurinha> figurinhas;

    public Album() {}

    public Album(String nome, Integer quantidadePaginas, String capaPath) {
        this.nome = nome;
        this.quantidadePaginas = quantidadePaginas;
        this.capaPath = capaPath;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getQuantidadePaginas() { return quantidadePaginas; }
    public void setQuantidadePaginas(Integer quantidadePaginas) { this.quantidadePaginas = quantidadePaginas; }

    public String getCapaPath() { return capaPath; }
    public void setCapaPath(String capaPath) { this.capaPath = capaPath; }

    public List<Figurinha> getFigurinhas() { return figurinhas; }
    public void setFigurinhas(List<Figurinha> figurinhas) { this.figurinhas = figurinhas; }
}
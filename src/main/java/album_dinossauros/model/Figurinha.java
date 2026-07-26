package album_dinossauros.model;

import jakarta.persistence.*;

@Entity
@Table(name = "figurinhas")
public class Figurinha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Integer pagina;

    private String tag;
    
    private String foto;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    public Figurinha() {}

    public Figurinha(Integer numero, String nome, String descricao, Integer pagina, String tag, String foto, Album album) {
        this.numero = numero;
        this.nome = nome;
        this.descricao = descricao;
        this.pagina = pagina;
        this.tag = tag;
        this.foto = foto;
        this.album = album;
    }

    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

    public Integer getNumero() { 
        return numero; 
    }
    
    public void setNumero(Integer numero) { 
        this.numero = numero; 
    }

    public String getNome() { 
        return nome; 
    }
    
    public void setNome(String nome) { 
        this.nome = nome; 
    }

    public String getDescricao() { 
        return descricao; 
    }
    
    public void setDescricao(String descricao) { 
        this.descricao = descricao; 
    }

    public Integer getPagina() { 
        return pagina; 
    }
    
    public void setPagina(Integer pagina) { 
        this.pagina = pagina; 
    }

    public String getTag() { 
        return tag; 
    }
    
    public void setTag(String tag) { 
        this.tag = tag; 
    }

    public String getFoto() { 
        return foto; 
    }
    
    public void setFoto(String foto) { 
        this.foto = foto; 
    }

    public Album getAlbum() { 
        return album; 
    }
    
    public void setAlbum(Album album) { 
        this.album = album; 
    }
}
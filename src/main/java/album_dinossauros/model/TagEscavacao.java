package album_dinossauros.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tags_escavacao")
public class TagEscavacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_tag", unique = true)
    private String codigoTag;

    @ManyToOne
    @JoinColumn(name = "figurinha_id")
    private Figurinha figurinha;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "resgatado")
    private Boolean resgatado = false;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }

    public TagEscavacao() {
    }

    public TagEscavacao(String codigoTag, Figurinha figurinha) {
        this.codigoTag = codigoTag;
        this.figurinha = figurinha;
        this.resgatado = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoTag() {
        return codigoTag;
    }

    public void setCodigoTag(String codigoTag) {
        this.codigoTag = codigoTag;
    }

    public Figurinha getFigurinha() {
        return figurinha;
    }

    public void setFigurinha(Figurinha figurinha) {
        this.figurinha = figurinha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getResgatado() {
        return resgatado;
    }

    public void setResgatado(Boolean resgatado) {
        this.resgatado = resgatado;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
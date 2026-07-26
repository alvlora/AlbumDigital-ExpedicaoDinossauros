package album_dinossauros.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "usuarios_figurinhas")
public class Colecao {

    @EmbeddedId
    private ColecaoId id;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @MapsId("figurinhaId")
    @JoinColumn(name = "figurinha_id")
    private Figurinha figurinha;

    private Integer quantidade;

    @Column(name = "codigo_resgate")
    private String codigoResgate;

    public Colecao() {
    }

    public ColecaoId getId() {
        return id;
    }

    public void setId(ColecaoId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Figurinha getFigurinha() {
        return figurinha;
    }

    public void setFigurinhas(Figurinha figurinha) {
        this.figurinha = figurinha;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getCodigoResgate() {
        return codigoResgate;
    }

    public void setCodigoResgate(String codigoResgate) {
        this.codigoResgate = codigoResgate;
    }

    @Embeddable
    public static class ColecaoId implements Serializable {
        private Long usuarioId;
        private Long figurinhaId;

        public ColecaoId() {}

        public ColecaoId(Long usuarioId, Long figurinhaId) {
            this.usuarioId = usuarioId;
            this.figurinhaId = figurinhaId;
        }

        public Long getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
        }

        public Long getFigurinhaId() {
            return figurinhaId;
        }

        public void setFigurinhaId(Long figurinhaId) {
            this.figurinhaId = figurinhaId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ColecaoId colecaoId = (ColecaoId) o;
            return Objects.equals(usuarioId, colecaoId.usuarioId) && Objects.equals(figurinhaId, colecaoId.figurinhaId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(usuarioId, figurinhaId);
        }
    }
}
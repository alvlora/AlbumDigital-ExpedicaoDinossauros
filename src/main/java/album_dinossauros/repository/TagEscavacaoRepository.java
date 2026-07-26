package album_dinossauros.repository;

import album_dinossauros.model.TagEscavacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface TagEscavacaoRepository extends JpaRepository<TagEscavacao, Long> {
    Optional<TagEscavacao> findByCodigoTag(String codigoTag);
    List<TagEscavacao> findByUsuarioId(Long usuarioId);
}
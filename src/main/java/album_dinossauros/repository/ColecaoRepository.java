package album_dinossauros.repository;

import album_dinossauros.model.Colecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ColecaoRepository extends JpaRepository<Colecao, Colecao.ColecaoId> {
    List<Colecao> findByUsuarioId(Long usuarioId);

    Optional<Colecao> findByIdUsuarioIdAndIdFigurinhaId(Long usuarioId, Long figurinhaId);
}
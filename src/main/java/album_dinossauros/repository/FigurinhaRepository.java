package album_dinossauros.repository;

import album_dinossauros.model.Figurinha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FigurinhaRepository extends JpaRepository<Figurinha, Long> {
    List<Figurinha> findByAlbumId(Long albumId);
    Optional<Figurinha> findByTag(String tag);
    List<Figurinha> findAllByOrderByNumeroAsc();
    Optional<Figurinha> findByNumero(Integer numero);
}
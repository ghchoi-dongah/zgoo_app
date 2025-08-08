package zgoo.app.repository.support;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.support.Voc;

public interface VocRepository extends JpaRepository<Voc, Long>, VocRepositoryCustom {

}

package zgoo.app.repository.support;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.support.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long>, FaqRepositoryCustom {

}

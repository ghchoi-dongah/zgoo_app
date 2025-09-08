package zgoo.app.repository.charge;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.charge.CsInfo;

public interface CsRepository extends JpaRepository<CsInfo, String>, CsRepositoryCustom {

}

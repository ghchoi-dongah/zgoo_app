package zgoo.app.repository.biz;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.biz.BizInfo;

public interface BizRepository extends JpaRepository<BizInfo, Long>, BizRepositoryCustom {

}

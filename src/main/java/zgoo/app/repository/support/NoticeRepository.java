package zgoo.app.repository.support;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.support.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {

}

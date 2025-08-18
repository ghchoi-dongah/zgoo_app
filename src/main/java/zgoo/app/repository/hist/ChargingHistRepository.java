package zgoo.app.repository.hist;

import org.springframework.data.jpa.repository.JpaRepository;

import zgoo.app.domain.hist.ChargingHist;

public interface ChargingHistRepository extends JpaRepository<ChargingHist, Long>, ChargingHistRepositoryCustom {

}

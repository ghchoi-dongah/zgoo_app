package zgoo.app.repository.member;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.member.QMemberCar;
import zgoo.app.dto.member.MemberDto.MemberCarDto;

@Slf4j
@RequiredArgsConstructor
public class MemberCarRepositoryCustomImpl implements MemberCarRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QMemberCar car = QMemberCar.memberCar;

    @Override
    public List<MemberCarDto> findAllByMemberId(Long memberId) {
        return queryFactory.select(Projections.fields(MemberCarDto.class,
                car.carNum.as("carNum"),
                car.model.as("model")))
                .from(car)
                .where(car.member.id.eq(memberId))
                .fetch();
    }
}

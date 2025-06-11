package zgoo.app.repository.member;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.member.Member;
import zgoo.app.domain.member.QMember;

@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QMember member = QMember.member;

    @Override
    public  Optional<Member> findByMemLoginId(String memLoginId) {
        Member result =  queryFactory
                .selectFrom(member)
                .where(member.memLoginId.eq(memLoginId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}

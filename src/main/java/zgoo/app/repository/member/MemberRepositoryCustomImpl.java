package zgoo.app.repository.member;

import java.util.Optional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.member.Member;
import zgoo.app.domain.member.QMember;
import zgoo.app.dto.member.MemberDto.MemberRegDto;

@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QMember member = QMember.member;

    @Override
    public Optional<Member> findByMemLoginId(String memLoginId) {
        Member result =  queryFactory
                .selectFrom(member)
                .where(member.memLoginId.eq(memLoginId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Member findMemberByMemLoginId(String memLoginId) {
        Member result =  queryFactory
                .selectFrom(member)
                .where(member.memLoginId.eq(memLoginId))
                .fetchOne();

        return result;
    }

    @Override
    public Optional<String> findMemLoginIdByNameAndPhone(String name, String phone) {
        String result =  queryFactory
                .select(member.memLoginId)
                .from(member)
                .where(member.name.eq(name), member.phoneNo.eq(phone))
                .fetchOne();
                
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Member> findMemberByNameAndIdAndPhone(String name, String memLoginId, String phone) {
        Member result = queryFactory
                .selectFrom(member)
                .where(member.name.eq(name), member.memLoginId.eq(memLoginId), member.phoneNo.eq(phone))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<String> getName(String memLoginId) {
        String result = queryFactory
                .select(member.name)
                .from(member)
                .where(member.memLoginId.eq(memLoginId))
                .fetchOne();
            
        return Optional.ofNullable(result);
    }

    @Override
    public MemberRegDto findMemberInfo(String memLoginId) {
        MemberRegDto dto = queryFactory.select(Projections.fields(MemberRegDto.class,
                member.id.as("memberId"),
                member.name.as("name"),
                member.memLoginId.as("memLoginId"),
                member.email.as("email"),
                member.birth.as("birth"),
                member.zipCode.as("zipCode"),
                member.address.as("address"),
                member.addressDetail.as("addressDetail")))
                .from(member)
                .where(member.memLoginId.eq(memLoginId))
                .fetchOne();

        return dto;
    }
}

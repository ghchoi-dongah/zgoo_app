package zgoo.app.repository.support;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.support.QNotice;
import zgoo.app.dto.support.NoticeDto.NoticeListDto;

@Slf4j
@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QNotice notice = QNotice.notice;
    LocalDate today = LocalDate.now();

    @Override
    public List<NoticeListDto> findNotices() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(notice.delYn.eq("N"));
        builder.and(notice.startDate.loe(today));
        builder.and(notice.endDate.goe(today));

        return queryFactory.select(Projections.fields(NoticeListDto.class,
                notice.id.as("noticeId"),
                notice.title.as("title"),
                notice.content.as("content"),
                notice.views.as("views"),
                notice.regDt.as("regDt"),
                notice.startDate.as("startDate")))
                .from(notice)
                .where(builder)
                .orderBy(notice.regDt.desc())
                .fetch();
    }
    
    @Override
    public List<NoticeListDto> getRecentNotices() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(notice.delYn.eq("N"));
        builder.and(notice.startDate.loe(today));
        builder.and(notice.endDate.goe(today));

        return queryFactory.select(Projections.fields(NoticeListDto.class,
                notice.id.as("noticeId"),
                notice.title.as("title"),
                notice.content.as("content"),
                notice.views.as("views"),
                notice.regDt.as("regDt"),
                notice.startDate.as("startDate")))
                .from(notice)
                .where(builder)
                .orderBy(notice.regDt.desc())
                .limit(5)
                .fetch();
    }
}

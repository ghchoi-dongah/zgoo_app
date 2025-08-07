package zgoo.app.repository.support;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.code.QCommonCode;
import zgoo.app.domain.support.QFaq;
import zgoo.app.dto.support.FaqDto.FaqBaseDto;

@Slf4j
@RequiredArgsConstructor
public class FaqRepositoryCustomImpl implements FaqRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QFaq faq = QFaq.faq;
    QCommonCode sectionName = new QCommonCode("section");

    @Override
    public List<FaqBaseDto> findFaqAll() {

        return queryFactory.select(Projections.fields(FaqBaseDto.class,
                faq.id.as("faqId"),
                faq.title.as("title"),
                faq.content.as("content"),
                faq.section.as("section"),
                faq.regDt.as("regDt"),
                sectionName.name.as("sectionName")))
                .from(faq)
                .leftJoin(sectionName).on(faq.section.eq(sectionName.commonCode))
                .where(faq.delYn.eq("N"))
                .orderBy(faq.regDt.desc())
                .fetch();
    }

    @Override
    public List<FaqBaseDto> getFaqBySection(String section) {
        return queryFactory.select(Projections.fields(FaqBaseDto.class,
                faq.id.as("faqId"),
                faq.title.as("title"),
                faq.content.as("content"),
                faq.section.as("section"),
                faq.regDt.as("regDt"),
                sectionName.name.as("sectionName")))
                .from(faq)
                .leftJoin(sectionName).on(faq.section.eq(sectionName.commonCode))
                .where(faq.delYn.eq("N").and(faq.section.eq(section)))
                .orderBy(faq.regDt.desc())
                .fetch();
    }
}

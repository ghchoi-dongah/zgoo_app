package zgoo.app.repository.support;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.support.QVoc;

@Slf4j
@RequiredArgsConstructor
public class VocRepositoryCustomImpl implements VocRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QVoc voc = QVoc.voc;
}

package zgoo.app.repository.support;

import java.util.List;

import zgoo.app.dto.support.FaqDto.FaqBaseDto;

public interface FaqRepositoryCustom {
    List<FaqBaseDto> findFaqAll();
    List<FaqBaseDto> getFaqBySection(String section);
}

package zgoo.app.repository.support;

import java.util.List;

import zgoo.app.dto.support.VocDto.VocDetailDto;
import zgoo.app.dto.support.VocDto.VocListDto;

public interface VocRepositoryCustom {
    List<VocListDto> findVocAll(Long memberId);
    List<VocListDto> getVocByType(String type, Long memberId);
    VocDetailDto findVocDetailOne(Long id);
    Long updateDelYn(Long id);
}

package zgoo.app.repository.code;

import java.util.List;

import zgoo.app.dto.code.CodeDto.CommCdBaseDto;

public interface CommonCodeRepositoryCustom {
    List<CommCdBaseDto> findCommonCdNamesByGrpCode(String grpcode);
}

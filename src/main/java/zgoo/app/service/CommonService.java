package zgoo.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.code.CodeDto.CommCdBaseDto;
import zgoo.app.repository.code.CommonCodeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonService {

    private final CommonCodeRepository commonCodeRepository;

    public List<CommCdBaseDto> findCommonCdNamesByGrpcd(String grpcd) {
        try {
            List<CommCdBaseDto> codeList = this.commonCodeRepository.findCommonCdNamesByGrpCode(grpcd);
            log.info("CommonService >> findCommonCdNamesGrpcd] codeList: {}", codeList.toString());
            return codeList;
        } catch (Exception e) {
            log.error("[CommonService >> findCommonCdNamesGrpcd] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}

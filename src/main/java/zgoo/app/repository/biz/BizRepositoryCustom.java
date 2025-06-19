package zgoo.app.repository.biz;

import java.util.List;

import zgoo.app.dto.member.BizDto.BizBaseDto;

public interface BizRepositoryCustom {
    List<BizBaseDto> findBizInfoByBizName(String bizName);
}

package zgoo.app.repository.support;

import java.util.List;

import zgoo.app.dto.support.NoticeDto.NoticeListDto;

public interface NoticeRepositoryCustom {
    List<NoticeListDto> findNotices();
    List<NoticeListDto> getRecentNotices();
}

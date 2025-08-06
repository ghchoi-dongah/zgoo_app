package zgoo.app.repository.support;

import java.util.List;

import zgoo.app.dto.support.NoticeDto.NoticeDetailDto;
import zgoo.app.dto.support.NoticeDto.NoticeListDto;

public interface NoticeRepositoryCustom {
    List<NoticeListDto> findNotices();
    List<NoticeListDto> getRecentNotices();
    List<NoticeListDto> getNoticesByType(String typeCode);
    NoticeDetailDto findNoticeDetailOne(Long id);
}

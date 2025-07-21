package zgoo.app.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.dto.support.NoticeDto.NoticeListDto;
import zgoo.app.repository.support.NoticeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 공지사항 조회
    public List<NoticeListDto> findNoticeAll() {
        try {
            List<NoticeListDto> noticeList = this.noticeRepository.findNotices();
            confirmNoticeNew(noticeList);
            log.info("[NoticeService >> findNoticeAll] noticeList: {}", noticeList.toString());
            return noticeList;
        } catch (Exception e) {
            log.error("[NoticeService >> findNoticeAll] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // 최신 5건 조회
    public List<NoticeListDto> getRecentNotices() {
        try {
            List<NoticeListDto> noticeList = this.noticeRepository.getRecentNotices();
            confirmNoticeNew(noticeList);
            log.info("[Noticeservice >> getRecentNotices] noticeList: {}", noticeList.toString());
            return noticeList;
        } catch (Exception e) {
            log.error("[NoticeService >> getRecentNotices] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // new notice
    public void confirmNoticeNew(List<NoticeListDto> dtos) {
        LocalDate now = LocalDate.now();

        dtos.forEach(dto -> {
            LocalDate registrationDate = dto.getStartDate();
            long daysBetween = ChronoUnit.DAYS.between(registrationDate, now);
            dto.setNew(daysBetween < 3);
        });
    }
}

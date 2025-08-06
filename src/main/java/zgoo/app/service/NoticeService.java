package zgoo.app.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zgoo.app.domain.support.Notice;
import zgoo.app.dto.support.NoticeDto.NoticeDetailDto;
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

    // 공지사항(상세)
    @Transactional
    public NoticeDetailDto findNoticeDetailOne(Long id) {
        try {
            incrementViewCount(id);
            NoticeDetailDto notice = this.noticeRepository.findNoticeDetailOne(id);
            log.info("[NoticeService >> findNoticeDetailOne] notice: {}", notice.toString());
            return notice;
        } catch (Exception e) {
            log.error("[NoticeService >> findNoticeDetailOne] error: {}", e.getMessage(), e);
            return null;
        }
    }

    // 조회수 증가
    @Transactional
    public void incrementViewCount(Long id) {
        try {
            Notice notice = this.noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항 정보를 찾을 수 없습니다."));

            notice.setViews(notice.getViews() + 1);
        } catch (Exception e) {
            log.error("[NoticeService >> incrementViewCount] error: {}", e.getMessage(), e);
        }
    }

    // 유형별 공지조회
    public List<NoticeListDto> getNoticesByType(String code) {
        try {
            List<NoticeListDto> noticeList = this.noticeRepository.getNoticesByType(code);
            confirmNoticeNew(noticeList);
            log.info("[NoticeService >> getNoticesByType] typeCode:{},  noticeList: {}", code, noticeList.toString());
            return noticeList;
        } catch (Exception e) {
            log.error("[NoticeService >> getNoticesByType] error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}

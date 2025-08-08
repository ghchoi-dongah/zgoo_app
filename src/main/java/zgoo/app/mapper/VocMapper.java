package zgoo.app.mapper;

import java.time.LocalDateTime;

import zgoo.app.domain.member.Member;
import zgoo.app.domain.support.Voc;
import zgoo.app.dto.support.VocDto.VocBaseDto;
import zgoo.app.util.CodeConstants;

public class VocMapper {

    /* 
     * voc(dto >> entity)
     */
    public static Voc toEntity(VocBaseDto dto, Member member) {
        Voc voc = Voc.builder()
                .member(member)
                .type(dto.getType())
                .regDt(LocalDateTime.now())
                .title(dto.getTitle())
                .content(dto.getContent())
                .delYn("N")
                .replyStat(CodeConstants.STANDBY)
                .channel(CodeConstants.VOCAPP)
                .build();
        return voc;
    }
}

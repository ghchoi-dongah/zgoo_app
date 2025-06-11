package zgoo.app.domain.member;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "MEMBER")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "mem_login_id", unique = true, nullable = false)
    private String memLoginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "biz_type", nullable = false)
    private String bizType;

    @Column(name = "phon_no")
    private String phoneNo;

    @Column(name = "id_tag", unique = true, nullable = false, length = 16)
    private String idTag;

    @Column(name = "email")
    private String email;

    @Column(name = "birth")
    private String birth;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "joined_dt")
    private LocalDateTime joinedDt;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    public void updateMemberInfo() {

    }

    public void updatePasswordInfo(String password) {
        this.password = password;
    }
}

package zgoo.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import zgoo.app.domain.member.Member;
import zgoo.app.repository.member.MemberRepository;
import zgoo.app.type.MemberRole;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memLoginId) throws UsernameNotFoundException {
        Optional<Member> _member = this.memberRepository.findByMemLoginId(memLoginId);
        if (_member.isEmpty()) {
            throw new UsernameNotFoundException("회원을 찾을 수 없습니다.");
        }

        Member member = _member.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if("zgoodev".equals(memLoginId)) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MemberRole.MEMBER.getValue()));
        }

        return new User(member.getMemLoginId(), member.getPassword(), authorities);
    }
}

package zgoo.app.util;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        HttpSession session = request.getSession();
        String memLoginId = authentication.getName();
        session.setAttribute("memLoginId", memLoginId);

        System.out.println("로그인 성공: 사용자 ID가 세션에 저장되었습니다 >> " + memLoginId);
        log.info("로그인 성공 >> {}", memLoginId);

        response.sendRedirect("/main");
    }
}

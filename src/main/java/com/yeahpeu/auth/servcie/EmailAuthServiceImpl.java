package com.yeahpeu.auth.servcie;

import com.yeahpeu.auth.domain.EmailAuthEntity;
import com.yeahpeu.auth.repository.EmailAuthRepository;
import com.yeahpeu.common.exception.BadRequestException;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.user.util.AuthCodeUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;

import static com.yeahpeu.common.exception.ExceptionCode.*;

@RequiredArgsConstructor
@Service
public class EmailAuthServiceImpl implements EmailAuthService {

    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createVerificationCode(String emailAddress) {

        userRepository.findByEmailAddress(emailAddress).ifPresent(user -> {
            throw new BadRequestException(DUPLICATE_EMAIL);
        });

        // 입력받은 이메일에 대한 인증코드 정보 유무 확인
        EmailAuthEntity emailAuth = emailAuthRepository.findByEmailAddress(emailAddress);
        String authCode = AuthCodeUtil.generateCode();

        // 재발급
        if (emailAuth != null) {
            updateEmailAuth(emailAuth, authCode);
        } else { // 신규발급
            emailAuth = createEmailAuth(emailAddress, authCode);
        }

        // 이메일 전송
        try {
            sendVerificationEmailAsync(emailAddress, authCode).join();
        } catch (Exception e) {
            // 이메일 발송 실패 시 트랜잭션 롤백
            throw new BadRequestException(FAIL_EMAIL_SENDING);
        }

        emailAuth.setExpiredAt(ZonedDateTime.now().plusMinutes(3));  // 인증 코드 만료 시간 이메일 전송 완료 후 설정
        emailAuthRepository.save(emailAuth);
    }

    private EmailAuthEntity createEmailAuth(String emailAddress, String authCode) {
        EmailAuthEntity newEmailAuth = new EmailAuthEntity();
        newEmailAuth.setEmailAddress(emailAddress);
        newEmailAuth.setAuthCode(authCode);
        newEmailAuth.setAuthStatus(false);  // 인증 상태 설정

        return newEmailAuth;
    }

    private void updateEmailAuth(EmailAuthEntity emailAuth, String authCode) {
        emailAuth.setAuthCode(authCode);
        emailAuth.setAuthStatus(false);  // 인증 상태 설정
    }

    @Async
    public CompletableFuture<Void> sendVerificationEmailAsync(String to, String authCode) {
        try {
            sendVerificationEmail(to, authCode);
            return CompletableFuture.completedFuture(null);
        } catch (MessagingException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    private void sendVerificationEmail(String to, String authCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);


        String content = "<html>"
                + "<head>"
                + "<style>"
                + "  @media (prefers-color-scheme: dark) {"
                + "    body { background-color: #1e1e1e !important; color: #f5f5f5 !important; }"
                + "    .email-container { background: #2c2c2c !important; color: white !important; box-shadow: 0 4px 8px rgba(255,255,255,0.1); }"
                + "    h2 { color: #ffaaaa !important; }"
                + "    .auth-code { background: #3c3c3c !important; color: #ffaaaa !important; border: 1px solid #ffaaaa !important; }"
                + "  }"
                + "</style>"
                + "</head>"
                + "<body style='font-family: Arial, sans-serif; background-color: #f8f8f8; padding: 30px; text-align: center;'>"
                + "    <div class='email-container' style='max-width: 500px; margin: auto; background: white; border-radius: 12px; padding: 20px; "
                + "    box-shadow: 0 4px 8px rgba(0,0,0,0.1); text-align: center;'>"
                + "        <h2 style='color: #ff6666; font-size: 22px;'>✨ 예쀼 이메일 인증 ✨</h2>"
                + "        <p style='font-size: 16px; color: #555;'>아래의 인증 코드를 입력하여 <br> 이메일 인증을 완료하세요.</p>"
                + "        <div class='auth-code' style='display: inline-block; font-weight: bold; font-size: 22px; "
                + "        color: #ff6666; background: #f0f0f0; padding: 15px 30px; border-radius: 8px; margin: 15px 0; border: 2px solid #ff6666;'>"
                + authCode
                + "        </div>"
                + "        <p style='font-size: 14px; color: #888;'>이 코드는 3분간 유효합니다.</p>"
                + "    </div>"
                + "</body>"
                + "</html>";


        helper.setTo(to);
        helper.setSubject("예쀼 이메일 인증 코드를 확인하세요.");
        helper.setText(content, true);

        mailSender.send(message);
    }

    public void validateAuthCode(String emailAddress, String authCode) {
        // 인증코드가 입력되지 않음
        if (authCode == null || authCode.trim().isEmpty()) {
            throw new BadRequestException(FAIL_EMAIL_AUTH);
        }

        EmailAuthEntity emailAuth = emailAuthRepository.findByEmailAddress(emailAddress);

        // 인증 정보 없음
        if (emailAuth == null) {
            throw new BadRequestException(NOT_FOUND_EMAIL_AUTH_INFO);
        }

        // 인증 중복 요청 처리
        if (emailAuth.getAuthStatus()) {
            throw new BadRequestException(ALREADY_VALIDATED_EMAIL);
        }

        // 인증 코드 불일치
        if (!authCode.equals(emailAuth.getAuthCode())) {
            throw new BadRequestException(FAIL_EMAIL_AUTH);
        }

        // 인증 시간 만료
        if (isExpired(emailAuth)) {
            throw new BadRequestException(EMAIL_AUTH_EXPIRED);
        }

        emailAuth.setAuthStatus(true);
        emailAuthRepository.save(emailAuth);
    }

    private boolean isExpired(EmailAuthEntity entity) {
        ZonedDateTime now = ZonedDateTime.now();
        return now.isAfter(entity.getExpiredAt());
    }
}

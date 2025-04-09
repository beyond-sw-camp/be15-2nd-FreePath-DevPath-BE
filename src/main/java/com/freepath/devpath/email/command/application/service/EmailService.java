package com.freepath.devpath.email.command.application.service;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.email.command.application.Dto.EmailAuthPurpose;
import com.freepath.devpath.email.config.RedisUtil;
import com.freepath.devpath.email.exception.TempUserNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService {
    private final RedisUtil redisUtil;
    private final JavaMailSender mailSender;
    private int authNumber;

    //임의의 6자리 양수를 반환합니다.
    public void makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        this.authNumber = Integer.parseInt(randomNumber);
    }


    //mail을 어디서 보내는지, 어디로 보내는지 , 인증 번호를 html 형식으로 어떻게 보내는지 작성합니다.
    public String joinEmail(String email) {
        makeRandomNumber();
        String setFrom = "leessjjgg123@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "[DevPath] 회원 가입 인증 이메일 입니다."; // 이메일 제목
        String content =
                "DevPath를 방문해주셔서 감사합니다." + 	//html 형식으로 작성 !
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "인증번호를 제대로 입력해주세요"; //이메일 내용 삽입
        mailSend(setFrom, toMail, title, content);
        return Integer.toString(authNumber);
    }

    //이메일을 전송합니다.
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
        redisUtil.setDataExpire(Integer.toString(authNumber),toMail,60*5L);
    }

    public boolean checkAuthNum(String email, String authNum, EmailAuthPurpose purpose) {
        // 인증 번호 유효성 확인
        String savedEmail = redisUtil.getData(authNum);
        if (savedEmail == null || !savedEmail.equals(email)) {
            return false;
        }

        // 용도에 맞는 TEMP 키 확인
        String tempKey = purpose.getTempKey(email);
        if (redisUtil.getData(tempKey) == null) {
            throw new TempUserNotFoundException(ErrorCode.EMAIL_NOT_REGISTERED_TEMP);
        }

        // 인증 완료 상태 저장 (30분간 유지)
        String verifiedKey = purpose.getVerifiedKey(email);
        redisUtil.setDataExpire(verifiedKey, "true", 60 * 30L);

        // 인증번호는 일회성이므로 삭제
        redisUtil.deleteData(authNum);

        return true;
    }

    public void sendCheckEmail(String email, EmailAuthPurpose purpose) {
        makeRandomNumber();

        String setFrom = "leessjjgg123@gmail.com";
        String toMail = email;
        String title = "[DevPath] 회원 인증 이메일입니다.";
        String content =
                "<b>DevPath</b>에서 발송한 회원 인증용 메일입니다." +
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "인증번호를 정확히 입력해주세요.";

        mailSend(setFrom, toMail, title, content);

        // 목적에 따라 TEMP, VERIFIED 키 구분하여 저장
        redisUtil.setDataExpire(purpose.getTempKey(email), email, 60 * 10L);
        redisUtil.setDataExpire(purpose.getVerifiedKey(email), "true", 60 * 10L);
    }
}

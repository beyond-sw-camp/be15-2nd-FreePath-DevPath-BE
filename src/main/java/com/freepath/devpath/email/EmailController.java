package com.freepath.devpath.email;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

//    @PostMapping("/email/send")
//    public String mailSend(@RequestBody @Valid EmailRequestDto emailDto){
//        System.out.println("이메일 인증 이메일 :"+emailDto.getEmail());
//        return emailService.joinEmail(emailDto.getEmail());
//    }

    @PostMapping("/email/check")
    public String AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto){
        Boolean Checked = emailService.checkAuthNum(emailCheckDto.getEmail(),emailCheckDto.getAuthNum());
        if(Checked){
            return "ok";
        }
        else{
            throw new NullPointerException("뭔가 잘못!");
        }
    }
}
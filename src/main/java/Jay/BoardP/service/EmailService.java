package Jay.BoardP.service;


import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    public final JavaMailSender javaMailSender;

    public String mailCheck(String email) {

        Random random = new Random();
        String key = "";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);

        for (int i = 0; i < 3; i++) {
            int index = random.nextInt(25)+65;
            key += (char) index;
        }

        int numIndex = random.nextInt(9999)+1000 ;

        key += numIndex;
        message.setSubject("회원가입 인증번호 입력을 위한 메일 전송");
        message.setText("인증번호 : " + key);

        javaMailSender.send(message);
        return key;
    }

}

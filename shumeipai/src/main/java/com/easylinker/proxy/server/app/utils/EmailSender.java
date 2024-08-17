package com.easylinker.proxy.server.app.utils;

import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.Base64;
import java.util.Date;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String mailUsername;


    public void sendTextMail(AppUser appUser) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setSentDate(new Date());
        message.setTo(appUser.getEmail());
        message.setSubject("[激活账户]");
        message.setText("欢迎注册!\n请点击下方链接激活账户:http://localhost/user/active/" + Base64.getEncoder().encodeToString(appUser.getUsername().getBytes()));
        mailSender.send(message);

    }

    public void sendHtmlMail(AppUser appUser) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailUsername);
            helper.setTo(appUser.getEmail());
            helper.setSubject("激活账户");

            StringBuffer sb = new StringBuffer();
            sb.append("<h1>激活账户</h1>")
                    .append("<div>")
                    .append("<p>账户注册成功,请激活账户!</p>")
                    .append("<p><a href=\"http://localhost/api/user/activeUser/" + Base64.getEncoder().encodeToString(appUser.getUsername().getBytes()) + "\">请点击链接激活账户</a></div>")
                    .append("</div>");
            helper.setText(sb.toString(), true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendToGroup() {

    }

//    public static void main(String[] args) {
//        StringBuffer sb = new StringBuffer();
//        System.out.println(
//                sb.append("<h1>激活账户</h1>")
//                        .append("<div>")
//                        .append("<p>账户注册成功,请激活账户!</p>")
//                        //http://localhost/user/activeUser/" + Base64.getEncoder().encodeToString(appUser.getId().toString().getBytes())
//                        .append("<p><a href=\"http://localhost/user/activeUser/" + Base64.getEncoder().encodeToString("424235423423".getBytes()) + "\">请点击链接激活账户</a></div>")
//                        .append("</div>"));
//    }
}

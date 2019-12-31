package com.meeting.meeting;

import com.meeting.meeting.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@SpringBootTest
class MeetingApplicationTests {

    @Resource
    private MeetingService meetingService;

    @Test
    void contextLoads() {
    }

    @Test
    public void test2() {
        String to = "xxx@qq.com";
        String subject = "今晚要加班，不用等我了";
        String content = "<html><body>测试邮件哈</body></html>";
        try {
            meetingService.sendHtmlMail(to, subject, content);
            System.out.println("成功了");
        } catch (MessagingException e) {
            System.out.println("失败了");
            e.printStackTrace();
        }
    }

}

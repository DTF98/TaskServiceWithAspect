package ru.DTF98.TaskServiceWithAspect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import ru.DTF98.TaskServiceWithAspect.model.Task;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender sender;
    @Value("${mail.to}")
    private String to;

    public void sendEmail(Task task) {
        String body = "Task " + task.toString() + " has been updated";
        String subject = "";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        StopWatch timeOfSend = new StopWatch("Debug");
        timeOfSend.start();
        sender.send(mailMessage);
        timeOfSend.stop();
        log.error(timeOfSend.toString());
        log.info("Send Email with updated task = {}", task);
    }
}

package com.sched.sched.infrastructure.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sched.sched.core.dtos.ActivityDto;
import com.sched.sched.core.dtos.HabitDto;
import com.sched.sched.core.mappers.ActivityMapper;
import com.sched.sched.core.mappers.HabitMapper;
import com.sched.sched.core.models.UserModel;
import com.sched.sched.core.repos.IUserRepo;
import com.sched.sched.core.services.IEmailService;
import com.sched.sched.infrastructure.repos.ActivityRepo;
import com.sched.sched.infrastructure.repos.HabitRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@EnableAsync
public class UserEmailService implements IEmailService{

    @Autowired
    ActivityRepo activityRepo;
    @Autowired
    HabitRepo habitRepo;
    @Autowired
    IUserRepo userRepo;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    Environment enviroment;

    Logger logger = LoggerFactory.getLogger(UserEmailService.class);

    @Override
    @Async
    @Scheduled(cron = "0 0 0")
    // отправка пользователям, у которых есть привычки на день, информацию о привычках, которые надо сделать
    public CompletableFuture<Void> sendAllActiveHabits() {

        return CompletableFuture.supplyAsync(() -> {
            Date date = new Date();
            int pageSize = 30;
            int pageCount = userRepo.getUsersPageCount(pageSize, false, date);

            
            DateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

            String subject = "Список привычек на сегодня("+formater.format(date)+")";
            logger.info("Send all habits for date" + formater.format(date));

            for(int i = 0; i < pageCount;i++){

                
                List<UserModel> users = new ArrayList<>(userRepo.getUsersModelsWithDateHabitByPage(date, i, pageSize));
                date.getTime();
                for(UserModel model : users){
                    String content = model.getHabits().toString()
                                    .replaceAll("[", "")
                                    .replaceAll("]", "")
                                    .replaceAll(",", "\n");
                    String toAdress = model.getEmail();

                    try {
                        sendEmail(toAdress, subject, content);
                    } 
                    catch (MessagingException e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            // либо раскидать на несколько доп потоков, либо найти какой-нибудь алгоритм, что будет умнее чем просто цикл for
            // Можно в for вызывать compleatlbe future
            // Можно сделать запросы в бд по малому колличеству записей(край 200), пока не будет ни одно записи в список 
            

            return null;
        });
    }
    @Override
    @Async
    @Scheduled(cron = "0 0 0")
    // отправка пользователям, у которых есть активности на день, информацию об активностях на день
    public CompletableFuture<Void> sendAllActivities() {
        return CompletableFuture.supplyAsync(() -> {
            Date date = new Date();
            int pageSize = 30;
            int pageCount = userRepo.getUsersPageCount(pageSize, true, date);

            
            DateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

            String subject = "Список активностей на сегодня("+formater.format(date)+")";
            logger.info("send all activities for date" + formater.format(date));

            for(int i = 0; i < pageCount;i++){

                
                List<UserModel> users = new ArrayList<UserModel>(userRepo.getUsersModelsWithDateActivityByPage(date, i, pageSize));
                date.getTime();
                for(UserModel model : users){
                    String content = model.getActivities().toString()
                                    .replaceAll("[", "")
                                    .replaceAll("]", "")
                                    .replaceAll(",", "\n");
                    String toAdress = model.getEmail();

                    try {
                        sendEmail(toAdress, subject, content);
                    } 
                    catch (MessagingException e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            // либо раскидать на несколько доп потоков, либо найти какой-нибудь алгоритм, что будет умнее чем просто цикл for
            // Можно в for вызывать compleatlbe future
            // Можно сделать запросы в бд по малому колличеству записей(край 200), пока не будет ни одно записи в список 
            

            return null;
        });
    }


    @Override
    @Async
    public CompletableFuture<Void> sendActivitiesByUserId(UUID userId) {
        // throw new UnsupportedOperationException("Unimplemented method 'sendActivitiesByUserId'");
        return CompletableFuture.supplyAsync(() -> {
            
            List<ActivityDto> activities = activityRepo.getAllAcctivitiesByUserId(userId).stream()
                                            .map(ActivityMapper::asDto)
                                            .collect(Collectors.toList());
            
            String userEmail = userRepo.getUsersEmailByUserId(userId);

            String forSend = activities.toString();

            try {
                sendEmail(userEmail, "Все активности", forSend);
            } catch (MessagingException e) {
                e.printStackTrace();
                logger.error(e.getLocalizedMessage(), e);
            }

            return null;
        });
    }
    @Override
    @Async
    public CompletableFuture<Void> sendActivitiesByUserId(UUID userId, Date date) {
        return CompletableFuture.supplyAsync(() -> {
            
            List<ActivityDto> activities = activityRepo.getAllAcctivitiesByUserId(userId, date)
                                            .stream()
                                            .map(ActivityMapper::asDto)
                                            .collect(Collectors.toList());
            //
            String userEmail = userRepo.getUsersEmailByUserId(userId);
            String forSend = activities.toString();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            String subject = "Активности на дату " + format.format(date);

            try {
                sendEmail(userEmail, subject, forSend);
            } catch (MessagingException e) {
                e.printStackTrace();
                logger.error(e.getLocalizedMessage(), e);
            }
            return null;
        });
    }
    

    @Override
    @Async
    public CompletableFuture<Void> sendHabitsByUserId(UUID userId) {
        return CompletableFuture.supplyAsync(() -> {
            
            List<HabitDto> activities = habitRepo.getHabitsByUserId(userId)
                                            .stream()
                                            .map(HabitMapper::asDto)
                                            .collect(Collectors.toList());
            //
            String userEmail = userRepo.getUsersEmailByUserId(userId);
            String forSend = activities.toString();


            String subject = "Привычки";

            try {
                sendEmail(userEmail, subject, forSend);
            } catch (MessagingException e) {
                e.printStackTrace();
                logger.error(e.getLocalizedMessage(), e);
            }
            return null;
        });
    }
    @Override
    @Async
    public CompletableFuture<Void> sendHabitsByUserId(UUID userId, Date date) {
        return CompletableFuture.supplyAsync(() -> {
            
            List<HabitDto> activities = habitRepo.getHabitsByDay(date, userId)
                                            .stream()
                                            .map(HabitMapper::asDto)
                                            .collect(Collectors.toList());
            //
            String userEmail = userRepo.getUsersEmailByUserId(userId);
            String forSend = activities.toString();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            String subject = "Привычки на дату " + format.format(date);

            try {
                sendEmail(userEmail, subject, forSend);
            } catch (MessagingException e) {
                e.printStackTrace();
                logger.error(e.getLocalizedMessage(), e);
            }
            return null;
        });
    }
    
    private void sendEmail(String toAdress,
                            String subject,
                            String content) throws MessagingException{
        
        //
        MimeMessage message = mailSender.createMimeMessage();
        
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        helper.setSubject(subject);
        helper.setTo(toAdress);

        helper.setText(content);

        helper.setFrom(content);

        mailSender.send(message);
    }
}

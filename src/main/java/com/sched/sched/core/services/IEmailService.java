package com.sched.sched.core.services;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IEmailService {
    
    public CompletableFuture<Void> sendAllActiveHabits();
    public CompletableFuture<Void> sendAllActivities();

    //блок с методами для отправки активностей по юзеру
    public CompletableFuture<Void> sendActivitiesByUserId(UUID userId);// отправка всех автивносте, которые были и будут
    public CompletableFuture<Void> sendActivitiesByUserId(UUID userId, Date date); // отправка всех активностей в определнную дату 
    
    // Методы для отправки привычек по юзеру
    public CompletableFuture<Void> sendHabitsByUserId(UUID userId);
    public CompletableFuture<Void> sendHabitsByUserId(UUID userId, Date date);
}

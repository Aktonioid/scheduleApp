package com.sched.sched.core.repos;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.sched.sched.core.models.Activity;

public interface IActivityRepo {
    
    public List<Activity> getAllAcctivitiesByUserId(UUID userId); // получение всех активностей по id пользователя
    public List<Activity> getAllAcctivitiesByUserId(UUID userId, Date date);// получение всех активностей пользователя по id пользователя и дате на которую надо искать
    public Activity getActivityById(UUID activityId); // получение активности по id
    public List<Activity> getActivitiesByDay(Date date); // получение активностей, которые назначены в конкретный день
    public boolean updateActivity(Activity activity); // обновление активности
    public boolean createActivity(Activity activity); // создание активности
    public boolean updateActivityStatus(UUID activityId); // обновление статуса активности по id
    public boolean deleteActivityById(UUID activityId);
    public boolean isActivityExhistsById(UUID activityId);
    // public Activity getActivityB
}

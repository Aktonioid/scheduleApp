package com.sched.sched.core.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.sched.sched.core.dtos.ActivityDto;
import com.sched.sched.core.dtos.ActivityStatus;

public interface IActivityService {
    /**
     * Создание модели активности в бд
     * @param activity - модель ActivityDto
     * @return true - активность в бд создалась и сохранилась. False - произошла ошибка при сохранении
     */
    public CompletableFuture<ActivityStatus> createActivity(ActivityDto activity);
    /**
     * Обновление модели активности в бд
     * @param activity - модель ActivityDto
     * @return true - активность в бд обовилась и сохранилась. False - произошла ошибка при обновлении
     */
    public CompletableFuture<ActivityStatus> updateActivity(ActivityDto activity);
    /**
     * Удаление модели активности в бд
     * @return true - активность в бд удалилась и статус бд обновился. False - произошла ошибка при удалении
     */
    public CompletableFuture<ActivityStatus> deleteActivityById(UUID activityId);
    /**
     * Получение вообще всех активаностей, которые есть у пользователя
     * @param userId - id пользователя
     * @return Возвращает список всех активностей пользователя внезависимости от даты их проведения. Список может быть null
     */
    public CompletableFuture<List<ActivityDto>> getAllActivitiesByUserId(UUID userId);
    /**
     * Получение активностей пользователя в определенный день
     * @param userId - id пользователя по которому нужно искать объекты
     * @param activityDate - дата на которую надо искать активности
     * @return Возвращает список всех активностей, список может быть null
     */
    public CompletableFuture<List<ActivityDto>> getAllActivitiesByUserId(UUID userId, Date activityDate);
    /**
     * Получение конкртной активности по id
     * @param id - id активности
     * @return Возвращяет Activity, но если в бд нет записи с таким id, то возвращает null 
     */
    public CompletableFuture<ActivityDto> getActivityById(UUID id);

    /**
     * @param id - id активности
     * @return Возвращает true, если статус активности удачно обновился, false если обновить статус активности не получилось
     */
    public CompletableFuture<ActivityStatus> updateActivityStatus(UUID id);
}

package com.sched.sched.infrastructure.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.sched.sched.core.dtos.ActivityDto;
import com.sched.sched.core.dtos.ActivityStatus;
import com.sched.sched.core.mappers.ActivityMapper;
import com.sched.sched.core.models.Activity;
import com.sched.sched.core.repos.IActivityRepo;
import com.sched.sched.core.services.IActivityService;

@Service
@EnableAsync
public class ActivityService implements IActivityService{

    @Autowired
    IActivityRepo activityRepo;

    @Override
    @Async
    public CompletableFuture<ActivityStatus> createActivity(ActivityDto activityDto) {
        CompletableFuture<ActivityStatus> result = CompletableFuture.supplyAsync(() ->{

            ActivityStatus status = new ActivityStatus(false, false, false,false,false,false);

            // проверка на то существуют ли нужные для создания поля
            if(activityDto.getActivityDate() != null){
                status.setActivityDate(true);
            }
            if(activityDto.getActivityDescription() != null){
                status.setActivityDescription(true);
            }
            if(activityDto.getActivityLocation() != null){
                status.setActivityLocation(true);
            }
            if(activityDto.getActivityName() != null){
                status.setActivityName(true);
            }
            if(activityDto.getActivityTime() != null){
                status.setActivityTime(true);
            }

            // Если хотя бы одно поле пропущено, отправляеет инфу о том, что каких полей нет
            if(!status.isActivityDate() || 
                !status.isActivityDescription() ||
                !status.isActivityLocation() ||
                !status.isActivityName() ||
                !status.isActivityTime() ){

                return status;
            }

            Activity activity = ActivityMapper.asEntity(activityDto);

            activityRepo.createActivity(activity);
            return status; 
        });

        return result;
    }
    

    @Override
    @Async
    public CompletableFuture<ActivityStatus> updateActivity(ActivityDto activityDto) {
        return CompletableFuture.supplyAsync(() -> {
            
            if(activityDto.getId() == null){
                return new ActivityStatus();
            }

            if(!activityRepo.isActivityExhistsById(activityDto.getId())){
                return new ActivityStatus(false);
            }

            ActivityStatus status = new ActivityStatus(false, false, false,false,false,false);

            // проверка на то существуют ли нужные для обновления поля
            if(activityDto.getActivityDate() != null){
                status.setActivityDate(true);
            }
            if(activityDto.getActivityDescription() != null){
                status.setActivityDescription(true);
            }
            if(activityDto.getActivityLocation() != null){
                status.setActivityLocation(true);
            }
            if(activityDto.getActivityName() != null){
                status.setActivityName(true);
            }
            if(activityDto.getActivityTime() != null){
                status.setActivityTime(true);
            }

            // Если хотя бы одно поле пропущено, отправляеет инфу о том, что каких полей нет
            if(!status.isActivityDate() || 
                !status.isActivityDescription() ||
                !status.isActivityLocation() ||
                !status.isActivityName() ||
                !status.isActivityTime() || 
                !status.isIdId()){

                return status;
            }

            Activity activity = ActivityMapper.asEntity(activityDto);
            
            activityRepo.updateActivity(activity);
            return status;
        });
    }

    @Override
    @Async
    public CompletableFuture<ActivityStatus> deleteActivityById(UUID activityId) {
        return CompletableFuture.supplyAsync(() -> {

            ActivityStatus status = new ActivityStatus(true);

            if(!activityRepo.isActivityExhistsById(activityId)){
                status.setActivityExhist(false);
                return status;
            }

            activityRepo.deleteActivityById(activityId);

            return status;

        });
    }

    @Override
    @Async
    public CompletableFuture<List<ActivityDto>> getAllActivitiesByUserId(UUID userId) {
    
        return CompletableFuture.supplyAsync(() ->{
            
            List<ActivityDto> activities = activityRepo.getAllAcctivitiesByUserId(userId)
                .stream()
                .map(ActivityMapper::asDto)
                .collect(Collectors.toList());

            return activities;
        });
    }


    @Override
    @Async
    public CompletableFuture<List<ActivityDto>> getAllActivitiesByUserId(UUID userId, Date activityDate) {
        return CompletableFuture.supplyAsync(() -> {
            
            List<ActivityDto> activities = activityRepo.getAllAcctivitiesByUserId(userId, activityDate)
                .stream()
                .map(ActivityMapper::asDto)
                .collect(Collectors.toList());
            
            return activities;
        });
    }

    @Override
    @Async
    public CompletableFuture<ActivityDto> getActivityById(UUID id) {

        return CompletableFuture.supplyAsync(() -> {
            return ActivityMapper.asDto(activityRepo.getActivityById(id));
        });
    }

    @Override
    @Async
    public CompletableFuture<ActivityStatus> updateActivityStatus(UUID id) {
        return CompletableFuture.supplyAsync(() -> {

            // проверка на то существует ли активность
            if(!activityRepo.isActivityExhistsById(id)){
                return new ActivityStatus(false);
            }
 
            activityRepo.updateActivityStatus(id);
            return new ActivityStatus(true);
        });
    }
    
    
}

package com.sched.sched.infrastructure.contollers;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sched.sched.core.dtos.ActivityDto;
import com.sched.sched.core.dtos.ActivityStatus;
import com.sched.sched.core.services.IActivityService;


@RestController
@RequestMapping("activities")
public class ActivityController {
    
    @Autowired
    IActivityService activityService;

    Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @PostMapping("/")
    public ResponseEntity<ActivityStatus> createActivity(@RequestBody ActivityDto activityDto){

        ActivityStatus status = null;

        try {
            status = activityService.createActivity(activityDto).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }

        // проверка на то все ли необходимые поля пришли, а если нет, то каких не хватает
        if(!status.isActivityDate()||
            !status.isActivityDescription()||
            !status.isActivityLocation() ||
            !status.isActivityName() ||
            !status.isActivityTime()){
                return ResponseEntity.badRequest().body(status);
            }

        return ResponseEntity.ok(null);
    } 

    @PutMapping("/")
    public ResponseEntity<ActivityStatus> updateActivity(@RequestBody ActivityDto activityDto){

        ActivityStatus status = null;

        try {
            status = activityService.updateActivity(activityDto).get();

        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }

        if(!status.isActivityExhist()){
            return ResponseEntity.notFound().build();
        }

        if(!status.isActivityDate()||
            !status.isActivityDescription()||
            !status.isActivityLocation() ||
            !status.isActivityName() ||
            !status.isActivityTime() ||
            !status.isIdId()){
                return ResponseEntity.badRequest().body(status);
            }

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteActivityById(@PathVariable UUID id){

        ActivityStatus status = null;

        try {
            status = activityService.deleteActivityById(id).get();


        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }

        if(!status.isActivityExhist()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityStatus> updateActivityStatusById(@PathVariable UUID id){

        ActivityStatus status = null;

        try {
            status = activityService.updateActivityStatus(id).get();

        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }

        if(!status.isActivityExhist()){
            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDto> getActivityById(@PathVariable UUID id){

        ActivityDto activityDto = null;

        try {
            activityDto = activityService.getActivityById(id).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }

        if(activityDto == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(activityDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ActivityDto>> getAllActivitiesByUserId(UUID userId){
        List<ActivityDto> activities = null;

        try {
            activities = activityService.getAllActivitiesByUserId(userId).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
        
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/allDate")
    public ResponseEntity<List<ActivityDto>> getAllActivitiesByUserId(long timestamp, UUID userId){
        
        Date date = new Date(timestamp);

        List<ActivityDto> activities = null;
        
        try {
            activities = activityService.getAllActivitiesByUserId(userId, date).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }

        return ResponseEntity.ok(activities);
    }
}

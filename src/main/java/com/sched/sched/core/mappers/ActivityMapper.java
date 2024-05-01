package com.sched.sched.core.mappers;

import java.time.LocalTime;

import com.sched.sched.core.dtos.ActivityDto;
import com.sched.sched.core.models.Activity;
import com.sched.sched.core.models.UserModel;

public class ActivityMapper {
    public static ActivityDto asDto(Activity model)
    {
        
        return new ActivityDto(
            model.getId(),
            model.getActivityName(),
            model.getActivityDescription(),
            model.getActivityLocation(),
            model.getActivityTime(),
            model.getUser().getId(),
            model.isActivityCompleted(),
            model.getActivityDate()
        );
    }

    public static Activity asEntity(ActivityDto dto)
    {
        return new Activity(
            dto.getId(),
            dto.getActivityName(),
            dto.getActivityDescription(),
            dto.getActivityLocation(),
            dto.getActivityTime(),
            new UserModel(dto.getUser()),
            dto.isActivityCompleted(),
            dto.getActivityDate()
        );
    }
}

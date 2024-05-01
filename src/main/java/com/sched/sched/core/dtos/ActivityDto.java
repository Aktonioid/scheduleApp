package com.sched.sched.core.dtos;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto {
    
    private UUID id;
    private String activityName; //название мероприяьния
    private String activityDescription; // описание мероприятия
    private String activityLocation; // локация где мероприятие проводиться        
    @JsonFormat(pattern = "HH:mm:ss", shape = Shape.STRING)
    private LocalTime activityTime; // время когда мероприятие проводиться
    private UUID user;
    private boolean isActivityCompleted; // Бывал ли пользователь на мероприятии
    private Date activityDate;

    // кривой вид для теста
    @Override
    public String toString() {
        return "ActivityDto [activityName=" + activityName + ", activityDescription=" + activityDescription
                + ", activityLocation=" + activityLocation + ", activityTime=" + activityTime + ", isActivityCompleted="
                + isActivityCompleted + ", activityDate=" + activityDate + "]";
    }

}

package com.sched.sched.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс необходимый для отправки иенформации о том нужно ли дозаполнить какие-либо поля при создании или обновлении активностей в бд. 
 * Содержит тоолько boolean отвечаюищй за то заполнены ли определенные поля отправляемого класса ActivityDto
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityStatus {
    
    private boolean idId;
    private boolean isActivityName;
    private boolean isActivityDescription;
    private boolean isActivityLocation;
    private boolean isActivityTime;
    private boolean isActivityDate;

    private boolean isActivityExhist; 

    public ActivityStatus(boolean idId, boolean isActivityName, boolean isActivityDescription,
            boolean isActivityLocation, boolean isActivityTime, boolean isActivityDate) {
        this.idId = idId;
        this.isActivityName = isActivityName;
        this.isActivityDescription = isActivityDescription;
        this.isActivityLocation = isActivityLocation;
        this.isActivityTime = isActivityTime;
        this.isActivityDate = isActivityDate;
    }

    public ActivityStatus(boolean isActivityExhist){
        this.isActivityExhist = isActivityExhist;
    }
}

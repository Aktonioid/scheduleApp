package com.sched.sched.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс необходимый для отправки иенформации о том нужно ли дозаполнить какие-либо поля при создании или обновлении привычек в бд. 
 * Содержит тоолько boolean отвечаюищй за то заполнены ли определенные поля отправляемого класса HabitDto 
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
 public class HabitStatus {

    // остальные опля класса нам не важны, так как они генерятся на месте

    private boolean isHabitId;
    private boolean isHabitExpiration;
    private boolean isHabitBegining;
    private boolean habitName;
    private boolean habitGoal;

    private boolean isHabitExhist;

    public HabitStatus(boolean isHabitId,boolean isHabitExpiration, boolean isHabitBegining, boolean habitName, boolean habitGoal) {
        this.isHabitId = isHabitId;
        this.isHabitExpiration = isHabitExpiration;
        this.isHabitBegining = isHabitBegining;
        this.habitName = habitName;
        this.habitGoal = habitGoal;
    }

    public HabitStatus(boolean isHabitExhist){
        this.isHabitExhist = isHabitExhist;
    }
}

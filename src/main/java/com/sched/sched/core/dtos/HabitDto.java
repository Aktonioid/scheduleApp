package com.sched.sched.core.dtos;

import java.util.Date;
import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabitDto {
    private UUID id;
    private String habitName;
    private String habitGoal;// описание привычки, которую надо привить
    private int habitDays;// сколько дней привычка будет длиться;
    // пользователь к которому эта привычка относиться
    private UUID user;
    private boolean todaySuccess; // выполнилась ли сегодня привычка
    private Date habitExpirationDate;
    private Date habitBeginingDate;
    // для статы по индивидуальной привычке
    private int successesHabits; // сколько раз пользователь выполнил цель по привычке(за день)
    private int faliures; // сколько раз пользователь не выполнил цели по привычке (за день)

    // кривой вид для тестов
    @Override
    public String toString() {
        return "HabitDto [habitName=" + habitName + ", habitGoal=" + habitGoal + ", habitDays=" + habitDays
                + ", habitExpirationDate=" + habitExpirationDate + ", habitBeginingDate=" + habitBeginingDate
                + ", successesHabits=" + successesHabits + ", faliures=" + faliures + "]";
    }
}

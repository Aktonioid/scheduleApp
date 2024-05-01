package com.sched.sched.core.mappers;


import com.sched.sched.core.dtos.HabitDto;
import com.sched.sched.core.models.Habit;
import com.sched.sched.core.models.UserModel;

public class HabitMapper {
    public static Habit asEntity(HabitDto dto)
    {
        return new Habit(
            dto.getId(),
            dto.getHabitName(),
            dto.getHabitGoal(),
            dto.getHabitDays(),
            new UserModel(dto.getUser()),
            dto.isTodaySuccess(),
            dto.getHabitExpirationDate(),
            dto.getHabitBeginingDate(),
            dto.getSuccessesHabits(),
            dto.getFaliures()
        );
    }

    public static HabitDto asDto(Habit model)
    {
        return new HabitDto(
            model.getId(),
            model.getHabitName(),
            model.getHabitGoal(),
            model.getHabitDays(),
            model.getUser().getId(),
            model.isTodaySuccess(),
            model.getHabitExpirationDate(),
            model.getHabitBeginingDate(),
            model.getSuccessesHabits(), 
            model.getFaliures()
        );
    }
    
}

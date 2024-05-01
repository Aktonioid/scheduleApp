package com.sched.sched.core.repos;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.sched.sched.core.models.Habit;

public interface IHabitRepo 
{
    public List<Habit> getHabitsByUserId(UUID userId); // получение всех привычек, коотрые пользователь хотел приобрести
    public List<Habit> getHabitsByDay(Date date, UUID userId); // получение всех привычек, по дню(просто по календарю)
    public Habit getHabitById(UUID habitId); // получение привычки по id
    public boolean updateHabit(Habit habit); // обновление привычки
    public boolean createHabit(Habit habit); // создание новой привычки
    public boolean updateHabitStatus(UUID habitId); // обновление статуса выполнения привычки в текущий день
    public boolean deleteHabitById(UUID habitId);
    public boolean checkIfAHabitExhistById(UUID habitId);

    // скидывает все привычки, которые попадают в дату и обновляет их информацию о провале привычки на день или о её успехе
    public boolean updateHabitStatusAndStatistics(Date date);

}

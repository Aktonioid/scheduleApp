package com.sched.sched.core.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.sched.sched.core.dtos.HabitDto;
import com.sched.sched.core.dtos.HabitStatus;

public interface IHabitService {
    public CompletableFuture<HabitStatus> createHabit(HabitDto habit);
    public CompletableFuture<HabitStatus> updateHabit(HabitDto habit);
    public CompletableFuture<HabitStatus> deleteHabitById(UUID id);
    public CompletableFuture<HabitStatus> updateHAbitStatusById(UUID habitId);
    public CompletableFuture<Boolean> updateHabitStatusAndStatistics();// обновление статистик привычек и сброс успехов на день(используется только для обновления инфы)
    public CompletableFuture<HabitDto> getHabitById(UUID habitId);
    public CompletableFuture<List<HabitDto>> getAllHabitsByUserId(UUID userID);
    public CompletableFuture<List<HabitDto>> getAllHabitsByUserId(UUID id, Date date);

}

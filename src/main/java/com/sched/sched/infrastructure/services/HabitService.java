package com.sched.sched.infrastructure.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sched.sched.core.dtos.HabitDto;
import com.sched.sched.core.dtos.HabitStatus;
import com.sched.sched.core.mappers.HabitMapper;
import com.sched.sched.core.models.Habit;
import com.sched.sched.core.repos.IHabitRepo;
import com.sched.sched.core.services.IHabitService;

@Service
@EnableAsync
public class HabitService implements IHabitService {

    @Autowired
    IHabitRepo habitRepo;

    @Override
    @Async
    public CompletableFuture<HabitStatus> createHabit(HabitDto habit) {

        CompletableFuture<Habit> mapToModel = CompletableFuture.supplyAsync(() ->
        {
            return HabitMapper.asEntity(habit);
        });

        CompletableFuture<HabitStatus> result = mapToModel.thenApply(s -> {

            HabitStatus status = new HabitStatus(false,false,false,false,false,true);

            if(!habitRepo.checkIfAHabitExhistById(habit.getId())){
                status.setHabitExhist(false);
                return status;
            }

            if(habit.getId() != null){
                status.setHabitId(true);
            }
            if(habit.getHabitGoal() != null){
                status.setHabitGoal(true);
            }
            if(habit.getHabitName() != null){
                status.setHabitName(true);
            }
            if(habit.getHabitExpirationDate() != null){
                status.setHabitExpiration(true);
            }
            if(habit.getHabitBeginingDate() != null){
                status.setHabitBegining(true);
            }

            if(!status.isHabitBegining() ||
                !status.isHabitExpiration() ||
                !status.isHabitGoal() ||
                !status.isHabitName() ||
                !status.isHabitId()){
                    return status;
                }


            habitRepo.createHabit(s);


            return status;
        });
        return result;
    }

    @Override
    @Async
    public CompletableFuture<HabitStatus> updateHabit(HabitDto habit) {
        CompletableFuture<HabitStatus> result = CompletableFuture.supplyAsync(() -> {
            
            HabitStatus status = new HabitStatus(false,false,false,false,false,true);

            if(!habitRepo.checkIfAHabitExhistById(habit.getId())){
                status.setHabitExhist(false);
                return status;
            }

            if(habit.getId() != null){
                status.setHabitId(true);
            }
            if(habit.getHabitGoal() != null){
                status.setHabitGoal(true);
            }
            if(habit.getHabitName() != null){
                status.setHabitName(true);
            }
            if(habit.getHabitExpirationDate() != null){
                status.setHabitExpiration(true);
            }
            if(habit.getHabitBeginingDate() != null){
                status.setHabitBegining(true);
            }

            if(!status.isHabitBegining() ||
                !status.isHabitExpiration() ||
                !status.isHabitGoal() ||
                !status.isHabitName() ||
                !status.isHabitId()){
                    return status;
                }

            Habit hab = HabitMapper.asEntity(habit);

            habitRepo.updateHabit(hab);

            return status;
        });
        return result;
    }

    @Override
    @Async
    public CompletableFuture<HabitStatus> deleteHabitById(UUID id) {
        return CompletableFuture.supplyAsync(() -> {

            HabitStatus status = new HabitStatus(true);

            if(!habitRepo.checkIfAHabitExhistById(id)){
                status.setHabitExhist(false);
                return status;
            }

            habitRepo.deleteHabitById(id);

            return status;
        });
    }

    @Override
    @Async
    public CompletableFuture<HabitStatus> updateHAbitStatusById(UUID habitId) {

        return CompletableFuture.supplyAsync(() -> {
            HabitStatus status = new HabitStatus(true);

            if(!habitRepo.checkIfAHabitExhistById(habitId)){
                status.setHabitExhist(false);
                return status;
            }

            habitRepo.updateHabitStatus(habitId);

            return status;
        });
    }

    @Override
    @Async
    @Scheduled(cron = "0 0 0")
    public CompletableFuture<Boolean> updateHabitStatusAndStatistics() {
        // TODO придумать как сдеать код точно выполняемым, даже если он один-два раза выкинет  false или exception
        return CompletableFuture.supplyAsync(() ->{
            return habitRepo.updateHabitStatusAndStatistics(new Date());
        });
    }

    @Override
    @Async
    public CompletableFuture<HabitDto> getHabitById(UUID habitId) {
        return CompletableFuture.supplyAsync(() -> {
            
            return HabitMapper.asDto(habitRepo.getHabitById(habitId));
        });
    }
    

    @Override
    @Async
    public CompletableFuture<List<HabitDto>> getAllHabitsByUserId(UUID userID) {
        return CompletableFuture.supplyAsync(() ->{
            
            List<HabitDto> habits = habitRepo.getHabitsByUserId(userID).stream()
                .map(HabitMapper::asDto)
                .collect(Collectors.toList());

            return habits;
        });
    }
    

    @Override
    @Async
    public CompletableFuture<List<HabitDto>> getAllHabitsByUserId(UUID id, Date date) {
        return CompletableFuture.supplyAsync(() ->{
            
            List<HabitDto> habits = habitRepo.getHabitsByDay(date, id).stream().
                map(HabitMapper::asDto).collect(Collectors.toList());
            
            return habits;
        });
    }

    
}

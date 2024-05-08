package com.sched.sched.infrastructure.contollers;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sched.sched.core.dtos.HabitDto;
import com.sched.sched.core.dtos.HabitStatus;
import com.sched.sched.core.services.IHabitService;


@RestController
@RequestMapping("habits")
public class HabitController {
    
    @Autowired
    IHabitService habitService;

    Logger logger = LoggerFactory.getLogger(HabitController.class);

    @PostMapping("/")
    public ResponseEntity<HabitStatus> createHabit(@RequestBody HabitDto habit){
        HabitStatus status = null;

        try {
            status = habitService.createHabit(habit).get();

        } catch (InterruptedException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);

        } catch (ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }


        if(!status.isHabitBegining() ||
            !status.isHabitExpiration()||
            !status.isHabitGoal() ||
            !status.isHabitId() ||
            !status.isHabitName()){
            
            return ResponseEntity.badRequest().body(status);
        }
        
        return ResponseEntity.ok(null);
    }


    @PutMapping("/")
    public ResponseEntity<HabitStatus> updateHabit(@RequestBody HabitDto habitDto){

        HabitStatus status = null;

        try {
            status = habitService.updateHabit(habitDto).get();
        
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }

        if(!status.isHabitExhist()){
            return ResponseEntity.notFound().build();
        }

        if(!status.isHabitBegining() ||
            !status.isHabitExpiration()||
            !status.isHabitGoal() ||
            !status.isHabitId() ||
            !status.isHabitName())
            {
            
            return ResponseEntity.badRequest().body(status);
        }        

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HabitStatus> deleteHabit(@PathVariable UUID id){

        HabitStatus status = null;

        try {
            status = habitService.deleteHabitById(id).get();

        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

        if(!status.isHabitExhist()){
            return ResponseEntity.notFound().build();
        }

        if(!status.isHabitBegining() ||
            !status.isHabitExpiration()||
            !status.isHabitGoal() ||
            !status.isHabitId() ||
            !status.isHabitName())
            {
            
            return ResponseEntity.badRequest().body(status);
        } 

        return ResponseEntity.ok(null);
    }

    @GetMapping("/{habitId}")
    public ResponseEntity<HabitDto> getHabitById(@PathVariable UUID habitId){
        
        HabitDto habitDto = null;

        try {
            habitDto = habitService.getHabitById(habitId).get();

        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

        if(habitDto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return ResponseEntity.ok(null);
    }

    @GetMapping("/allHabits")
    public ResponseEntity<List<HabitDto>> getAllHabitsByUserId(UUID userId){

        List<HabitDto> getHabits = null;

        try {
            getHabits = habitService.getAllHabitsByUserId(userId).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(getHabits);
    }

    @GetMapping("/allHabitsDate")
    public ResponseEntity<List<HabitDto>> getAllHabitsByUserId(UUID userId, long timestamp){
        
        Date date = new Date(timestamp);

        System.out.println(date);

        List<HabitDto> habits = null;

        try {
            habits = habitService.getAllHabitsByUserId(userId, date).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(habits);
    }

    @PutMapping("/updatestatus/true/{habitId}")
    public ResponseEntity<String> updateHabitStatusById(UUID habitId){
        
        HabitStatus status = null;

        try {
            status = habitService.updateHAbitStatusById(habitId).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        if(!status.isHabitExhist()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        return ResponseEntity.ok(null);
    }

    @PostMapping("/updatestatus/false/{habitId}")
    public ResponseEntity<String> setHabitStatusToFasle(@PathVariable UUID haibitId){

        HabitStatus status = null;

        try {
            status = habitService.setHabitStatusToFalse(haibitId).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!status.isHabitExhist()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(null);
    } 

}

package com.sched.sched;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sched.sched.core.models.Habit;
import com.sched.sched.core.models.UserModel;
import com.sched.sched.core.repos.IHabitRepo;

@Tag("UnitTest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = SchedApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
  // locations = "resources",
  locations = {"classpath:application-integrationtest.properties"}
  )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// TODO прописать удаление после каждого создания в бд
public class HabitRepoTests {
  
  @Autowired
  IHabitRepo habitRepo;

  UUID userId = UUID.fromString("50bf9a50-1a9e-4ec2-bc5f-ffbebee250dc");
  UUID habitId = UUID.fromString("7b06d04a-999a-4ccc-afe1-e51c71179af3"); // id для проверки получения из бд
  
  @Test
  @Order(1)
  // сразу же удалить
  public void HabitRepo_createHabit_true(){
    
    UserModel model = new UserModel(userId);

    LocalDate date = LocalDate.now();

    Date expirationDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

    date = date.plusDays(21);

    Date beginingDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());


    Habit habit = new Habit(null, "habitName", "habitGoal", 21, model, false, expirationDate, beginingDate, 0, 0);

    boolean reslut = habitRepo.createHabit(habit);

    habitRepo.deleteHabitById(habit.getId());

    assertEquals(true, reslut);
  }

  @Test
  @Order(2)
  public void HabitRepo_updateHabit_true(){
    UserModel model = new UserModel(userId);

    LocalDate date = LocalDate.now();

    Date beginingDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

    date = date.plusDays(21);

    Date expirationDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

    Habit habit = new Habit(habitId, "updatedHabit", "updatedGoal", 21, model, false, expirationDate, beginingDate, 0, 0);

    boolean result = habitRepo.updateHabit(habit);

    assertEquals(true, result);
  }

  @Test
  @Order(3)
  // так как в репозитории просто получение по id 
  public void HabitRepo_getHabitById(){

    UserModel model = new UserModel(userId);

    LocalDate date = LocalDate.now();
    Date beginingDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

    date = date.plusDays(21);

    Date expirationDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());    

    // TODO переписать habitId на cretatedId
    
    Habit habit = new Habit(null, "habitName", "habitGoal", 21, model, false, expirationDate, beginingDate, 0, 0);

    //создаем и сразу получаем активность(потом удалить сразу)
    habitRepo.createHabit(habit);

    UUID createdHabitId = habit.getId();

    Habit expected = new Habit(createdHabitId, "habitName", "habitGoal", 21, model, false, expirationDate, beginingDate, 0, 0);

    System.out.println("Вывод createdHabitId");
    System.out.println(createdHabitId);
    habit = habitRepo.getHabitById(createdHabitId);

    // мб сделать тест на вариант, что он просто получил инфу из бд? 

    boolean result = false;

    // так как переписывать hashcode и equals ради одной проверки как-то не охота
    if(habit.getId().equals(expected.getId())&&
    (habit.getHabitName().equals(expected.getHabitName()))&&
    (habit.getHabitGoal().equals(expected.getHabitGoal()))&&
    (habit.getHabitDays() == expected.getHabitDays())&&
    (habit.getUser().getId().equals(expected.getUser().getId()))&&
    (habit.isTodaySuccess() == expected.isTodaySuccess())&&
    (habit.getHabitExpirationDate().equals(expected.getHabitExpirationDate()))&&
    (habit.getHabitBeginingDate().equals(expected.getHabitBeginingDate()))&&
    (habit.getSuccessesHabits() == expected.getSuccessesHabits())&&
    (habit.getFailures() == expected.getFailures())
    ){
      result = true;
    }

    habitRepo.deleteHabitById(createdHabitId);
    
    assertEquals(true, result);
  }


  @Test
  // просто прописать обновление статуса привычки, потом получить привычки по id  и проверить статус на true
  public void HabitRepo_updateHabitStatus(){
    habitRepo.updateHabitStatus(habitId);

    Habit habit = habitRepo.getHabitById(habitId);

    assertEquals(true, habit.isTodaySuccess());
  }

  @Test
  public void HabitRepo_updateHabitStatusAndStatistics(){

    LocalDate local = LocalDate.now().plusDays(1);

    Habit habitBeforeUpdate = habitRepo.getHabitById(habitId);

    Date date = Date.from(local.atStartOfDay(ZoneId.systemDefault()).toInstant());

    habitRepo.updateHabitStatusAndStatistics(date);

    System.out.println(date.toString());

    Habit habitAfterUpdate = habitRepo.getHabitById(habitId);

    System.out.println(habitBeforeUpdate.getSuccessesHabits());
    System.out.println(habitAfterUpdate.getSuccessesHabits());

    boolean result = habitAfterUpdate.getSuccessesHabits() > habitBeforeUpdate.getSuccessesHabits(); 

    assertEquals(true, result);
  }

  @Test
  public void HabitRepo_deleteHabitById(){

    UserModel model = new UserModel(userId);

    LocalDate date = LocalDate.now();
    Date beginingDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

    date = date.plusDays(21);

    Date expirationDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());    

    // TODO переписать habitId на cretatedId
    
    Habit habit = new Habit(null, "habitName", "habitGoal", 21, model, false, expirationDate, beginingDate, 0, 0);

    //создаем и сразу получаем активность(потом удалить сразу)
    habitRepo.createHabit(habit);

    UUID createdHabitId = habit.getId();

    boolean result = habitRepo.deleteHabitById(createdHabitId);

    assertEquals(true, result);
  }
}

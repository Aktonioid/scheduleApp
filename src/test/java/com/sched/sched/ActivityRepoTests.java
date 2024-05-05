package com.sched.sched;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sched.sched.core.models.Activity;
import com.sched.sched.core.models.UserModel;
import com.sched.sched.core.repos.IActivityRepo;

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
// подключаю springBootTest чтоб просто не надо было настраивать sessionFactory. Приложение не большое, так что сильной разницы во врермени выполнения быть не должно
// TODO Прописать удаление после каждого создания
public class ActivityRepoTests {
    
    @Autowired
    IActivityRepo activityRepo;

    UUID activityIdForOtherTests = UUID.fromString("64c0caa2-2659-45ae-85a7-ffb66903c3fe");
    UUID userId = UUID.fromString("50bf9a50-1a9e-4ec2-bc5f-ffbebee250dc");
    UUID activityForDelete = null;

    @Test
    public void ActivityRepo_createActivity_true(){

        LocalTime activityTime = LocalTime.of(12, 20, 0);
        Date date = new Date();
        UserModel model = new UserModel(userId);
        Activity activity = new Activity(null, "testActiviyt", "activityDesc", "location", activityTime, model, false, date);

        boolean result = activityRepo.createActivity(activity);

        activityForDelete = activity.getId();


        assertEquals(true, result); 
    }

    @Test
    public void ActivityRepo_updateActivity_true(){
        LocalTime time = LocalTime.of(15, 45, 0);
        Date date = new Date(1714424400000L);
        UserModel model = new UserModel(userId);

        Activity activity = new Activity(activityIdForOtherTests, "UpdateAct", "activityDestUpdate", "locationUpd", time,model, false, date);

        boolean result = activityRepo.updateActivity(activity);

        assertEquals(true, result);
    }

    @Test
    public void ActivityRepo_updateActivityStatus_true(){
        boolean result = activityRepo.updateActivityStatus(activityIdForOtherTests);
        assertEquals(true, result);
    }

    @Test
    public void ActivityRepo_activityExhistsById_true(){
        
        boolean result = activityRepo.isActivityExhistsById(activityIdForOtherTests);

        assertEquals(true, result);
    }

    @Test
    public void ActivityRepo_getActivityById(){
        LocalTime time = LocalTime.of(15, 45, 0);
        Date date = new Date(1714424400000L);
        UserModel model = new UserModel(userId);
        Activity testActivity = new Activity(activityIdForOtherTests, "UpdateAct", "activityDestUpdate", "locationUpd", time,model, false, date);

        Activity result = activityRepo.getActivityById(activityIdForOtherTests);

        boolean res = false;

        System.out.println(result.getActivityDate().getTime() +"   " + date.getTime());
        System.out.println(result.getActivityDescription() +"   " + testActivity.getActivityDescription());
        System.out.println(result.getActivityLocation() +"   " + testActivity.getActivityLocation());
        System.out.println(result.getActivityName() +"   " + testActivity.getActivityName());
        System.out.println(result.getId() +"   " + testActivity.getId());
        System.out.println(result.getUser().getId() +"   " + testActivity.getUser().getId());


        if( result.getActivityDate().getTime() == date.getTime() &&
            result.getActivityDescription().equals(testActivity.getActivityDescription())&&
            result.getActivityLocation().equals(testActivity.getActivityLocation()) &&
            result.getActivityName().equals(testActivity.getActivityName()) &&
            result.getId().equals(activityIdForOtherTests) &&
            result.getUser().getId().equals(model.getId()))
        {
            
            res = true; 
        }
        assertEquals(true, res);
    }

    @Test
    public void ActivityRepo_getAllAcctivitiesByUserId(){
        List<Activity> activities = activityRepo.getAllAcctivitiesByUserId(userId);
    
        System.out.println(activities.size());

        boolean result = activities.size() > 0;

        assertEquals(true, result);
    }
    
    @Test
    public void ActivityRepo_deleteActivity(){
        LocalTime activityTime = LocalTime.of(12, 20, 0);
        Date date = new Date();
        UserModel model = new UserModel(userId);
        Activity activity = new Activity(null, "testActiviyt", "activityDesc", "location", activityTime, model, false, date);

        boolean result = activityRepo.createActivity(activity);
        
        result = activityRepo.deleteActivityById(activity.getId());

        assertEquals(true, result);
    }
}

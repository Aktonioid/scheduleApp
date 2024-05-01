package com.sched.sched;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sched.sched.core.dtos.ActivityDto;
import com.sched.sched.core.dtos.ActivityStatus;
import com.sched.sched.core.models.Activity;
import com.sched.sched.core.models.UserModel;
import com.sched.sched.core.repos.IActivityRepo;
import com.sched.sched.core.services.IActivityService;
import com.sched.sched.infrastructure.repos.ActivityRepo;

// @ExtendWith(SpringExtension.class)
// @SpringBootTest(
//   webEnvironment = SpringBootTest.WebEnvironment.MOCK,
//   classes = SchedApplication.class)
// @AutoConfigureMockMvc
// @TestPropertySource(
//   // locations = "resources",
//   locations = {"classpath:application-integrationtest.properties"}
//   )
// @ActiveProfiles(profiles = "test")
@Tag("UnitTest")

public class HibernateTest {

  @Autowired
  IActivityRepo activityRepo;

  @Autowired
  IActivityService activityService;
  
  @Test
  public void test(){
    
    // UserModel model =new UserModel(UUID.fromString("50bf9a50-1a9e-4ec2-bc5f-ffbebee250dc"));
    
    // LocalTime time = LocalTime.of(12, 47, 0);
    
    // long date = 1714338000000l;
    
    // Activity activity = new Activity(null, "testActivity", "testDesc", "Loc", time, model, false, new Date(date));
    
    // boolean result = activityRepo.createActivity(activity);
    
    System.out.println("untis");

    assertEquals(true, true);
  }

  @Test
  public void testingService() throws InterruptedException, ExecutionException{
    // Date date  = new Date(1714338000000l);
    // LocalTime time = LocalTime.of(12, 47, 0);
    // ActivityDto dto = new ActivityDto(null, "name", "name", "name", time, UUID.fromString("50bf9a50-1a9e-4ec2-bc5f-ffbebee250dc"), false, date);
    
    // ActivityStatus status = new ActivityStatus();

    // ActivityStatus result = null;

    // result = activityService.createActivity(dto).get();

    // assertEquals(result.isActivityTime(), result.isActivityTime());
  
    boolean tr = true;
    assertEquals(true, tr);
  }


}

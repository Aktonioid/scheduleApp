package com.sched.sched;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sched.sched.core.models.UserModel;
import com.sched.sched.core.repos.IUserRepo;

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
@TestInstance(Lifecycle.PER_CLASS)
public class UserRepoTests {
  
  @Autowired
  IUserRepo repo;

  // здесь можно заменить на любой другой id, я использу запись с этим id
  UUID userId = UUID.fromString("e9816831-fd5e-4a74-ae83-fc16c30524ac");


  @BeforeAll
  public void createTestUserIfNotExhist(){
    String email = "Email@example.one";

    UserModel model = new UserModel(userId, "username",null,"name","surname",email,true, null, null);  
    
    boolean exhist = repo.checkIsUserExhistByUUID(userId);

    if(exhist){
      return;
    }

    repo.createUser(model);
  }

  @Test
  public void UserRepo_createUser_true(){

    // String dateString = new Date
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    String email = "example@example.one";

    UserModel model = new UserModel(UUID.randomUUID(), "testUsername" + dateFormat.format(date)  ,null,"name","surname",email,true, null, null);  
  
    boolean result = repo.createUser(model);

    repo.deleteUser(model.getId());

    assertEquals(true, result);
  }
  
  @Test
  public void UserRepo_updateUser_true(){
  
    Date date = new Date();
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    
    String email = "Email@example.one";
    
    UserModel model = new UserModel(userId, dateFormat.format(date),null,"updatedName","UpdatedSurname",email,true, null, null);

    boolean result = repo.updateUser(model);
    UserModel modelAfterUpdate = repo.getUserById(userId);
    
    System.out.println(model.getName() +"   " + modelAfterUpdate.getName());

    result = result && modelAfterUpdate.getUsername().equals(dateFormat.format(date));

    assertEquals(true, result);
  }
  
  @Test
  public void UserRepo_deleteUser_true(){
    
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    String email = "example@example.one";

    UserModel model = new UserModel(UUID.randomUUID(), "testUsername" + dateFormat.format(date)  ,null,"name","surname",email,true, null, null);  
  
    boolean result = repo.createUser(model);

    repo.deleteUser(model.getId());

    assertEquals(true, result);
    
  }
  
  @Test
  public void UserRepo_checkIsUserExhistsByEmail(){
    String email = "exampleEmail@example.one";
    boolean result = repo.checkIsUserExhistsByEmail(email);
  
    assertEquals(true, result);
  }
  @Test
  public void UserRepo_checkIsUserExhistsByUsername(){
    boolean result = repo.checkIsUserExhistsByusername("testUsername");
    
    assertEquals(true, result);
  }
  
  @Test
  public void UserRepo_getUserById(){
    String email = "exampleEmail@example.one";
    UUID gettingId = UUID.fromString("069ef853-0a9f-4ef3-8fcd-0a34b90c9d4a");
    
    UserModel model = new UserModel(gettingId, "testUsername",null,"name","surname",email,true, null, null);
    
    UserModel gettedModel = repo.getUserById(gettingId);

    boolean reslut = model.getId().equals(gettedModel.getId()) &&
      model.getName().equals(gettedModel.getName()) &&
      model.getUsername().equals(gettedModel.getUsername()) &&
      model.getSurname().equals(gettedModel.getSurname()) &&
      model.getEmail().equals(gettedModel.getEmail());

    assertEquals(true, reslut);
  }
  
  @Test
  public void UserRepo_getUserByEmail(){

    String email = "exampleEmail@example.one";
    UUID gettingId = UUID.fromString("069ef853-0a9f-4ef3-8fcd-0a34b90c9d4a");
    
    UserModel model = new UserModel(gettingId, "testUsername",null,"name","surname",email,true, null, null);
    
    UserModel gettedModel = repo.getUserByEmail(email);

    boolean reslut = model.getId().equals(gettedModel.getId()) &&
      model.getName().equals(gettedModel.getName()) &&
      model.getUsername().equals(gettedModel.getUsername()) &&
      model.getSurname().equals(gettedModel.getSurname()) &&
      model.getEmail().equals(gettedModel.getEmail());

    assertEquals(true, reslut);
  }
  
  public void UserRepo_getUsersPageCount1(){
  }
  
  public void UserRepo_getUsersPageCount2(){
  }
  
  @Test
  public void UserRepo_getUsersEmailByUserId(){
    String email = "exampleEmail@example.one";
    UUID gettingId = UUID.fromString("069ef853-0a9f-4ef3-8fcd-0a34b90c9d4a");

    String gettedEmail = repo.getUsersEmailByUserId(gettingId);

    assertEquals(email, gettedEmail);
  }

}

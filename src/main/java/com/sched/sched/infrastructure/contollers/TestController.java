package com.sched.sched.infrastructure.contollers;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sched.sched.core.models.UserModel;
import com.sched.sched.core.repos.IUserRepo;

import jakarta.servlet.http.HttpServletRequest;


@RestController
public class TestController {

    @Autowired
    IUserRepo userRepo;

    @PostMapping("/createUser")
    public ResponseEntity<Boolean> createUserModel(@RequestBody UserModel model)
    {
        if(!userRepo.createUser(model))
        {
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok(true);
    }

    @GetMapping("/auth/test")
    public ResponseEntity<String> authTest()
    {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().replaceAll(",", "\n"));
    }


    // @GetMapping("/test")
    public ResponseEntity<String> credentialsTest(HttpServletRequest req , OAuth2AuthenticationToken authenticationToken){
        System.out.println( req.getAuthType());
        System.out.println();
        System.out.println(req.getUserPrincipal());
        System.out.println();
       return ResponseEntity.ok(authenticationToken.toString()); 
    }

    @GetMapping("/test")
    public ResponseEntity<Set<UserModel>> Test(HttpServletRequest req){
        Date date = Date.from(Instant.ofEpochMilli(1714165200000l));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println(format.format(date));

        // userRepo.getUsersPageCount(10, true, date);
        userRepo.getUsersModelsWithDateActivityByPage(date, 1, 10);
        // userRepo.getUsersModelsWithDateHabitByPage(date, 1, 10);


        return ResponseEntity.ok(userRepo.getUsersModelsWithDateActivityByPage(date, 1, 10));
    }
}

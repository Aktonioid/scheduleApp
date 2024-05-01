package com.sched.sched.infrastructure.contollers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sched.sched.core.dtos.UserModelDto;
import com.sched.sched.core.services.IUserService;

@RestController
@RequestMapping("/users")
public class UserContoller {
    
    @Autowired
    IUserService userService;

    @PostMapping("/register")
    // регистрация пользователя при первом входе в сисетму
    public ResponseEntity<UserModelDto> registerByOauth(@AuthenticationPrincipal OAuth2User oAuth2User, 
                                                            @RequestBody UserModelDto user) throws InterruptedException, ExecutionException
    {
        return userService.registerUserByOauth(oAuth2User, user).get();
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserModelDto> getUserByEmail(@PathVariable(name = "email") String email) throws InterruptedException, ExecutionException{
        UserModelDto model = userService.getUserModelByEmail(email).get();

        if(model == null){
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(model);
    }

    //нельзя обновлять почту!
    public ResponseEntity<UserModelDto> updateUserModel(@RequestBody UserModelDto model){
        return ResponseEntity.ok(null);        
    }

    @GetMapping("/oauth_info")
    // метод, необходимый для отправки отправки необходимой инфы о пользователе
    public ResponseEntity<UserModelDto> forRegister(@AuthenticationPrincipal OAuth2User oAuth2User, String authServer) throws InterruptedException, ExecutionException{
        UserModelDto model = new UserModelDto();
      

        if(userService.checkIsUserExhistsByEmail(oAuth2User.getAttribute("email")).get()){
            return new ResponseEntity<>(HttpStatus.CONFLICT); // при попытке зарегать юзера еще раз кидаем конфликт, это значит, что юзер уже зареган
        }
        model.setEmail(oAuth2User.getAttribute("email")); // так как авторизация через гугл, то email не сменить
        model.setName(oAuth2User.getAttribute("given_name")); // это поле можно будет изменить
        model.setSurname(oAuth2User.getAttribute("family_name")); // это тоже должнго быть меняемым

        return ResponseEntity.ok(model);
    }
}

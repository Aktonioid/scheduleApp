package com.sched.sched.infrastructure.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.sched.sched.core.dtos.UserModelDto;
import com.sched.sched.core.mappers.UserModelMapper;
import com.sched.sched.core.models.UserModel;
import com.sched.sched.core.repos.IUserRepo;
import com.sched.sched.core.services.IUserService;

@Service
@EnableAsync
@EnableCaching
public class UserService implements IUserService {

    @Autowired
    IUserRepo userRepo;

    @Override
    @Async
    public CompletableFuture<Boolean> updateUser(UserModelDto user) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() ->
            userRepo.updateUser(UserModelMapper.asEntity(user)));

        return future;
    }

    @Override
    @Async
    public CompletableFuture<Boolean> deleteUserById(UUID userId) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() ->
            userRepo.deleteUser(userId));
        return future;
    }

    @Override
    @Async
    public CompletableFuture<Boolean> checkIsUserExhistsByEmail(String email) {

        return CompletableFuture.supplyAsync(() -> userRepo.checkIsUserExhistsByEmail(email));
    }

    @Override
    @Async
    public CompletableFuture<Boolean> checkIsUserExhistsByusername(String username) {
        return CompletableFuture.supplyAsync(() -> userRepo.checkIsUserExhistsByusername(username));
    }

    @Override
    @Async
    public CompletableFuture<ResponseEntity<UserModelDto>> registerUserByOauth(OAuth2User oauthUser, UserModelDto user) {

        CompletableFuture<ResponseEntity<UserModelDto>> result = CompletableFuture.supplyAsync(() -> {
            
            user.setEmail(oauthUser.getAttribute("email"));
            user.setName(oauthUser.getAttribute("given_name"));
            user.setSurname(oauthUser.getAttribute("family_name"));
            user.setRegisterCompleted(true);
            user.setFaliures(0);
            user.setSuccessesHabits(0);
            user.setPassword("");
            user.setId(UUID.randomUUID());

            if(userRepo.checkIsUserExhistsByusername(user.getUsername())){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            if(!userRepo.createUser(UserModelMapper.asEntity(user))){
                return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
            }
            
            return ResponseEntity.ok(user);
        });
        
        return result;
    }

    @Override
    @Async
    public CompletableFuture<UserModelDto> getUserModelByEmail(String email) {
        
        return CompletableFuture.supplyAsync(() -> {
            return UserModelMapper.asDto(userRepo.getUserByEmail(email));
        });
    }

    @Override
    @Async
    public CompletableFuture<Boolean> updateUserModel(UserModelDto userModel) {

        CompletableFuture<Boolean> result = CompletableFuture.supplyAsync(() ->{
            
            UserModel model = userRepo.getUserByEmail(userModel.getEmail());
            
            if(model == null){
                return false;
            }

            model.setName(userModel.getName());
            model.setSurname(userModel.getSurname());

            return true;
        });

        return result;
    }
    
}
package com.sched.sched.core.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.sched.sched.core.dtos.UserModelDto;

public interface IUserService {
    
    public CompletableFuture<Boolean> updateUser(UserModelDto user);
    public CompletableFuture<Boolean> deleteUserById(UUID userId);
    public CompletableFuture<Boolean> checkIsUserExhistsByEmail(String email);
    public CompletableFuture<Boolean> checkIsUserExhistsByusername(String username);   
    public CompletableFuture<ResponseEntity<UserModelDto>> registerUserByOauth(OAuth2User oauthUser,
                                                                UserModelDto user);
    public CompletableFuture<UserModelDto> getUserModelByEmail(String email);
    public CompletableFuture<Boolean> updateUserModel(UserModelDto userModel);
    
}

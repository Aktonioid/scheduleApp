package com.sched.sched.core.dtos;

import java.util.UUID;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserModelDto {
    private UUID id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private boolean isRegisterCompleted;    

    // общая стата пользователя
    private int successesHabits; // сколько раз пользователь выполнил цель по привычке(за день)
    private int faliures; // сколько раз пользователь не выполнил цели по привычке (за день) 

    private boolean isVerificated;

    public UserModelDto(UUID id, String username, String password, String name, String surname, String email,
                        boolean isRegisterCompleted, boolean isVerificated){

        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname; 
        this.email = email;
        this.isRegisterCompleted = isRegisterCompleted;
        this.isVerificated = isVerificated;
    }
}

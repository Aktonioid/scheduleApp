package com.sched.sched.core.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.hibernate.type.YesNoConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
// @NoArgsConstructor
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "user_table")
public class UserModel implements UserDetails{
    
    @Id
    private UUID id;
    @Column(name = "username", unique = true)
    private String username;
    private String password;//пароль просто рудемент от userDetails и он у всех пустой(¯\_(ツ)_/¯)
    @Column()
    private String name;
    @Column
    private String surname;
    @Column(unique = true)
    private String email;
    @Column(name = "is_register_completed")
    @Convert(converter = YesNoConverter.class)
    private boolean isRegisterCompleted;
    
    // Активности(Ивенты?) которые есть у пользователя(по типу похода к врачу)
    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    // @Column
    private Set<Activity> activities;
    // привычки, ктороыве пользователь хочет привить
    @JsonManagedReference 
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    // @Column
    private Set<Habit> habits;

    // // общая стата пользователя
    // пока что оставлю в комментах, так как идея получать инфу по конкретным пользователям и запихиват её в дто при отправке кажется логичней и избавляет от гемора
    // @Column(name = "successes_habits")
    // private int successesHabits; // сколько раз пользователь выполнил цель по привычке(за день)
    // @Column(name = "failure_habbits")
    // private int faliureHabits; // сколько раз пользователь не выполнил цели по привычке (за день) 
    
    // public UserModel(UUID id, Set<Habit> habits, String email){
    //     this.email = email;
    //     this.id = id;
    //     this.habits = habits;
    // }

    public UserModel(UUID id, String email, Set<Activity> activities){
        this.id = id;
        this.activities = activities;
        this.email = email;
    }

    // public UserModel(UUID id, Set<Activity> activities, String email){
    //     this.id = id;
    //     this.activities = activities;
    //     this.email = email;
    // }
    
    public UserModel(UUID id){
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}

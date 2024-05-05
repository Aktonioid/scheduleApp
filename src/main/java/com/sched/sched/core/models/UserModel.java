package com.sched.sched.core.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.hibernate.type.YesNoConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    // @JoinColumn(name = "activity_id")
    private Set<Activity> activities;
    // привычки, ктороыве пользователь хочет привить 
    @OneToMany(fetch = FetchType.EAGER)
    // @JoinColumn(name="habit_id")
    private Set<Habit> habits;

    // // общая стата пользователя
    // пока что оставлю в комментах, так как идея получать инфу по конкретным пользователям и запихиват её в дто при отправке кажется логичней и избавляет от гемора
    // @Column(name = "successes_habits")
    // private int successesHabits; // сколько раз пользователь выполнил цель по привычке(за день)
    // @Column(name = "failure_habbits")
    // private int faliureHabits; // сколько раз пользователь не выполнил цели по привычке (за день) 
    
    @Column(name = "is_verificated")
    @Convert(converter = YesNoConverter.class)
    private boolean isVerificated;
    
    
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

package com.sched.sched.core.models;

import java.util.Date;
import java.util.UUID;

import org.hibernate.type.YesNoConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "habit")
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name="habit_name")
    private String habitName;
    @Column(name = "habit_goal")
    private String habitGoal;// описание привычки, которую надо привить
    @Column(name="habit_days")
    private int habitDays;// сколько дней привычка будет длиться; в целом можно убрать, и сделать так, чтоб просто на фронте высчитывалось
    // пользователь к которому эта привычка относиться
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
    @Column(name="today_success")
    @Convert(converter = YesNoConverter.class)
    private boolean todaySuccess; // выполнилась ли сегодня привычка
    @Column(name = "habit_expiration_date")
    @Temporal(TemporalType.DATE)
    private Date habitExpirationDate;
    @Column(name = "habit_begining_date")
    @Temporal(TemporalType.DATE)
    private Date habitBeginingDate;
    // для статы по индивидуальной привычке
    @Column(name = "success_habits")
    private int successesHabits; // сколько раз пользователь выполнил цель по привычке(за день)
    @Column()
    private int failures; // сколько раз пользователь не выполнил цели по привычке (за день)
    // надо переписать на более грамотный вид
    @Override
    public String toString() {
        return "habitName=" + habitName +"; habitGoal=" + habitGoal;
    }
}

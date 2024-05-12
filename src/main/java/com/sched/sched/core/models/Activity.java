package com.sched.sched.core.models;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

import org.hibernate.type.YesNoConverter;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activity")
@Entity

public class Activity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "activity_name")
    private String activityName; //название мероприяьния
    @Column(name = "activity_description")
    private String activityDescription; // описание мероприятия
    @Column(name = "activity_location")
    private String activityLocation; // локация где мероприятие проводиться        
    @Column(name = "activity_time")
    private LocalTime activityTime; // время когда мероприятие проводиться
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserModel user;
    @Convert(converter = YesNoConverter.class)
    @Column(name = "is_activity_completed")
    private boolean isActivityCompleted; // Бывал ли пользователь на мероприятии
    @Temporal(TemporalType.DATE)
    @Column(name = "activity_date")
    private Date activityDate;

    // переписываем toString для отправки сообщений пользователям по почте для тестов кривой пока что, потом мб перепишу на поприятнее вид
    @Override
    public String toString() {
        return "activityName=" + activityName + "; activityDescription=" + activityDescription
                + "; activityLocation=" + activityLocation + "; activityTime=" + activityTime + "; activityDate=" + activityDate;
    }
}

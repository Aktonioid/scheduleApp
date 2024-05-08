package com.sched.sched.infrastructure.repos;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sched.sched.core.models.Habit;
import com.sched.sched.core.models.UserModel;
import com.sched.sched.core.repos.IHabitRepo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Repository
public class HabitRepo implements IHabitRepo{

    //TODO переписать получение по дате, так как хуета получется, нормально не отображает

    @Autowired
    SessionFactory sessionFactory;

    Logger logger = LoggerFactory.getLogger(HabitRepo.class);

    @Override
    public List<Habit> getHabitsByUserId(UUID userId) {

        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Habit> cq = cb.createQuery(Habit.class);
        Subquery<UserModel> subquery = cq.subquery(UserModel.class);
        Root<Habit> root = cq.from(Habit.class);
        Root<UserModel> subRoot = subquery.from(UserModel.class);

        subquery.select(subRoot).where(cb.equal(subRoot.get("id"), userId));

        cq.select(root).where(cb.equal(root.get("user"), subquery));
        
        List<Habit> result = session.createQuery(cq).getResultList();

        session.close();
        return Collections.synchronizedList(result);
    }

    @Override // получение всех привычек, по дню(просто по календарю)
    public List<Habit> getHabitsByDay(Date date, UUID userId) {

        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Habit> cq = cb.createQuery(Habit.class);
        Root<Habit> root = cq.from(Habit.class);

        Subquery<UserModel> subquery = cq.subquery(UserModel.class);
        Root<UserModel> subRoot = subquery.from(UserModel.class);

        subquery.select(subRoot).where(cb.equal(subRoot.get("id"), userId));

        cq.select(root)
            .where(cb.and(
                    cb.le(root.get("habitBeginingDate"), date.getTime()),
                    cb.ge(root.get("habitExpirationDate"), date.getTime()),
                    cb.equal(root.get("user"), subquery)));
        
        List<Habit> result = session.createQuery(cq).getResultList();

        session.close();

        return Collections.synchronizedList(result);
    }

    @Override
    public Habit getHabitById(UUID habitId) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Habit> cq = cb.createQuery(Habit.class);
        Root<Habit> root = cq.from(Habit.class);

        cq.select(root).where(root.get("id").in(habitId));

        Habit result = session.createQuery(cq).uniqueResult();
        
        session.close();
        
        return result;
    }

    @Override
    public boolean updateHabit(Habit habit) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaUpdate<Habit> update = cb.createCriteriaUpdate(Habit.class);
        Root<Habit> root = update.from(Habit.class);

        update.where(cb.equal(root.get("id"), habit.getId()));

        update.set(root.get("habitBeginingDate"), habit.getHabitBeginingDate()); // обновить дату начала привычки
        update.set(root.get("habitExpirationDate"), habit.getHabitExpirationDate().getTime()); // обновить дату окончания привычки
        update.set("habitName", habit.getHabitName()); // обновить habitName
        update.set("habitGoal", habit.getHabitGoal()); // обновить habitGoal


        try{
            transaction.begin();
            session.createMutationQuery(update).executeUpdate();            
            transaction.commit();
        }
        catch(HibernateException e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e){// для того чтоб при любой, даже не ожидаемой ошибке был rollback
            transaction.rollback();
            logger.error(e.getMessage(), e);
            session.close();
            throw e;            
        }
        
        session.close();
        return true;
    }

    @Override
    public boolean createHabit(Habit habit) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try{
            transaction.begin();
            session.persist(habit);
            transaction.commit();
        }
        catch(HibernateException e){
            e.printStackTrace();
            transaction.rollback();
            session.close();
            logger.error(e.getMessage(), e);
            return false;
        }
        catch(Exception e){// для того чтоб при любой, даже не ожидаемой ошибке был rollback
            transaction.rollback();
            session.close();
            logger.error(e.getMessage(), e);
            throw e;            
        }
        
        session.close();
        return true;
    }

    @Override
    // для тогото чтобы пользователь ручками ткнул на привычку и завершил ее выполнение на нынешний день
    public boolean updateHabitStatus(UUID habitId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Habit> cu = cb.createCriteriaUpdate(Habit.class); // для обновления успешно выполненных привычек
        Root<Habit> root = cu.from(Habit.class); 


        cu.set(root.get("todaySuccess"), true); // обновление статуса привычки
        // cu.set("successesHabits", "successesHabits+1"); // обновление счетчика привычки

        cu.where(cb.equal(root.get("id"), habitId));

        try{
            transaction.begin();
            session.createMutationQuery(cu).executeUpdate();
            transaction.commit();
        }
        catch(HibernateException e){
            e.printStackTrace();
            transaction.rollback();
            session.close();
            logger.error(e.getMessage(), e);
            return false;
        }
        catch(Exception e){// для того чтоб при любой, даже не ожидаемой ошибке был rollback
            transaction.rollback();
            session.close();
            logger.error(e.getMessage(), e);
            throw e;            
        }
        
        session.close();
        return true;
    }

    @Override
    // обновление статы привычек, и скидывание(если надо) todaySuccess на false
    public boolean updateHabitStatusAndStatistics(Date date) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        // обновление привычек, которые были выполнены
        CriteriaUpdate<Habit> updateSuccessHabits = cb.createCriteriaUpdate(Habit.class);
        // обновляет привычки со статусом false
        CriteriaUpdate<Habit> updateFailedHabits = cb.createCriteriaUpdate(Habit.class);
        
        Root<Habit> successRoot = updateSuccessHabits.from(Habit.class);
        Root<Habit> failuresRoot = updateFailedHabits.from(Habit.class);

        //
        // настройка обновления проваленых привычек
        //  
        // updateFailedHabits.set(failuresRoot.get("failures"), 
        //     cb.sum(failuresRoot.get("failures"), 1));

        updateFailedHabits.set("failures", 
            cb.sum(failuresRoot.get("failures"), 1));

        updateFailedHabits.where(cb.and(
            cb.lessThanOrEqualTo(failuresRoot.<Date>get("habitBeginingDate"), date),
            cb.greaterThanOrEqualTo(failuresRoot.<Date>get("habitExpirationDate"), date),
            cb.equal(failuresRoot.<Boolean>get("todaySuccess"), false)
        ));
        // настройка проваленых привычек закончена

        
        //
        // настройка обновления успешных привычек
        //
        updateSuccessHabits.set("successesHabits", 
            cb.sum(successRoot.get("successesHabits"), 1) );
        
        updateSuccessHabits.set(successRoot.get("todaySuccess"), false);

        updateSuccessHabits.where(cb.and(
            cb.lessThanOrEqualTo(successRoot.<Date>get("habitBeginingDate"), date),
            cb.greaterThanOrEqualTo(successRoot.<Date>get("habitExpirationDate"), date),
            cb.equal(successRoot.get("todaySuccess"), true)
        ));
        //настройка обновления успешных привычек окончена

        try{
            transaction.begin();
            session.createMutationQuery(updateFailedHabits).executeUpdate();
            session.createMutationQuery(updateSuccessHabits).executeUpdate();
            transaction.commit();
        }
        catch(HibernateException e){
            transaction.rollback();
            session.close();
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return false;
        }
        catch(Exception e){
            transaction.rollback();
            session.close();
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw e;
        }
        session.close();

        return true;
    }

    @Override
    public boolean deleteHabitById(UUID habitId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<Habit> delete = cb.createCriteriaDelete(Habit.class);
        Root<Habit> root = delete.from(Habit.class);

        delete.where(cb.equal(root.get("id"), habitId));

        try{
            transaction.begin();
            session.createMutationQuery(delete).executeUpdate();
            transaction.commit();
        }
        catch(HibernateException e){
            transaction.rollback();
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return false;   
        }
        catch(Exception e){
            transaction.rollback();
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            throw e;
        }
        finally{
            session.close();
        }

        // throw new UnsupportedOperationException("Unimplemented method 'deleteHabitById'");
        return true;
    }

    @Override
    public boolean checkIfAHabitExhistById(UUID habitId) {

        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Habit> cq = cb.createQuery(Habit.class);
        Root<Habit> root = cq.from(Habit.class);

        cq.select(root).where(cb.equal(root.get("id"), habitId));

        Habit habit = session.createQuery(cq).uniqueResult();

        session.close();

        if(habit == null){
            return false;
        }

        return true;
    }

    @Override
    public boolean setHabitStatusToFalseById(UUID habitId) {
        // TODO Auto-generated method stub
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Habit> update = cb.createCriteriaUpdate(Habit.class);
        Root<Habit> root = update.from(Habit.class);

        update.set(root.get("todaySuccess"), false);

        update.where(cb.equal(root.get("id"), habitId));

        try{
            transaction.begin();

            session.createMutationQuery(update).executeUpdate();

            transaction.commit();
        }
        catch(HibernateException e){
            transaction.rollback();
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            session.close();
            return false;
        }
        catch(Exception e){
            transaction.rollback();
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            session.close();
            throw e;
        }

        session.close();

        return true;
    }

    
}

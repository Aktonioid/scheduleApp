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

import com.sched.sched.core.models.Activity;
import com.sched.sched.core.models.UserModel;
import com.sched.sched.core.repos.IActivityRepo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Repository
public class ActivityRepo implements IActivityRepo{

    @Autowired
    SessionFactory sessionFactory;
    Logger logger = LoggerFactory.getLogger(HabitRepo.class);

    //TODO переписать получение активностей по юзеру на subquery

    @Override
    public List<Activity> getAllAcctivitiesByUserId(UUID userId) {
        
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Activity> cq = cb.createQuery(Activity.class);
        Root<Activity> root = cq.from(Activity.class);

        Subquery<UserModel> subquery = cq.subquery(UserModel.class);
        Root<UserModel> subRoot = subquery.from(UserModel.class);

        subquery.select(subRoot).where(cb.equal(subRoot.get("id"), userId));

        cq.select(root).where(cb.equal(root.get("user"), subquery));

        List<Activity> activities = session.createQuery(cq).getResultList();

        session.close();
        return Collections.synchronizedList(activities);
    }

    @Override
    public Activity getActivityById(UUID activityId) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Activity> cq = cb.createQuery(Activity.class);
        Root<Activity> root = cq.from(Activity.class);

        cq.select(root).where(root.get("id").in(activityId));

        Activity activity = session.createQuery(cq).uniqueResult();

        session.close();

        return activity;
    }

    @Override
    public List<Activity> getActivitiesByDay(Date date) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Activity> cq = cb.createQuery(Activity.class);
        Root<Activity> root = cq.from(Activity.class);

        cq.select(root).where(root.get("activityDate").in(date));

        List<Activity> activities = session.createQuery(cq).getResultList();

        session.close();
        return activities;
    }

    @Override
    public boolean updateActivity(Activity activity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try{
            transaction.begin();
            session.merge(activity);
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
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw e;
        }

        session.close();
        
        return true;
    }
    

    @Override
    public boolean createActivity(Activity activity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        System.out.println(activity.getUser().getId());

        try{
            transaction.begin();
            session.persist(activity);
            transaction.commit();
        }
        catch(HibernateException e){
            transaction.rollback();
            session.close();
            logger.error(e.getMessage(), e);
            return false;
        }
        catch(Exception e){
            transaction.rollback();
            session.close();
            logger.error(e.getMessage(), e);
            throw e;
        }

        session.close();
        return true;
    }

    @Override
    public boolean updateActivityStatus(UUID activityId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Activity> cu = cb.createCriteriaUpdate(Activity.class);
        Root<Activity> root = cu.from(Activity.class);

        cu.set("isActivityCompleted", true);
        
        cu.where(root.get("id").in(activityId));

        try{
            transaction.begin();
            session.createMutationQuery(cu).executeUpdate();
            transaction.commit();
        }
        catch(HibernateException e){
            transaction.rollback();
            session.close();
            logger.error(e.getMessage(), e);
            e.printStackTrace();
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
    public boolean deleteActivityById(UUID activityId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        CriteriaBuilder cb =session.getCriteriaBuilder();
        CriteriaDelete<Activity> cd = cb.createCriteriaDelete(Activity.class);
        Root<Activity> root = cd.from(Activity.class);
        
        cd.where(root.get("id").in(activityId));

        try{
            transaction.begin();
            session.createMutationQuery(cd).executeUpdate();
            transaction.commit();
        }
        catch(HibernateException e){
            transaction.rollback();
            session.close();
            logger.error(e.getMessage(), e);
            return false;
        }
        catch(Exception e){
            transaction.rollback();
            session.close();
            logger.error(e.getMessage(), e);
            throw e;
        }

        session.close();
        return true;
    }

    @Override
    public List<Activity> getAllAcctivitiesByUserId(UUID userId, Date date) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Activity> cq = cb.createQuery(Activity.class);
        Root<Activity> root = cq.from(Activity.class);

        Subquery<UserModel> subquery = cq.subquery(UserModel.class);
        Root<UserModel> subRoot = subquery.from(UserModel.class);

        subquery.select(subRoot).where(cb.equal(subRoot.get("id"), userId));

        cq.select(root);
        // посик по записям где id пользователя равно userId и activityDate равен date
        cq.where(cb.and(
            cb.equal(subRoot.get("user"), subquery),
            cb.equal(root.<Date>get("activityDate"), date)));

        List<Activity> activities = session.createQuery(cq).getResultList();

        session.close();

        return activities;
    }

    @Override
    public boolean isActivityExhistsById(UUID activityId) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder cb =  session.getCriteriaBuilder();
        CriteriaQuery<Activity> cq = cb.createQuery(Activity.class);
        Root<Activity> root = cq.from(Activity.class);

        cq.select(root).where(cb.equal(root.get("id"), activityId));

        Activity activity = session.createQuery(cq).uniqueResult();

        session.close();

        if(activity == null){
            return false;
        }

        return true;
    }
    
}

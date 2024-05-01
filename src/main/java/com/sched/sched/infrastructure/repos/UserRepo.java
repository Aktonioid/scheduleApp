package com.sched.sched.infrastructure.repos;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sched.sched.core.models.Activity;
import com.sched.sched.core.models.Habit;
import com.sched.sched.core.models.UserModel;
import com.sched.sched.core.repos.IUserRepo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Repository
public class UserRepo implements IUserRepo{

    @Autowired
    SessionFactory sessionFactory;
    // @Autowired
    Logger logger = LoggerFactory.getLogger(UserRepo.class);

    @Override
    public boolean createUser(UserModel model) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        try{
            transaction.begin();
            session.persist(model);
            transaction.commit();
        }
        catch(HibernateException exception)
        {
            exception.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e)
        {
            transaction.rollback();
            session.close();
            throw e;
        }

        session.close();
        
        return true;
    }

    @Override
    public boolean updateUser(UserModel model) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        try{
            transaction.begin();
            session.merge(model);
            transaction.commit();
        }
        catch(HibernateException exception)
        {
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e)
        {
            transaction.rollback();
            session.close();
            throw e;
        }

        session.close();
        
        return true;
    }

    @Override
    public boolean deleteUser(UUID id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<UserModel> cd = cb.createCriteriaDelete(UserModel.class);
        Root<UserModel> root = cd.from(UserModel.class);

        cd.where(root.get("id").in(id));

        try{
            transaction.begin();
            session.createMutationQuery(cd).executeUpdate();
            transaction.commit();
        }
        catch(HibernateException exception)
        {
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e)
        {
            transaction.rollback();
            session.close();
            throw e;
        }

        session.close();
        
        return true;
    }

    @Override
    public List<UserModel> getAllUsersByPage(int page) {
        
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.select(root);

        List<UserModel> result = session.createQuery(cq).getResultList();

        session.close();

        return result;
    }

    @Override
    public boolean checkIsUserExhistsByEmail(String email) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.select(root).where(root.get("email").in(email));

        boolean isExhist = false;

        if(session.createQuery(cq).uniqueResult() != null){
            isExhist = true;
        }

        session.close();
        return isExhist;
    }

    @Override
    public boolean checkIsUserExhistsByusername(String username) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.select(root);

        cq.where(cb.equal(root.get("username"), username));

        boolean isExhist = false;

        if(session.createQuery(cq).uniqueResult() != null){
            isExhist = true;
        }

        session.close();
        return isExhist;
    }

    @Override
    public UserModel getUserById(UUID id) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.select(root).where(root.get("id").in(id));

        UserModel model = session.createQuery(cq).uniqueResult();

        session.close();
        return model;
    }

    @Override
    public UserModel getUserByEmail(String email) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.select(root).where(root.get("email").in(email));

        UserModel model = session.createQuery(cq).uniqueResult();

        session.close();
        return model;
    }

    @Override
    // получение пользователей с активностями на день по странице pageSize присылается при вызове метода
    // page size рекомендуется делать одинковыми для данного метода и метода получения колличества страниц(если используется)
    public List<UserModel> getUsersModelsWithDateActivityByPage(Date date, int page, int pageSize) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);
        
        cq.multiselect(root.get("id"),
                    root.get("email"), 
                    root.get("activities"));


        Subquery<Activity> subQuery = cq.subquery(Activity.class); // subquery для того чтобы получать только тех пользователей,
                                                                    // у котороых есть активности на сегодняшнюю дату

        Root<Activity> subRoot = subQuery.from(Activity.class);

        // subquery для фильтрации запросов по наличию активностей на день
        subQuery.select(subRoot);
        subQuery.where(root.get("activityDate").in(date));
        

        // query для получения пользователей по subquery
        cq.where(root.get("activities").in(subQuery));
        
        Query<UserModel> query = session.createQuery(cq);
        
        query.setFirstResult((page - 1) * pageSize); 
        query.setMaxResults(pageSize);

        List<UserModel> users = query.getResultList();
        
        session.close();
        return users;
    }

    @Override
    // получение пользователей с привычками на день по страницам со страницами размерами страницы равной pageSize( посылается при вызове метода)
    // page size рекомендуется делать одинковыми для данного метода и метода получения колличества страниц(если используется)
    public List<UserModel> getUsersModelsWithDateHabitByPage(Date date, int page, int pageSize) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.multiselect(root.get("id"),
                    root.get("email"), 
                    root.get("activities"));


        Query<UserModel> query = session.createQuery(cq);

        query.setFirstResult((page - 1) * pageSize); 
        query.setMaxResults(pageSize);

        List<UserModel> users = query.getResultList();

        session.close();
        return users;
    }

    @Override
    // получаем сколько пользователей по n страниц можно получить(с условием того что у пользователя есть активности или привычки на сегодня)
    public int   getUsersPageCount(int pageSize, boolean isActivity, Date date) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<UserModel> root = countQuery.from(UserModel.class);


        countQuery.select(cb.count(root));

        // если получение страниц по активностям
        if(isActivity){
            // subquery для получения страниц польщователей у которых есть активности на сегодня
            Subquery<Activity> activityQuery = countQuery.subquery(Activity.class);
            Root<Activity> activityRoot = activityQuery.from(Activity.class);
            
            activityQuery.select(activityRoot);
            activityQuery.where(activityRoot.get("activityDate").in(date));
            
            countQuery.where(root.get("activities").in(activityQuery));
        }
        // Если получение страниц по привычкам
        else{
            // subquery для получения страниц польщователей у которых есть привычки на сегодня
            Subquery<Habit> habitQuery = countQuery.subquery(Habit.class);
            Root<Habit> habitRoot = habitQuery.from(Habit.class);
            
            habitQuery.select(habitRoot);
            habitQuery.where(cb.and(
                cb.ge(root.get("habitBeginingDate"), date.getTime()),  
                cb.le(root.get("habitExpirationDate"), date.getTime())
                // cb.gt(root.get("habitBeginingDate"), date)
            ));
        }

        //Колличество пользователей, подходяших под описание
        long usersCount = session.createQuery(countQuery).uniqueResult();

        session.close();

        // колличество страниц 
        int pageCount = (int)usersCount/pageSize;

        if((int)usersCount%pageSize != 0){
            pageCount++;
        }

        return pageCount;
    }

    @Override
    // получаем сколько пользователей по n страниц можно получить. Вообще всех пользователей
    public int getUsersPageCount(int pageSize) {
        // TODO надо проверить как эта ебала работает просто я как-то не уверен, что оно будет корректно получать инфу
        Session session = sessionFactory.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        // Root<UserModel> root = countQuery.from(UserModel.class);


        long usersCount = session.createQuery(countQuery).uniqueResult();


        session.close();

        int pageCount = (int)(usersCount/pageSize);

        if(usersCount%pageSize != 0){
            pageCount++;
        }

        return pageCount;
    }

    @Override
    public String getUsersEmailByUserId(UUID userId) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<String> emailQuery = cb.createQuery(String.class);
        Root<UserModel> root = emailQuery.from(UserModel.class);

        emailQuery.select(root.get("email"))
            .where(cb.equal(root.get("id"), userId));

        String email = session.createQuery(emailQuery).uniqueResult();

        session.close();

        return email;
    }
}

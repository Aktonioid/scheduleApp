package com.sched.sched.core.repos;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.sched.sched.core.models.UserModel;

public interface IUserRepo {
    public boolean createUser(UserModel model);
    public boolean updateUser(UserModel model);
    public boolean deleteUser(UUID id);
    public boolean checkIsUserExhistsByEmail(String email);
    public boolean checkIsUserExhistsByusername(String username);
    public boolean checkIsUserExhistByUUID(UUID userId);
    public List<UserModel> getAllUsersByPage(int page);
    public UserModel getUserById(UUID id);
    public UserModel getUserByEmail(String email);
    //TODO поменять withDateHabit на set
    public Set<UserModel> getUsersModelsWithDateActivityByPage(Date date, int page, int pageSize); // получает пользователей и привычки пользователей, если они соответствуют 
    public Set<UserModel> getUsersModelsWithDateHabitByPage(Date date, int page, int pageSize);
    public int getUsersPageCount(int pageSize, boolean isActivity, Date date); // pageSize - это размер страницы, 
                                                                    //isActivity - если true, то получаем страницы пользователей с активностями на день
                                                                    // если isActivity - false, то получем страницы пользователей с активными привычками на дель 
                                                                    // date - на какой день брать колличество страниц пользоветелей
    
    // получаем сколько пользователей по n страниц можно получить. Вообще всех пользователей
    public int getUsersPageCount(int pageSize);

    public String getUsersEmailByUserId(UUID userId); // Получение только адреса почты пользователя

    // обновление пользовательской статистики о проваленых и выполненых привычках в целом
    // public boolean updateAllUserSuccessAndFailureStats();
}
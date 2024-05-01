package com.sched.sched.core.mappers;

import com.sched.sched.core.dtos.UserModelDto;
import com.sched.sched.core.models.UserModel;

public class UserModelMapper {
    public static UserModel asEntity(UserModelDto dto)
    {
        return new UserModel(
            dto.getId(),
            dto.getUsername(),
            dto.getPassword(),
            dto.getName(),
            dto.getSurname(),
            dto.getEmail(),
            dto.isRegisterCompleted(),
            null,
            null,
            dto.isVerificated()
        );
    }
    public static UserModelDto asDto(UserModel model)
    {
        return new UserModelDto(
            model.getId(),
            model.getUsername(),
            model.getPassword(),
            model.getName(),
            model.getSurname(),
            model.getEmail(),
            model.isRegisterCompleted(),
            model.isVerificated()
        );
    }
}

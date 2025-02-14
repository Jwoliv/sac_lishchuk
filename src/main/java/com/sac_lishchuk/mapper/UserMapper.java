package com.sac_lishchuk.mapper;

import com.sac_lishchuk.enums.MandatoryLevel;
import com.sac_lishchuk.model.User;
import com.sac_lishchuk.shared.dto.CreateUserRequest;
import com.sac_lishchuk.shared.dto.UserDto;
import com.sac_lishchuk.shared.request.ChangePasswordRequest;
import com.sac_lishchuk.shared.response.SuccessChangedPasswordResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = { LocalDateTime.class, MandatoryLevel.class })
public interface UserMapper {
    @Mapping(target = "mandatoryLevel", expression = "java(MandatoryLevel.PUBLIC)")
    User mapToCreateUser(CreateUserRequest request);
    UserDto mapEntityToDto(User user);
    List<UserDto> mapEntityToDto(List<User> users);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "oldPassword", source = "user.password")
    @Mapping(target = "newPassword", source = "request.password")
    @Mapping(target = "changedAt", expression = "java(LocalDateTime.now())")
    SuccessChangedPasswordResponse mapToSuccessChangePasswordResponse(User user, ChangePasswordRequest request);
}

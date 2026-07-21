package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // 密码加密器，变成常量使用，密码以后会使用加密器加密存储到数据库中
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(); 
    public final WeChatProperties weChatProperties;
    public final UserMapper userMapper;

    /**
     * User Register
     * @param userRegisterDTO
     */
    public User register(UserRegisterDTO userRegisterDTO) {

        // 判断邮箱是否已被注册
        if(userMapper.getByEmail(userRegisterDTO.getEmail()) != null) {
            throw new LoginFailedException(MessageConstant.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .name(userRegisterDTO.getName())
                .email(userRegisterDTO.getEmail())
                .password(PASSWORD_ENCODER.encode(userRegisterDTO.getPassword()))
                .createTime(LocalDateTime.now())
                .build();
        userMapper.insert(user);
        return user;
    }

    /**
     * User Login
     * @param userLoginDTO
     */
    public User login(UserLoginDTO userLoginDTO) {
        // 根据邮箱查询用户
        User user = userMapper.getByEmail(userLoginDTO.getEmail());
        if (user == null){
            throw new LoginFailedException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 校验密码
        if (!PASSWORD_ENCODER.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new LoginFailedException(MessageConstant.PASSWORD_ERROR);
        }

        return user;
    }
}

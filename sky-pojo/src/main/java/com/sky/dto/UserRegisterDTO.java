package com.sky.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserRegisterDTO implements Serializable {

    private String email;
    private String password;
    private String name;

}

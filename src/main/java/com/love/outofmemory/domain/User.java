package com.love.outofmemory.domain;

import lombok.Data;


import javax.validation.constraints.*;
import java.util.Date;


@Data 
public class User {
    private Integer id;


    @NotNull
    @Size(min=2,max=10,message ="{username.size}")
    private String username;

    @Pattern(regexp = "^(?![0-9]+$)(?![A-Za-z]+$)[A-Za-z0-9]{8,15}$" ,message ="{password.pattern}" )
    private String password;

    @Pattern(regexp = "^[1-9][0-9]{10}$" ,message ="{phone.pattern}")
    private String phone;


    @NotNull(message = "请选择性别")
    private String sex;

    private Date birthday;

    private String reputation;

    @Email(message = "{email}")
    private String email;

    private String qq_number;

    private String address;

    private String image;

    private Date create_time;

    private Integer fans;

    private Integer follow;

    private Integer level;


}

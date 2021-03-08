package com.love.outofmemory.domain.view;

import lombok.Data;

import java.util.Date;

@Data
public class ProfilePageUser {


    private String id;

    private String username;

    private String sex;

    private Date birthday;

    private String reputation;

    private String qq_number;

    private String address;

    private String image;

    private Integer codeage;

    private Integer level;
}

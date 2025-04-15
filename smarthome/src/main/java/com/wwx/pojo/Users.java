package com.wwx.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private Integer id; //ID
    private String username; //用户名
    private String password; //密码
    private String name; //姓名
    private LocalDate entrydate;
    private LocalDateTime createTime; //创建时间
    private LocalDateTime updateTime; //修改时间
    private String image; //头像
    private String currentCity; //当前城市

}

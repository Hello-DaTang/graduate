package com.wwx.pojo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLog {
    private LocalDateTime createTime;
    private String description;
    private Integer id;
}

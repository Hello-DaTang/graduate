package com.wwx.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//作用的时间
@Target(ElementType.METHOD)//作用的位置
public @interface Log {

}

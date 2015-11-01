package com.jarbytes.lab.auth.annotation;

import com.jarbytes.lab.beans.Privilege;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface AuthRequired
{
    Privilege[] value() default Privilege.USER;
}
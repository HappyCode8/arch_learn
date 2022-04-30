package com.wyj.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({FIELD, METHOD, PARAMETER, TYPE})
@Constraint(validatedBy = AuthenticatedAccountValidator.class)
public @interface AuthenticatedAccount {
    String message() default "不是当前登陆用户";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

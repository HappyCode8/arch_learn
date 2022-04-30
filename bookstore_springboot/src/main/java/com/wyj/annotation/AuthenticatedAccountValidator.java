package com.wyj.annotation;

import com.wyj.entity.Account;
import com.wyj.entity.AuthenticAccount;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//todo: 此注解好像没起作用
public class AuthenticatedAccountValidator implements ConstraintValidator<AuthenticatedAccount, Account>{
    @Override
    public boolean isValid(Account account, ConstraintValidatorContext constraintValidatorContext) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(principal)) {
            return false;
        } else {
            AuthenticAccount loginUser = (AuthenticAccount) principal;
            return account.getId().equals(loginUser.getId());
        }
    }
}

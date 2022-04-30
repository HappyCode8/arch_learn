package com.wyj.util;

import com.wyj.model.web.Result;
import org.springframework.http.HttpStatus;

public final class ResultUtil {

    private ResultUtil() {
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(0, HttpStatus.OK.getReasonPhrase(), data);
    }

    public static Result ok() {
        return new Result<>(0, HttpStatus.OK.getReasonPhrase());
    }

}


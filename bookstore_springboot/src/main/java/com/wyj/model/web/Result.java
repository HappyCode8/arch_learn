package com.wyj.model.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @NonNull
    private Integer code;
    @NonNull
    private String msg;
    private T data;
}

package com.milko.awsfilestorage.exception;

import lombok.Getter;

public class ApiException extends RuntimeException{
    protected String errorCode;

    public ApiException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}

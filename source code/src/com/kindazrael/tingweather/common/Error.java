
package com.kindazrael.tingweather.common;

public class Error {

    private Throwable e;
    private String errorMsg;
    private int errorCode = Integer.MIN_VALUE;

    public Error (Throwable e) {
        this.e = e;
        this.errorMsg = e.getMessage();
    }

    public Error (Throwable e, int errorCode) {
        this.e = e;
        this.errorCode = errorCode;
        this.errorMsg = e.getMessage();
    }

    public Error (Throwable e, int errorCode, String msg) {
        this.e = e;
        this.errorCode = errorCode;
        this.errorMsg = msg;
    }

    public Throwable getThrowable() {
        return e;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }
}

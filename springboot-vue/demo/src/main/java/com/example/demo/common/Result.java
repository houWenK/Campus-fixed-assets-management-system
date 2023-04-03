package com.example.demo.common;

import lombok.Data;

@Data
public class Result {
    private int code;
    private String msg;
    private Long total;
    private Object data;

    public static Result fall() {
        return result(400, "失败", 0L, null);
    }

    public static Result suc() {
        return result(200, "成功", 0L, null);
    }

    public static Result suc(Object data) {
        return result(200, "成功", 0L, data);
    }

    public static Result suc(Long total, Object data) {
        return result(200, "成功", total, data);
    }

    public static Result result(int code, String msg, Long total, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setTotal(total);
        result.setData(data);
        return result;
    }
}

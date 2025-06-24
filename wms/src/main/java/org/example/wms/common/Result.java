package org.example.wms.common;

import lombok.Data;

@Data
public class Result {
    //编码 200/400
    private int code;
    //成功/失败
    private String message;
    //总记录数
    private Long total;
    //数据
    private Object data;

    public static Result success(){
        return result(200,"成功",0L,null);
    }

    public static Result success(Object data) {
        return result(200,"成功",0L,data);
    }

    public static Result success(Long total, Object data) {
        return result(200,"成功",total,data);
    }

    public static Result fail(){
        return result(400,"失败",0L,null);
    }

    public static Result result(int code, String message, Long total, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setTotal(total);
        result.setData(data);
        return result;
    }
}

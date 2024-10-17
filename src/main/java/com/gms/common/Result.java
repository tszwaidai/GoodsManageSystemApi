package com.gms.common;

import lombok.Data;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: 后端返回数据的封装
 * @date 2024/6/7 23:21
 */
@Data
public class Result<T> {

    private int code; //编码200 / 400
    private String msg; //成功/失败
    private Long total; //总记录数
    private T data; //数据

    public static <T> Result<T> fail() {
        return result(400,"失败",0L,null);
    }

    public static <T> Result<T> fail(String msg) {
        return result(400,msg,0L,null);
    }

    public static <T> Result<T> suc(String msg) {
        return result(200,msg,0L,null);
    }
    public static <T> Result<T> suc() {
        return result(200,"成功",0L,null);
    }

    public static <T> Result<T> suc(Object data) {
        return result(200,"成功",0L,data);
    }

    public static <T> Result<T> suc(Object data,Long total) {
        return result(200,"成功",total,data);
    }


    private static <T> Result<T> result(int code, String msg,Long total,Object data){
        Result res = new Result();
        res.setData(data);
        res.setMsg(msg);
        res.setCode(code);
        res.setTotal(total);
        return res;
    }

}

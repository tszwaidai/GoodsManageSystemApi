package com.gms.common;

import lombok.Data;

import java.util.HashMap;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/6/7 18:40
 */
@Data
public class QueryPageParam {
    //默认

    //pagehelper
    private int pageSize ;
    private int pageNum ;

    private String queryParam;

}

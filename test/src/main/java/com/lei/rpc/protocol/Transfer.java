package com.lei.rpc.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class Transfer<T> {

    public String serialize(T t){
        return JSONObject.toJSONString(t);
    }

    public T deserialize(String json,Class clazz){
        return (T) JSON.parseObject(json, clazz);
    }
}

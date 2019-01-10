package com.github.xiaomogu.commons.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

import java.io.IOException;
import java.util.List;

/**
 * @auther JameHou
 * @date 2019/1/6 21:02
 */
public class HzJson {

    private ObjectMapper objectMapper;

    public HzJson(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 将对象转换为json字符串
     */
    public <T> String obj2string(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * description: 注意此修改配置，会影响此线程的objectMapper
     * @param t
     * @param serializationView
     * @param <T>
     * @return obj -> String
     */
    public <T> String obj2string(T t,Class<?> serializationView){
        try {
            return objectMapper.writerWithView(serializationView).writeValueAsString(t);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * 将字符串转list对象
     */
    public <T> List<T> str2list(String jsonStr, Class<T> cls) {
        try {
            JavaType t = objectMapper.getTypeFactory().constructParametricType(
                    List.class, cls);
            return objectMapper.readValue(jsonStr, t);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public <T> T str2obj(String jsonStr, TypeReference typeReference){
        try {
            return objectMapper.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }


    /**
     * 将字符串转为对象
     */
    public <T> T str2obj(String jsonStr, Class<T> cls) {
        try {
            return objectMapper.readValue(jsonStr, cls);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }



    /**
     * 将字符串转为json节点
     */
    public JsonNode str2node(String jsonStr) {
        try {
            return objectMapper.readTree(jsonStr);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public <T> T readAs(byte[] bytes, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(bytes, typeReference);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * 获取BaseResult中的泛型，传入的是泛型类型
     * @param clz
     * @param <T>
     * @return
     */
   /*public  <T> TypeReference<ResponseResult<T>> getReference(Class <T> clz) {
        Type[] types = new Type[1];
        types[0] = clz;
        final ParameterizedTypeImpl type = ParameterizedTypeImpl.make(ResponseResult.class, types, ResponseResult.class.getDeclaringClass());
        return new TypeReference<ResponseResult<T>>() {
            @Override
            public Type getType() {
                return type;
            }
        };
   }*/
}
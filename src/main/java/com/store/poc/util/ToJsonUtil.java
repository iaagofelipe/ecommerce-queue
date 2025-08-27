package com.store.poc.util;


import com.fasterxml.jackson.databind.ObjectMapper;

public class ToJsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object o) {
        try { return mapper.writeValueAsString(o); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}

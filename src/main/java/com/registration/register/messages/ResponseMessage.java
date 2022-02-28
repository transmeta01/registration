package com.registration.register.messages;


import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class ResponseMessage<T> {
    public T payload;
    public String message;

    public Map<String, String> metadata = new HashMap<>();

}

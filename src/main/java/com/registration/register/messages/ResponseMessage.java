package com.registration.register.messages;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Builder
@Setter
public class ResponseMessage<T> {
    public T payload;
    public String message;

    public Date lastModified = new Date();

    public Map<String, String> metadata = new HashMap<>();

}

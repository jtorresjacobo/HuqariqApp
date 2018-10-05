package com.lolisapp.gametraductor.util.volley;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by JeanPatrick on 12/10/2015.
 */
public class UnixTimeDeserializer implements JsonDeserializer<Date> {
    public static Date FromUnixTime(long unix_time) {
        Date date = new Date();
        date.setTime(unix_time);
        return date;
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        if (json.isJsonNull())
            return null;
        long unixstamp = json.getAsLong();
        return FromUnixTime(unixstamp);
    }

}
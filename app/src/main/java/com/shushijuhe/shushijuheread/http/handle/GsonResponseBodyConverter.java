package com.shushijuhe.shushijuheread.http.handle;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }
    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject!=null){
                return gson.fromJson(response, type);
            }else {
                String msg = jsonObject.getString("Message");
                throw  new RuntimeException(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }
}

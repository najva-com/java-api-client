package com.najva.api;

import java.io.IOException;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.Map;
import java.util.List;
import java.lang.Object;

import okhttp3.*;

public class Najva {

    public static final int SEGMENTS = 0;
    public static final int ACCOUNTS = 1;

    public static final String SEGMENTS_URL = "https://app.najva.com/api/v1/websites/%s/segments/";
    public static final String ACCOUNTS_URL = "https://app.najva.com/api/v1/one-signals/";

    private String apikey;
    private String token;

    public Najva(String apikey, String token) {
        this.apikey = apikey;
        this.token = token;
    }

    public String post(Notification notification) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        notification.data.put("api_key", apikey);
        String content = buildBody(notification.data);

        System.out.println(content);

        RequestBody body = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url(notification.url)
                .post(body)
                .addHeader("authorization", "Token " + token)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String get(int type) {
        if (type == SEGMENTS) {
            return get(String.format(SEGMENTS_URL, apikey));
        } else if (type == ACCOUNTS) {
            return get(ACCOUNTS_URL);
        } else {
            throw new IllegalArgumentException("'type' must be Najva.ACCOUNTS or Najva.SEGMENTS");
        }
    }

    private String get(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("authorization", "Token " + token)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String buildBody(Map<String, Object> body) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        builder.append("{");
        for (String key : body.keySet()) {
            if (!isFirst) {
                builder.append(",");
            }
            isFirst = false;
            builder.append("\"");
            builder.append(key);
            builder.append("\"");
            builder.append(":");
            if (body.get(key) instanceof List) {
                appendList(builder, (List) body.get(key));
            } else if (body.get(key) instanceof Map) {
                appendJson(builder, (Map) body.get(key));
            } else {
                builder.append("\"");
                builder.append(body.get(key));
                builder.append("\"");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    public void appendList(StringBuilder builder, List list) {
        builder.append("[");
        boolean isFirst = true;
        for (Object obj : list) {
            if (!isFirst) {
                builder.append(",");
            }
            isFirst = false;
            builder.append(obj);
        }
        builder.append("]");
    }

    public void appendJson(StringBuilder builder, Map map) {
        builder.append("\"{");
        boolean isFirst = true;
        for (Object key : map.keySet()) {
            if (!isFirst) {
                builder.append(",");
            }
            isFirst = false;
            builder.append("\\\"");
            builder.append(key);
            builder.append("\\\"");
            builder.append(":");
            builder.append("\\\"");
            builder.append(map.get(key));
            builder.append("\\\"");
        }
        builder.append("}\"");
    }


}
package com.edison.android.tools.io;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpRetryException;

import okhttp3.Response;

public class JsonResponse {

    final JSONObject mJson;

    public JsonResponse(Response response) throws IOException {
        if (response.isSuccessful() && response.body() != null) {
            String body = response.body().string();
            try {
                mJson = new JSONObject(body);
            } catch (JSONException e) {
                throw new HttpRetryException("Malformed response: " + body, response.code());
            }
        } else {
            throw new  HttpRetryException(response.message(), response.code());
        }
    }

    public JSONObject json() {
        return mJson;
    }

}

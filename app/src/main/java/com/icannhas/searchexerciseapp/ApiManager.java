package com.icannhas.searchexerciseapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 9/16/2016.
 */
public class ApiManager {

    private static final String API_BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final String ENDPOINT_LIST = "/posts/";

    private static ApiManager singleton;
    private static Context mContext;

    //Android/JAVA sometimes removes objects unused so we need to initialize the manager everytime
    public static ApiManager getInstance(Context context) {
        mContext = context;
        if (singleton == null) {
            singleton = new ApiManager();
        }
        return singleton;
    }

    //appends the API BASE URL to the endpoint
    private static String createApiUrl(String endPoint) {
        String baseUrl = API_BASE_URL;
        if (!baseUrl.endsWith("/"))
            baseUrl = baseUrl + "/";
        if (endPoint.startsWith("/"))
            return baseUrl + endPoint.substring(1);
        return baseUrl + endPoint;
    }

    //appends the API BASE URL to the endpoint with the parameters
    private String createApiUrl(String endPoint, Map<String, List<String>> params) {
        StringBuilder sb = new StringBuilder(createApiUrl(endPoint));
        boolean first = true;
        for (String key : params.keySet()) {
            for (String val : params.get(key)) {
                if (first) {
                    sb.append("?");
                    first = false;
                } else {
                    sb.append("&");
                }
                sb.append(key + "=" + val);
            }
        }
        return sb.toString();
    }

    private void printErrorBody(VolleyError error) {
        try {
            String errorBody = new String(error.networkResponse.data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //generic interface for creating get request
    private void newGetRequest(String endPoint, final Map<String, String> headers, final Map<String, List<String>> params, int timeOutMs,
                               int retries, int backOffMultiplier, final ApiCallback<String> callback) {
        RequestQueue rq = SearchApplication.getInstance().getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, createApiUrl(endPoint, params), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onFetchFromRemote(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // printErrorBody(error);
                callback.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers == null)
                    return new HashMap<String, String>();
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, backOffMultiplier));
        rq.add(request);
    }

    //GET request for the unfiltered list
    public void apiGetList(final ApiCallback<List<JSONTestObject>> callback) {
        Map<String, String> headers = new HashMap<String, String>();
        String endpoint = ENDPOINT_LIST;
        newGetRequest(endpoint, headers, new HashMap<String, List<String>>(), 30000, 1, 1, new SimpleFetchCallback<String>() {

            @Override
            protected void onFetchFromRemote(String response) {

                Log.d("RESPONSE_TAG", response);

                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<JSONTestObject>>() {
                }.getType();
                List<JSONTestObject> list = gson.fromJson(response, type);
                callback.onFetchFromRemote(list);
            }

            @Override
            protected void onError(VolleyError error) {
                callback.onError(error);
            }
        });
    }

    //GET request for the filtered list
    public void apiGetList(String userId, final ApiCallback<List<JSONTestObject>> callback) {
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        if(userId != null && userId.length() > 0){
            ArrayList<String> idList = new ArrayList<>();
            idList.add(userId);
            params.put("userId",idList);
        }
        String endpoint = ENDPOINT_LIST;
        newGetRequest(endpoint, headers, params, 30000, 1, 1, new SimpleFetchCallback<String>() {

            @Override
            protected void onFetchFromRemote(String response) {

                Log.d("RESPONSE_TAG", response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<JSONTestObject>>(){}.getType();
                List<JSONTestObject> list = gson.fromJson(response, type);
                callback.onFetchFromRemote(list);
            }

            @Override
            protected void onError(VolleyError error) {
                callback.onError(error);
            }
        });
    }

    //interface for a callback (to easily get the results after successful request, see newGetRequest function callback.onFetchFromRemote)
    public static abstract class ApiCallback<T> {

        protected abstract void onFetchFromDb(T response);

        protected abstract void onFetchFromRemote(T response);

        protected void onFetchFromRemote(T response, Integer nextPage) {}

        protected abstract void onError(VolleyError error);
    }

    //interface for a callback (to easily get the results after successful request, see apiGetList function callback.onFetchFromRemote)
    public static class SimpleFetchCallback<T> extends ApiCallback<T> {
        @Override
        protected void onFetchFromDb(T response) {
        }

        @Override
        protected void onFetchFromRemote(T response) {
        }

        @Override
        protected void onFetchFromRemote(T response, Integer nextPage) {

        }

        @Override
        protected void onError(VolleyError error) {
        }
    }

}

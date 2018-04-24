package com.example.myvolleysdk;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class VolleyObject {

    private static Context mcontext;
    private static MyVolleySingleton myVolleySingleton;
    private String URL="https://api.androidhive.info/volley/person_object.json";

    public static void initSDK(Context context){
        mcontext=context.getApplicationContext();
        myVolleySingleton = new MyVolleySingleton(mcontext);
    }

    public void fetchDatabyUrl(JSONObject jsonObject, final VolleyResponseInterface volleyResponseInterface){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                volleyResponseInterface.onResponse(response,null);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseInterface.onResponse(null,error);
            }
        });

        // Adding request to request queue
        myVolleySingleton.addToRequestQueue(jsonObjReq);
    }

    public void fetchDatabyUrlInterface(JSONObject jsonObject, final VolleyResponseInterface volleyResponseInterface){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                volleyResponseInterface.onResponse(response,null);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseInterface.onResponse(null,error);
            }
        });

        // Adding request to request queue
        myVolleySingleton.addToRequestQueue(jsonObjReq);
    }
}

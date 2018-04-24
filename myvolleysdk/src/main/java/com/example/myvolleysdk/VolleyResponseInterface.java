package com.example.myvolleysdk;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyResponseInterface {

    void onResponse(JSONObject response, VolleyError volleyError);
}

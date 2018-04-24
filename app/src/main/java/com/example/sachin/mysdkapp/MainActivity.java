package com.example.sachin.mysdkapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.myvolleysdk.VolleyObject;
import com.example.myvolleysdk.VolleyResponseInterface;

import org.json.JSONObject;

public class MainActivity extends BaseClassActivity implements VolleyResponseInterface{

    private TextView resultTV;
    private TextView resultTV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VolleyObject.initSDK(this);
        resultTV = (TextView) findViewById(R.id.resultTV);
        resultTV2 = (TextView) findViewById(R.id.resultTV2);
        final VolleyObject volleyObject = new VolleyObject();
        resultTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            volleyObject.fetchDatabyUrl(null, new VolleyResponseInterface() {
                @Override
                public void onResponse(JSONObject response, VolleyError volleyError) {

                    if(volleyError==null){
                        System.out.println("----response---- "+response.toString());
                        resultTV.setText(response.toString());
                    }else{
                        System.out.println("----volleyError---- "+volleyError.getMessage());
                        resultTV.setText(volleyError.getMessage());
                    }

                }
            });





            }
        });


        resultTV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyObject.fetchDatabyUrlInterface(null,MainActivity.this);
            }
        });


        //commonMethod("sachin " +MainActivity.class.getSimpleName());
    }


    @Override
    public void onResponse(JSONObject response, VolleyError volleyError) {
        if(volleyError==null){
            System.out.println("----response2---- "+response.toString());
            resultTV2.setText(response.toString());
        }else{
            System.out.println("----volleyError2---- "+volleyError.getMessage());
            resultTV2.setText(volleyError.getMessage());
        }
    }

    @Override
    protected void commonMethod2(String data) {
        super.commonMethod2(data);
    }


    @Override
    public void commonMethod(String data) {
        super.commonMethod(data);
    }
}

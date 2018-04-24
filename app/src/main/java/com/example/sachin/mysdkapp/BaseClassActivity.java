package com.example.sachin.mysdkapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("-----onCreateBaseClassActivity-----");

        //commonMethod("sachin");
       // commonMethod2("sapkale");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("-----onPauseBaseClassActivity-----");
    }

    public void commonMethod  (String data){
        System.out.println("-----commonMethodBaseClassActivity----- " +data);
    }

    protected void commonMethod2  (String data){
        System.out.println("-----commonMethod2BaseClassActivity----- " +data);
    }
}

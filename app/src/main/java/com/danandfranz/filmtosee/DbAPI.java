package com.danandfranz.filmtosee;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Franz on 17/02/2016.
 */
public class DbAPI {
    static final String URL = "http://normandy.dmi.unipg.it/blockchainvis/Film/orient.php";
    private static DbAPI instance;
    private RequestQueue mRequestQueue;

    public DbAPI(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static DbAPI getInstance(Context context) {
        if (instance == null) {
            instance = new DbAPI(context);
        }
        return instance;
    }

    public void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }


    public RequestQueue getRequestQueue(){
        return this.mRequestQueue;
    }
}

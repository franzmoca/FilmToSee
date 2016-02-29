package com.danandfranz.filmtosee;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tsuru on 20/02/16.
 */
public class Util {

    static Call post(RequestBody body,OkHttpClient client, Callback callback) throws IOException {
        Request request = new Request.Builder()
                .url("http://normandy.dmi.unipg.it/blockchainvis/Film/orient.php")
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);

        return call;

    }

}

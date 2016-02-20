package com.danandfranz.filmtosee;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

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

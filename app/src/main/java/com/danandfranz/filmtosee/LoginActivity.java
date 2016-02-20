package com.danandfranz.filmtosee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    public String responseString = null;
    private static final String TAG = "LoginActivity";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    ProgressDialog progressDialog;

    OkHttpClient client;
    private static final int REQUEST_SIGNUP = 0;
    // Session Manager Class
    SessionManager session;

    // UI references.
    private ScrollView rootlayout;
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button signInButton;
    private TextView signUpLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.input_email);
        mPasswordView = (EditText) findViewById(R.id.input_password);
        signInButton = (Button) findViewById(R.id.btn_login);
        signUpLink = (TextView)findViewById(R.id.link_signup);
        Log.d(TAG, "User Login Status: " + session.isLoggedIn());

        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    login();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        signUpLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }

    private void login() throws JSONException {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        signInButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        client = new OkHttpClient();
        // TODO: Implement your own authentication logic here.

        Call newcall = getLogin(email, password);
        signInButton.setEnabled(true);
/*

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (email.equals("test@test.it") && password.equals("123456")) {
                            onLoginSuccess();
                        } else {
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);*/

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                //this.finish();
                rootlayout = (ScrollView) findViewById(R.id.rootlayout);
                Snackbar.make(rootlayout, "Signup success!", Snackbar.LENGTH_LONG)
                        .setAction("Close", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Perform anything for the action selected
                            }
                        }).setDuration(Snackbar.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String username, String rid) {
        //signInButton.setEnabled(true);
        session.createLoginSession(username, mEmailView.getText().toString() ,rid);

        Intent intent = new Intent(getApplicationContext(), GroupsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //makes sure that you cannot go back to the previous activity with the BACK button.
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        rootlayout = (ScrollView) findViewById(R.id.rootlayout);

        Snackbar.make(rootlayout, "Login failed!", Snackbar.LENGTH_SHORT)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Perform anything for the action selected
                    }
                }).setDuration(Snackbar.LENGTH_SHORT).show();


        //signInButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailView.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordView.setError("between 4 and 10false alphanumeric characters");
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;
    }


    public Call getLogin(String mail,String pwd) throws JSONException{
        RequestBody body;
        body = new FormBody.Builder()
                .add("get","login")
                .add("email", mail)
                .add("password",pwd)
                .build();
        try {
            return Util.post(body,client, new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call , Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    //System.out.println(response.body().toString());
                    try {
                        String json = response.body().string();
                        Log.d(TAG,json);
                        JSONObject jsonObj = new JSONObject(json);
                        String result = jsonObj.getString("result");

                        Log.d(TAG,result);
                        if (result.equalsIgnoreCase("success")) {
                            String rid = jsonObj.getString("id");
                            String username = jsonObj.getString("username");
                            onLoginSuccess(username,rid);
                        } else {
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}


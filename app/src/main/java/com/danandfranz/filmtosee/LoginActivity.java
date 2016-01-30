package com.danandfranz.filmtosee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {


    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    // Session Manager Class
    SessionManager session;

    // UI references.
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
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
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

    private void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        signInButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if(email.equals("test@test.it")&password.equals("123456")){
                            onLoginSuccess();
                        }
                        else {
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        signInButton.setEnabled(true);
        session.createLoginSession("FilmToSee", mEmailView.getText().toString());

        Intent intent = new Intent(getApplicationContext(), GroupsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //makes sure that you cannot go back to the previous activity with the BACK button.
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signInButton.setEnabled(true);
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
            mPasswordView.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;
    }
}


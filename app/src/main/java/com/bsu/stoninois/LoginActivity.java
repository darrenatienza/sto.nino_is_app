package com.bsu.stoninois;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bsu.stoninois.api.interfaces.AuthClient;
import com.bsu.stoninois.api.models.AuthModel;
import com.bsu.stoninois.fragments.BasicFormDialog_;
import com.bsu.stoninois.fragments.IpAddressDialog_;
import com.bsu.stoninois.misc.Prefs_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.annotations.sharedpreferences.SharedPref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

/**
 * A login screen that offers login via email/password.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity implements IpAddressDialog_.DialogActionListener{


    // UI references.
    @ViewById(R.id.email)
    AutoCompleteTextView mEmailView;
    @ViewById(R.id.password)
    EditText mPasswordView;
    @ViewById(R.id.login_progress)
    View mProgressView;
    @ViewById(R.id.login_form)
    View mLoginFormView;
    @ViewById(R.id.btnSetting)
    ImageView btnSetting;

    // Preferences
    @Pref
    Prefs_ prefs;

    @RestService
    AuthClient authClient;
    private boolean isLogin;
    private String token;
    private IpAddressDialog_ formDialog;
    private String ip;
    private String port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @AfterViews
    void afterViews(){
        ip = prefs.ipAddress().get();
        port = prefs.port().get();
    }

    @EditorAction(R.id.password)
    void eaPassword(){
       attemptLogin();
    }
    @Click(R.id.email_sign_in_button)
    void login(){
        attemptLogin();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isUserNameValid(userName)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            loginAsync(userName,password);
        }
    }

    private boolean isUserNameValid(String email) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            btnSetting.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            btnSetting.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    @Click
    void btnSetting(){
        formDialog = new IpAddressDialog_();
        formDialog.setIpAddress(prefs.ipAddress().get());
        formDialog.setPort(prefs.port().get());
        formDialog.show(getSupportFragmentManager(),"dialog");
    }
    @Background(delay = 2000)
    void loginAsync(String userName, String password){
        try{
            AuthModel authModel = new AuthModel();
            authModel.setUserName(userName);
            authModel.setPassword(password);
            ip = prefs.ipAddress().get();
            token = authClient.login(ip,port, authModel);
            if (token != ""){
               loginResult(true);
            }else{
               loginResult(false);
            }

        }catch (RestClientException ex){
            Log.e("Rest Error", ex.getMessage());
            loginResult(false    );
        }
    }

    @UiThread
    void loginResult(boolean isSuccess) {
        if(isSuccess){
            setResult(RESULT_OK);
            finish();
        }else{
            Toast.makeText(this,"Login failed",Toast.LENGTH_SHORT).show();

        }
        showProgress(false);
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();

    }

    @Override
    public void onSave(String ipAddress,String port) {
        prefs.ipAddress().put(ipAddress);
        prefs.port().put(port);
        Toast.makeText(this,"Url " + ipAddress + " with port " + port + " has been saved!",Toast.LENGTH_SHORT).show();
    }
}


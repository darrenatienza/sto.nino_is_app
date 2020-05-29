package com.bsu.stoninois;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bsu.stoninois.activities.AccomplishmentListActivity_;
import com.bsu.stoninois.activities.CommonHealthProfileActivity;
import com.bsu.stoninois.activities.CommonHealthProfileActivity_;
import com.bsu.stoninois.activities.HealthDataBoardListActivity_;
import com.bsu.stoninois.activities.QuarterlyReportListActivity_;
import com.bsu.stoninois.api.interfaces.AuthClient;
import com.bsu.stoninois.api.interfaces.HealthDataBoardClient;
import com.bsu.stoninois.api.interfaces.UserClient;
import com.bsu.stoninois.api.models.AuthModel;
import com.bsu.stoninois.api.models.HealthDataBoardModel;
import com.bsu.stoninois.fragments.BasicFormDialog;
import com.bsu.stoninois.fragments.BasicFormDialog_;

import com.bsu.stoninois.misc.Prefs_;
import com.github.thunder413.datetimeutils.DateTimeUtils;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    static final int SHOW_LOGIN = 1;
    @Pref
    Prefs_ authPrefs;

    @RestService
    AuthClient restClient;
    @RestService
    UserClient userClient;

    @RestService
    HealthDataBoardClient healthDataBoardClient;

    private Bitmap image;
    private Date expDate;

    @AfterViews
    void AfterViews(){
        expDate = DateTimeUtils.formatDate("2020-05-05");
        //CheckExpiration();
        //getImage();
       showLogin();



    }
    void showLogin(){
        LoginActivity_.intent(this).startForResult(SHOW_LOGIN);
    }
    @Click
    void btnLogOut(){
        showLogin();
    }
    void CheckExpiration(){

        if(new Date().after(expDate)){
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);

            sweetAlertDialog.setTitleText("Trial Version Expires")
                    .setContentText("This application has been expired. Please contact developer.")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                            finish();
                        }
                    })
                    .show();
            sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });



        }else{
            ShowTrialVersionNotif();
        }
    }
    @Click
    void healthDataBoard(){
        HealthDataBoardListActivity_.intent(this).start();
    }

    @Click
    void quarterlyReport(){
        QuarterlyReportListActivity_.intent(this).start();
    }
    @Click
    void accomplishment(){
        AccomplishmentListActivity_.intent(this).start();
    }
    @Click
    void cardCommonHealthProfile(){
        CommonHealthProfileActivity_.intent(this).start();
    }

    @OnActivityResult(SHOW_LOGIN)
    void onResult(int resultCode) {
        if(resultCode != RESULT_OK){
            //exit application for not logging in
            finish();
        }

    }

    void ShowTrialVersionNotif(){
        String expDate =  ("This application is trial version and will expire on ").concat(DateTimeUtils.formatWithPattern(this.expDate,"EEEE, MMMM dd, yyyy"));
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Trial Version")
                .setContentText(expDate)
                .show();
    }
}

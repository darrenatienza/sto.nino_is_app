package com.bsu.stoninois.activities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bsu.stoninois.R;
import com.bsu.stoninois.api.interfaces.CommonHealthProfileClient;
import com.bsu.stoninois.api.interfaces.HealthDataBoardClient;
import com.bsu.stoninois.api.models.CommonHealthProfileModel;
import com.bsu.stoninois.api.models.HealthDataBoardModel;
import com.bsu.stoninois.misc.Prefs_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InjectMenu;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

@EActivity(R.layout.activity_health_data_board_update)
public class HealthDataBoardUpdateActivity extends AppCompatActivity {


    @Extra
    int id;

    @RestService
    CommonHealthProfileClient commonHealthProfileClient;
    @RestService
    HealthDataBoardClient healthDataBoardClient;
    @ViewById
    Spinner spYears;

    @ViewById
    Spinner spCommonHealthProfiles;
    @ViewById
    EditText etTally;
    private ArrayAdapter<String> yearsAdapter;
    private ArrayList<String> commonHealthProfileList;
    private int year;
    private ArrayAdapter<String> commonHealthProfileAdapter;
    private String commonHealthProfile;
    private int commonHealthProfileID;
    private ArrayList<Integer> commonHealthProfileIDs;
    // Preferences
    @Pref
    Prefs_ prefs;
    private String ip;
    private String port;
    @AfterViews
    void afterViews(){
        initVariables();
        initAsync();
        initViews();
        initActions();


    }
    void getDataAsync() {
        if(id > 0){
            HealthDataBoardModel model = healthDataBoardClient.getHealthDataBoard(ip,port,id);
            setData(model);
        }
    }
    @UiThread
    void setData(HealthDataBoardModel model) {
        spCommonHealthProfiles.setSelection(getPositionByID(commonHealthProfileIDs,model.getCommonHealthProfileID()));
        spYears.setSelection(getPositionByName(getApplication().getResources().getStringArray(R.array.years2),String.valueOf(model.getYear())));
        etTally.setText(String.valueOf(model.getCount()));
    }

    private int getPositionByID(ArrayList<Integer> ids, int id) {
        int pos = 0;

        for (int _id :
                ids) {
            if(_id == id){
                return pos;
            }else{
                pos++;
            }
        }
        // no id found
        return 0;
    }
    private int getPositionByName(String[] names, String name) {
        int pos = 0;

        for (String str :
                names) {
            if(str.contentEquals(name)){
                return pos;
            }else{
                pos++;
            }
        }
        // no name found
        return 0;
    }

    void initVariables(){
        ip = prefs.ipAddress().get();
        port = prefs.port().get();
        commonHealthProfileIDs = new ArrayList<>();
        commonHealthProfileList = new ArrayList<>();
        year = 2020;
        yearsAdapter = new ArrayAdapter<>(this,R.layout.item_spinner,getResources().getStringArray(R.array.years2));

    }
    void initViews(){
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYears.setAdapter(yearsAdapter);


    }
    void initActions() {


    }

    @Background
    void initAsync(){
        loadCommonHealthProfilesAsync();
        getDataAsync();    }

    void loadCommonHealthProfilesAsync(){
        try {
            for (CommonHealthProfileModel m:
                    commonHealthProfileClient.getAll(ip,port)) {
                commonHealthProfileList.add(m.getTitle());
                commonHealthProfileIDs.add(m.getId());
            }
            loadCommonHealthProfiles();
        } catch (RestClientException e){
            Log.e("Rest error",e.getMessage());
        }
    }
    @UiThread
    void loadCommonHealthProfiles(){

        if (commonHealthProfileList.size() > 0){

            commonHealthProfileAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, commonHealthProfileList);
            commonHealthProfileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCommonHealthProfiles.setAdapter(commonHealthProfileAdapter);
        }



    }
    @InjectMenu
    void setMenu(Menu myMenu){

    }

    @ItemSelect
    void spYears(boolean selected, String item){
        if(selected){
            if(!item.contentEquals("Filter Years")){
                year = Integer.parseInt(item);

            }

        }
    }
    @ItemSelect
    void spCommonHealthProfiles(boolean selected, int position){
        if(selected){

          commonHealthProfileID = commonHealthProfileIDs.get(position);

            Toast.makeText(this,commonHealthProfileList.get(position),Toast.LENGTH_LONG).show();
        }

    }
    @Background
    void getCommonHealthProfileIdAsync(String title) {
        try{
            commonHealthProfileID = commonHealthProfileClient.getID(ip,port,title);
        }catch (RestClientException ex){
            Log.e("Rest Error",ex.getMessage());
        }

    }

    @Click
    void btnSave(){
        HealthDataBoardModel model = new HealthDataBoardModel();
        model.setCommonHealthProfileID(commonHealthProfileID);
        model.setCount(Integer.valueOf(etTally.getText().toString()));
        model.setUserID(1);
        model.setYear(year);
        if(id > 0){
            editAsync(id,model);
        }else{
            addAsync(model);
        }

    }
    @Background
    void addAsync(HealthDataBoardModel model) {
        try{
            healthDataBoardClient.add(ip,port,model);
            saveSuccess();
        }catch (RestClientException ex){
            Log.e("Rest Error",ex.getMessage());
        }

    }
    @UiThread
    void saveSuccess() {
      Toast.makeText(this,"Successfully save!",Toast.LENGTH_LONG).show();
      setResult(RESULT_OK);
      finish();
    }

    @Background
    void editAsync(int id, HealthDataBoardModel model) {
        try{
            healthDataBoardClient.edit(ip,port,id,model);
            saveSuccess();
        }catch (RestClientException ex){
            Log.e("Rest Error",ex.getMessage());
        }
    }
    @Click
    void btnPlus(){
        int tally = Integer.valueOf(etTally.getText().toString()) + 1;
        etTally.setText(String.valueOf(tally));
    }
    @Click
    void btnMinus(){
        int tally = Integer.valueOf(etTally.getText().toString()) - 1;
        etTally.setText(String.valueOf(tally));
    }
    @Click
    void btnDelete(){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Delete record?")
                .setContentText("Record can't be recover!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        delete();
                    }
                })
                .show();
    }
    @Background
    void delete() {
        try{
            healthDataBoardClient.delete(ip,port,id);
            deleteSuccess();
        }catch (RestClientException ex){
            Log.e("Rest error",ex.getMessage());
        }

    }
    @UiThread
    void deleteSuccess() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Success")
                .setContentText("Record deleted!")
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                       setResult(RESULT_OK);
                       finish();
                    }
                })
                .show();
    }
}

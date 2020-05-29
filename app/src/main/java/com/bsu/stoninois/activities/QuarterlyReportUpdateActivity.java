package com.bsu.stoninois.activities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bsu.stoninois.R;
import com.bsu.stoninois.api.interfaces.AccomplishmentClient;
import com.bsu.stoninois.api.interfaces.QuarterlyReportClient;
import com.bsu.stoninois.api.models.AccomplishmentModel;
import com.bsu.stoninois.api.models.HealthDataBoardModel;
import com.bsu.stoninois.api.models.QuarterlyReportModel;
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

@EActivity(R.layout.activity_quarterly_report_update)
public class QuarterlyReportUpdateActivity extends AppCompatActivity {

    @Extra
    int id;

    @RestService
    AccomplishmentClient accomplishmentClient;
    @RestService
    QuarterlyReportClient quarterlyReportClient;
    @ViewById
    Spinner spYears;
    @ViewById
    Spinner spQuarter;

    @ViewById
    Spinner spAccomplishments;

    @ViewById
    Spinner spGender;

    @ViewById
    EditText etTally;

    private ArrayAdapter<String> yearsAdapter;
    private ArrayAdapter<String> quarterAdapter;
    private ArrayAdapter<String> genderAdapter;
    private ArrayList<String> accomplishmentList;

    private ArrayAdapter<String> accomplishmentListAdapter;
    private int year;
    private int quarter;
    private String accomplishment;
    private int accomplishmentID;
    private ArrayList<Integer> accomplishmentIDs;
    private String gender;
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
            QuarterlyReportModel model = quarterlyReportClient.get(ip,port,id);
            setData(model);
        }
    }
    @UiThread
    void setData(QuarterlyReportModel model) {
        spAccomplishments.setSelection(getPositionByID(accomplishmentIDs,model.getAccomplishmentID()));
        spYears.setSelection(getPositionByName(getApplication().getResources().getStringArray(R.array.years2),String.valueOf(model.getYear())));
        spQuarter.setSelection(getPositionByName(getApplication().getResources().getStringArray(R.array.quarter2),String.valueOf(model.getQuarter())));
        spGender.setSelection(getPositionByName(getApplication().getResources().getStringArray(R.array.gender),String.valueOf(model.getGender())));
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
        accomplishmentIDs = new ArrayList<>();
        accomplishmentList = new ArrayList<>();
        year = 2020;
        quarter = 1;
        yearsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.years2));
        quarterAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.quarter2));
        genderAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.gender));
    }
    void initViews(){
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYears.setAdapter(yearsAdapter);
        spQuarter.setAdapter(quarterAdapter);
        spGender.setAdapter(genderAdapter);


    }
    void initActions() {


    }

    @Background
    void initAsync(){
        loadAccomplishmentsAsync();
        getDataAsync();    }

    void loadAccomplishmentsAsync(){
        try {
            for (AccomplishmentModel m:
                    accomplishmentClient.getAll(ip,port)) {
                accomplishmentList.add(m.getTitle());
                accomplishmentIDs.add(m.getId());
            }
            loadAccomplishments();
        } catch (RestClientException e){
            Log.e("Rest error",e.getMessage());
        }
    }
    @UiThread
    void loadAccomplishments(){

        if (accomplishmentList.size() > 0){

            accomplishmentListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accomplishmentList);
            accomplishmentListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spAccomplishments.setAdapter(accomplishmentListAdapter);
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
    void spQuarter(boolean selected, String item){
        if(selected){
            if(!item.contentEquals("Filter Years")){
                quarter = Integer.parseInt(item);

            }

        }
    }
    @ItemSelect
    void spAccomplishments(boolean selected, int position){
        if(selected){
            accomplishmentID = accomplishmentIDs.get(position);
        }

    }
    @ItemSelect
    void spGender(boolean selected, String item){
        if(selected){
            if(!item.contentEquals("Filter Years")){
                gender = item;

            }

        }

    }
    @Background
    void getAccomplishmentIdAsync(String title) {
        try{
            accomplishmentID = accomplishmentClient.getID(ip,port,title);
        }catch (RestClientException ex){
            Log.e("Rest Error",ex.getMessage());
        }

    }

    @Click
    void btnSave(){
        QuarterlyReportModel model = new QuarterlyReportModel();
        model.setAccomplishmentID(accomplishmentID);
        model.setCount(Integer.valueOf(etTally.getText().toString()));
        model.setUserID(1);
        model.setYear(year);
        model.setQuarter(quarter);
        model.setGender(gender);
        if(id > 0){
            editAsync(id,model);
        }else{
            addAsync(model);
        }

    }
    @Background
    void addAsync(QuarterlyReportModel model) {
        try{
            quarterlyReportClient.add(ip,port,model);
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
    void editAsync(int id, QuarterlyReportModel model) {
        try{
            quarterlyReportClient.edit(ip,port,id,model);
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
            quarterlyReportClient.delete(ip,port,id);
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

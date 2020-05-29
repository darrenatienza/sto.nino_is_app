package com.bsu.stoninois.activities;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bsu.stoninois.R;
import com.bsu.stoninois.adapters.BasicAdapter;
import com.bsu.stoninois.api.interfaces.CommonHealthProfileClient;
import com.bsu.stoninois.api.models.AccomplishmentModel;
import com.bsu.stoninois.api.models.CommonHealthProfileModel;
import com.bsu.stoninois.fragments.BasicFormDialog;
import com.bsu.stoninois.fragments.BasicFormDialog_;
import com.bsu.stoninois.misc.Prefs_;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InjectMenu;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_common_health_profile)
@OptionsMenu(R.menu.menu_main)
public class CommonHealthProfileActivity extends AppCompatActivity implements BasicFormDialog_.EditNameDialogListener {

    final int LOAD_LIST = 1;
    @ViewById
    RecyclerView recyclerView;

    @RestService
    CommonHealthProfileClient commonHealthProfileClient;

    private List<AccomplishmentModel> list;
    private SearchView searchView;
    private LinearLayoutManager layoutManager;

    private ArrayList<BasicAdapter> adapterList;
    private FastItemAdapter<BasicAdapter> fastAdapter;
    private String criteria;
    private BasicFormDialog formDialog;
    private int commonHealthProfileID;
    // Preferences
    @Pref
    Prefs_ prefs;
    private String ip;
    private String port;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void afterViews(){

        initVariables();
        initViews();
        initActions();
        loadListAsync();
    }
    void initVariables(){

        ip = prefs.ipAddress().get();
        port = prefs.port().get();
        criteria = "";
        list = new ArrayList<>();
        fastAdapter = new FastItemAdapter<>();
        adapterList = new ArrayList<>();
    }
    void initViews(){
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setAdapter(fastAdapter);
        fastAdapter.withSelectable(true);


    }
    void initActions() {
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<BasicAdapter>() {
            @Override
            public boolean onClick(View v, IAdapter<BasicAdapter> adapter, BasicAdapter item, int position) {
                edit(item.getId());
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Background
    void edit(int id) {
        try{
            commonHealthProfileID = id;
            CommonHealthProfileModel commonHealthProfileModel = commonHealthProfileClient.get(ip,port,id);
            showFormDialog(commonHealthProfileModel);
        }catch (RestClientException e){
            Log.e("Rest error",e.getMessage());
            showErrorMessage(e.getMessage());
        }

    }
    @UiThread
    void showFormDialog(CommonHealthProfileModel model) {
        formDialog = new BasicFormDialog_();
        formDialog.setFormTitle("Common Health Profile");
        formDialog.setId(model.getId());
        formDialog.setTitle(model.getTitle());
        formDialog.show(getSupportFragmentManager(),"dialog");


    }

    @Background
    void loadListAsync(){
        try {
            if(criteria.isEmpty()){
                criteria = "All";
            }
            list = commonHealthProfileClient.getAllByCriteria(ip,port,criteria);
            loadList();
        } catch (RestClientException e){
            Log.e("Rest error",e.getMessage());
            showErrorMessage(e.getMessage());
        }
    }
    @UiThread
    void loadList(){
        adapterList.clear();
        if (list.size() == 0){
            Toast.makeText(this,"No record found!",Toast.LENGTH_SHORT).show();
        }else{

            int index = 0;
            for (AccomplishmentModel m :
                    list) {
                index++;
                BasicAdapter adapter = new BasicAdapter();
                adapter.setId(m.getId());
                adapter.setIndex(index);
                adapter.setTitle(m.getTitle());
                adapterList.add(adapter);
            }
        }
        fastAdapter.setNewList(adapterList);



    }
    @InjectMenu
    void setMenu(Menu myMenu){
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)myMenu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //showBackButton(false);
                criteria = "";
                loadListAsync();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                criteria = query;
                loadListAsync();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when query submitted
                Log.e("","");
                return false;
            }
        });
    }


    @Click
    void add() {
        commonHealthProfileID =0;
        showFormDialog(new CommonHealthProfileModel());
    }

    @OnActivityResult(LOAD_LIST)
    void onResult(int resultCode) {
        if(resultCode == RESULT_OK){
            loadListAsync();
        }
    }

    @Override
    public void onFinishEditDialog(String inputText) {

        CommonHealthProfileModel model = new CommonHealthProfileModel();
        model.setTitle(inputText);
        saveAsync(model);


    }

    @Override
    public void onActionDelete() {
        if(commonHealthProfileID > 0){
            deleteAsync();
        }
    }
    @Background
    void deleteAsync() {
        try{
            commonHealthProfileClient.delete(ip,port,commonHealthProfileID);
            loadListAsync();
        }catch (RestClientException e){
            Log.e("Rest error",e.getMessage());
            showErrorMessage(e.getMessage());
        }


    }

    @Background
    void saveAsync(CommonHealthProfileModel model) {
        try{
            if(commonHealthProfileID > 0){
                commonHealthProfileClient.edit(ip,port,commonHealthProfileID,model);
            }else{
                commonHealthProfileClient.add(ip,port,model);
            }

            loadListAsync();
        }catch (RestClientException e){
            Log.e("Rest error",e.getMessage());
            showErrorMessage(e.getMessage());
        }

    }
    @UiThread
    void showErrorMessage(String error) {
        Toast.makeText(this,error,Toast.LENGTH_LONG).show();
    }
}

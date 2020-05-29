package com.bsu.stoninois.activities;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bsu.stoninois.R;
import com.bsu.stoninois.adapters.HealthDataBoardAdapter;
import com.bsu.stoninois.adapters.QuarterlyReportAdapter;
import com.bsu.stoninois.api.interfaces.HealthDataBoardClient;
import com.bsu.stoninois.api.interfaces.QuarterlyReportClient;
import com.bsu.stoninois.api.models.HealthDataBoardModel;
import com.bsu.stoninois.api.models.QuarterlyReportModel;
import com.bsu.stoninois.misc.Prefs_;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InjectMenu;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_quarterly_report_list)
@OptionsMenu(R.menu.menu_main)
public class QuarterlyReportListActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;
    @ViewById
    FloatingActionButton fab;
    final int LOAD_LIST = 1;
    @ViewById
    RecyclerView recyclerView;

    @RestService
    QuarterlyReportClient quarterlyReportClient;
    @ViewById
    Spinner spYears;

    @ViewById
    Spinner spQuarter;

    private List<QuarterlyReportModel> quarterlyReportModelList;
    private SearchView searchView;
    private LinearLayoutManager layoutManager;
    private ArrayList<QuarterlyReportAdapter> adapterList;
    private FastItemAdapter<QuarterlyReportAdapter> fastAdapter;
    private ArrayAdapter<String> yearsAdapter;
    private ArrayAdapter<String> quarterAdapter;
    private String criteria;
    private int year;
    private int quarter;

    // Preferences
    @Pref
    Prefs_ prefs;
    private String ip;
    private String port;
    @AfterViews
    void afterViews(){

        initVariables();
        initViews();
        initActions();
        loadListBackGround();
    }

    @Click
    void fab(){
        QuarterlyReportUpdateActivity_.intent(this).startForResult(LOAD_LIST);
    }

    void initVariables(){
        ip = prefs.ipAddress().get();
        port = prefs.port().get();
        criteria = "";
        year = 2020;
        quarter = 1;
        quarterlyReportModelList = new ArrayList<>();
        fastAdapter = new FastItemAdapter<>();
        adapterList = new ArrayList<>();
        yearsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.years));
        quarterAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.quarters));
    }
    void initViews(){
        setSupportActionBar(toolbar);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setAdapter(fastAdapter);
        fastAdapter.withSelectable(true);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYears.setAdapter(yearsAdapter);
        spQuarter.setAdapter(quarterAdapter);

    }
    void initActions() {
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<QuarterlyReportAdapter>() {
            @Override
            public boolean onClick(View v, IAdapter<QuarterlyReportAdapter> adapter, QuarterlyReportAdapter item, int position) {
                edit(item.getId());
                return false;
            }
        });

    }

    private void edit(int id) {
        QuarterlyReportUpdateActivity_.intent(this).id(id).startForResult(LOAD_LIST);
    }

    @Background
    void loadListBackGround(){
        try {
            if(criteria.isEmpty()){
                criteria = "All";
            }
            quarterlyReportModelList = quarterlyReportClient.getAll(ip,port,criteria,year,quarter);
            loadList();
        } catch (RestClientException e){
            Log.e("Rest error",e.getMessage());
        }
    }
    @UiThread
    void loadList(){
        if (quarterlyReportModelList.size() == 0){
            fastAdapter.clear();
            Toast.makeText(this,"No record found!",Toast.LENGTH_SHORT).show();
        }else{
            adapterList.clear();

            for (QuarterlyReportModel m :
                    quarterlyReportModelList) {
                QuarterlyReportAdapter adapter = new QuarterlyReportAdapter();
                adapter.setId(m.getId());
                adapter.setCount(m.getCount());
                adapter.setName(m.getUserFullName());
                adapter.setQuarter(m.getQuarter());
                adapter.setYear(m.getYear());
                adapter.setTitle(m.getAccomplishmentTitle());
                adapterList.add(adapter);
            }
            fastAdapter.setNewList(adapterList);
        }



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
                loadListBackGround();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                criteria = query;
                loadListBackGround();
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

    @ItemSelect
    void spYears(boolean selected, String item){
        if(selected){
            if(!item.contentEquals("Filter Years")){
                year = Integer.parseInt(item);
                loadListBackGround();
            }

        }
    }
    @ItemSelect
    void spQuarter(boolean selected, String item){
        if(selected){
            if(!item.contentEquals("Filter Quarter")){
                quarter = Integer.parseInt(item);
                loadListBackGround();
            }

        }
    }


    @OnActivityResult(LOAD_LIST)
    void onResult(int resultCode) {
        if(resultCode == RESULT_OK){
            loadListBackGround();
        }

    }
}

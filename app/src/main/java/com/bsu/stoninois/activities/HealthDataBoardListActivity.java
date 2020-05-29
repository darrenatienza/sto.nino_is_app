package com.bsu.stoninois.activities;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bsu.stoninois.R;
import com.bsu.stoninois.adapters.HealthDataBoardAdapter;
import com.bsu.stoninois.api.interfaces.HealthDataBoardClient;
import com.bsu.stoninois.api.models.HealthDataBoardModel;
import com.bsu.stoninois.misc.Prefs_;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InjectMenu;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_health_data_board_list)
@OptionsMenu(R.menu.menu_main)
public class HealthDataBoardListActivity extends AppCompatActivity {


    final int LOAD_LIST = 1;
    @ViewById
    RecyclerView recyclerView;

    @RestService
    HealthDataBoardClient healthDataBoardClient;
    @ViewById
    Spinner spYears;
    private List<HealthDataBoardModel> list;
    private SearchView searchView;
    private LinearLayoutManager layoutManager;

    private ArrayList<HealthDataBoardAdapter> adapterList;
    private FastItemAdapter<HealthDataBoardAdapter> fastAdapter;
    private ArrayAdapter<String> yearsAdapter;
    private String criteria;
    private int year;
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
    void initVariables(){
        ip = prefs.ipAddress().get();
        port = prefs.port().get();
        criteria = "";
        year = 2020;
        list = new ArrayList<>();
        fastAdapter = new FastItemAdapter<>();
        adapterList = new ArrayList<>();
        yearsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.years));
    }
    void initViews(){
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setAdapter(fastAdapter);
        fastAdapter.withSelectable(true);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYears.setAdapter(yearsAdapter);

    }
    void initActions() {
        fastAdapter.withOnClickListener(new FastAdapter.OnClickListener<HealthDataBoardAdapter>() {
            @Override
            public boolean onClick(View v, IAdapter<HealthDataBoardAdapter> adapter, HealthDataBoardAdapter item, int position) {
                edit(item.getId());
                return false;
            }
        });

    }

    private void edit(int id) {
        HealthDataBoardUpdateActivity_.intent(this).id(id).startForResult(LOAD_LIST);
    }

    @Background
    void loadListBackGround(){
        try {
            if(criteria.isEmpty()){
                criteria = "All";
            }
            list = healthDataBoardClient.getHealthDataBoards(ip,port,criteria,year);
            loadList();
        } catch (RestClientException e){
            Log.e("Rest error",e.getMessage());
        }
    }
    @UiThread
    void loadList(){
        if (list.size() == 0){
            Toast.makeText(this,"No record found!",Toast.LENGTH_SHORT).show();
        }else{
            adapterList.clear();

            for (HealthDataBoardModel m :
                    list) {
                HealthDataBoardAdapter adapter = new HealthDataBoardAdapter();
                adapter.setId(m.getId());
                adapter.setCount(m.getCount());
                adapter.setName(m.getUserName());
                adapter.setTitle(m.getCommonHealthProfileTitle());
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
    @Click
    void add(){
        HealthDataBoardUpdateActivity_.intent(this).startForResult(LOAD_LIST);
    }

    @OnActivityResult(LOAD_LIST)
    void onResult(int resultCode) {
        if(resultCode == RESULT_OK){
            loadListBackGround();
        }

    }


}

package com.icannhas.searchexerciseapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton vFab;
    private Toolbar vToolbar;
    private EditText vSearchBar;
    private ListView vList;

    private CustomAdapter mAdapter;
    private List<JSONTestObject> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = new ArrayList<>();

        setupViews();
        setupListeners();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //loads the unfiltered list
    public void loadData(){
        ApiManager.getInstance(MainActivity.this).apiGetList(new ApiManager.SimpleFetchCallback<List<JSONTestObject>>() {

            @Override
            protected void onFetchFromRemote(List<JSONTestObject> response) {
                mList = response;
                mAdapter.setData(mList);
            }

            @Override
            protected void onError(VolleyError error) {

            }
        });
    }

    //loads the filtered list
    public void loadData(String userId){
        ApiManager.getInstance(MainActivity.this).apiGetList(userId, new ApiManager.SimpleFetchCallback<List<JSONTestObject>>() {

            @Override
            protected void onFetchFromRemote(List<JSONTestObject> response) {
                mList = response;
                mAdapter.setData(mList);
            }

            @Override
            protected void onError(VolleyError error) {

            }
        });
    }

    //all initial UI goes here
    public void setupViews(){

        vToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(vToolbar);

        vFab = (FloatingActionButton) findViewById(R.id.fab);

        vSearchBar = (EditText) findViewById(R.id.searchbar);
        vList = (ListView) findViewById(R.id.list);

        mAdapter = new CustomAdapter(MainActivity.this, mList);
        vList.setAdapter(mAdapter);
    }

    //listeners for the views go here
    public void setupListeners(){

        vFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData(vSearchBar.getText().toString());
            }
        });

        vList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONTestObject object = mList.get(position);
                Toast.makeText(MainActivity.this, object.getId() + " , " + object.getUserId(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

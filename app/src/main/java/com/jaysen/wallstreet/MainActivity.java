package com.jaysen.wallstreet;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jaysen.wallstreet.util.Message;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Message>> {

    private static final String TAG = "MainActivity";
    private RecyclerView         mRecyclerView;
    private StockRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_rv);
        mAdapter = new StockRecyclerAdapter();
        mRecyclerView
                .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
//        getLoaderManager().initLoader(0, null, this);
        new StockAsyncTask(this, mAdapter).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getLoaderManager().destroyLoader(0);
//        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<Message>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 0:
                return new StockAsyncLoader(this);
        }
        return null;
    }


    @Override
    public void onLoadFinished(Loader<ArrayList<Message>> loader, ArrayList<Message> data) {
        mAdapter.setmDatasets(data);
        Log.i(TAG, "onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Message>> loader) {
        mAdapter.setmDatasets(null);
        Log.i(TAG, "onLoaderReset");
    }


}

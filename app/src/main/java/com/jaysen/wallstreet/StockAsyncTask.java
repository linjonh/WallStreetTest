package com.jaysen.wallstreet;

import android.content.Context;
import android.os.AsyncTask;

import com.jaysen.wallstreet.util.Message;
import com.jaysen.wallstreet.util.Stock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Project:   WallStreet
 * className: StockAsyncTask
 * Created on 2016/7/13
 *
 * @author jaysen.lin@foxmail.com
 */
public class StockAsyncTask extends AsyncTask<Void, Void, ArrayList<Message>> {
    StockRecyclerAdapter mAdapter;
    private Context mContext;

    public StockAsyncTask(Context context, StockRecyclerAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    protected ArrayList<Message> doInBackground(Void... params) {
        return getMessages();
    }

    @Override
    protected void onPostExecute(ArrayList<Message> messages) {
        super.onPostExecute(messages);
        mAdapter.setmDatasets(messages);
    }

    private ArrayList<Message> getMessages() {
        ArrayList<Message> dataList = null;
        try {
            InputStream    is      = mContext.getAssets().open("data.json");
            BufferedReader reader  = new BufferedReader(new InputStreamReader(is));
            StringBuilder  builder = new StringBuilder();
            String         tmp;
            while ((tmp = reader.readLine()) != null) {
                builder.append(tmp);
            }
            JSONObject jsonObject = new JSONObject(builder.toString());
            JSONArray  array      = jsonObject.getJSONArray("Messages");
            dataList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                Message    message  = new Message();
                JSONObject itemData = array.getJSONObject(i);
                message.title = itemData.getString("Title");
                message.summary = itemData.getString("Summary");
                message.summary = itemData.getString("Summary");
//                message.likeCount = itemData.getInt("LikeCount");
//                message.createdAt = itemData.getLong("CreatedAt");
                JSONArray stocks = itemData.getJSONArray("Stocks");
                message.stocks = new ArrayList<>();
                for (int j = 0; j < stocks.length(); j++) {
                    Stock      stock     = new Stock();
                    JSONObject stockItem = stocks.getJSONObject(j);
                    stock.name = stockItem.getString("Name");
                    stock.symbol = stockItem.getString("Symbol");
                    message.stocks.add(stock);
                }
                dataList.add(message);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }
}

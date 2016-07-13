package com.jaysen.wallstreet;

import android.content.AsyncTaskLoader;
import android.content.Context;

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
 * className: StockAsyncLoader
 * Created on 2016/7/13
 *
 * @author jaysen.lin@foxmail.com
 * @deprecated since it doesn't work on system
 */
public class StockAsyncLoader extends AsyncTaskLoader<ArrayList<Message>> {
    Context mContext;

    public StockAsyncLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public ArrayList<Message> loadInBackground() {
        ArrayList<Message> dataList = null;
        dataList = getMessages(dataList);
        return dataList;
    }

    private ArrayList<Message> getMessages(ArrayList<Message> dataList) {
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

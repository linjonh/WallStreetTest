package com.jaysen.wallstreet;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jaysen.wallstreet.util.HSJsonUtil;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    RequestQueue mRequestQueue;
    public static final String TAG = "StockAsyncTask";
    private ProgressDialog mProgressDialog;

    public StockAsyncTask(Context context, StockRecyclerAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.show();
    }

    @Override
    protected ArrayList<Message> doInBackground(Void... params) {
        return getMessages();
    }

    @Override
    protected void onPostExecute(ArrayList<Message> messages) {
        super.onPostExecute(messages);
        mProgressDialog.hide();
        if (mAdapter != null) {
            mAdapter.setmDatasets(messages);
        }
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
            final ArrayList<String> en_prod_codeList = new ArrayList<>();
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
                String en_prod_code = "";
                for (int j = 0; j < stocks.length(); j++) {
                    Stock      stock     = new Stock();
                    JSONObject stockItem = stocks.getJSONObject(j);
                    stock.name = stockItem.getString("Name");
                    stock.symbol = stockItem.getString("Symbol");
                    en_prod_code += stock.symbol + ",";
                    message.stocks.add(stock);
                }
                dataList.add(message);
                en_prod_codeList.add(en_prod_code);
            }
            final CountDownLatch     countDownLatch = new CountDownLatch(dataList.size());
            ThreadPoolExecutor       executor       = new ThreadPoolExecutor(4, 8, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
            final ArrayList<Message> finalDataList  = dataList;
            for (int i = 0; i < dataList.size(); i++) {
                final int finalI = i;
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        requestStockRate(finalDataList.get(finalI), en_prod_codeList
                                .get(finalI), countDownLatch);
                    }
                });
            }
            countDownLatch.await();
            executor.shutdown();
        } catch (IOException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private void requestStockRate(final Message message, String en_prod_code, final CountDownLatch countDownLatch) {
        String url = "https://bao.wallstreetcn.com/q/quote/v1/real";
        url += "?en_prod_code=" + en_prod_code + "&fields=prod_name,px_change,last_px,px_change_rate,trade_status";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                message.stocks = HSJsonUtil.getRealStockList(response.toString(), "snapshot");
                Log.i(TAG, "response: " + response.toString());
                countDownLatch.countDown();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.toString());
            }
        });
        jsonObjectRequest.setTag(TAG);
        mRequestQueue.add(jsonObjectRequest);
    }

    public void releaseAllRequest() {
        mRequestQueue.cancelAll(TAG);
    }
}

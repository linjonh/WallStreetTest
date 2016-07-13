package com.jaysen.wallstreet;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaysen.wallstreet.util.Message;

/**
 * Project:   WallStreet
 * className: StockVH
 * Created on 2016/7/13
 *
 * @author jaysen.lin@foxmail.com
 */
public class StockVH extends RecyclerView.ViewHolder {
    private static final String TAG = "stock";
    TextView     titleMsgTv;
    TextView     summaryTv;
    //    TextView stockOneTv;
//    TextView stockTwoTv;
//    TextView stockThreeTv;
    LinearLayout linearLayout;

    public StockVH(View itemView) {
        super(itemView);

        titleMsgTv = (TextView) itemView.findViewById(R.id.massage_tv);
        summaryTv = (TextView) itemView.findViewById(R.id.summary_tv);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.stocks_ll);
//        stockOneTv = (TextView) itemView.findViewById(R.id.stock_one_tv);
//        stockTwoTv = (TextView) itemView.findViewById(R.id.stock_two_tv);
//        stockThreeTv = (TextView) itemView.findViewById(R.id.stock_three_tv);
    }

    public void bindItem(Message msg) {
        titleMsgTv.setText(msg.title);
        summaryTv.setText(msg.summary);

//        stockOneTv.setText(getString(msg, 0));
//        stockTwoTv.setText(getString(msg, 1));
//        stockThreeTv.setText(getString(msg, 2));

        for (int i = 0; i < msg.stocks.size(); i++) {
            TextView textView = newTextView();
            textView.setText(getString(msg, i));
            linearLayout.addView(textView);
        }

    }

    private TextView newTextView() {
        TextView textView = new TextView(linearLayout.getContext());
        textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_stock_down, 0, 0, 0);
        int dpi = (int) textView.getResources().getDisplayMetrics().density;
        textView.setCompoundDrawablePadding(5*dpi);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.setMargins(0, 0, 10 * dpi, 0);
        textView.setLayoutParams(params);
        return textView;
    }

    @NonNull
    private String getString(Message msg, int index) {
        logStockChangeInfo(msg, index);
        return linearLayout.getContext().getString(R.string.stock_name_rate, msg.stocks
                .get(index).name, msg.stocks.get(index).px_change_rate);
    }

    private void logStockChangeInfo(Message msg, int index) {
        Log.i(TAG, msg.stocks.get(index).symbol + " " + msg.stocks.get(index).px_change_rate);
    }
}

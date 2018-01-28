package com.tistory.qlyh8.calculator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tistory.qlyh8.calculator.R;
import com.tistory.qlyh8.calculator.model.HistoryObejct;
import com.tistory.qlyh8.calculator.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2018-01-28.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryObejct> res;
    private LayoutInflater inflater;

    public HistoryAdapter(Context context, List<HistoryObejct> res){
        inflater = LayoutInflater.from(context);
        this.res = res;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.viewUtils = new ViewUtils(inflater.getContext(), holder.historyItem);
        String calc = res.get(position).getCalc();

        //분수 데이터 넣기 ?.?
        for (int i = 0; i < calc.length(); i++) {
            holder.temp.add(String.valueOf(calc.charAt(i)));
            if(i==0)
                holder.viewUtils.setNumTextView(holder.temp,String.valueOf(calc.charAt(i)));
            else
                holder.viewUtils.appendTextView(holder.temp, "calcTextView", String.valueOf(calc.charAt(i)));
        }

    }

    @Override
    public int getItemCount() {
        return res.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.history_item) LinearLayout historyItem;
        private ViewUtils viewUtils;
        private ArrayList<String> temp;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            temp = new ArrayList<>();
        }
    }
}

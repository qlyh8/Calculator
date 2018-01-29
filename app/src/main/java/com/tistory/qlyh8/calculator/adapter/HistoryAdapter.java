package com.tistory.qlyh8.calculator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tistory.qlyh8.calculator.R;
import com.tistory.qlyh8.calculator.model.HistoryObject;
import com.tistory.qlyh8.calculator.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2018-01-28.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryObject> res;
    private LayoutInflater inflater;

    public HistoryAdapter(Context context, List<HistoryObject> res){
        inflater = LayoutInflater.from(context);
        this.res = res;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrayList<String> calc = res.get(position).getCalc();

        //분수 데이터 넣기 ?.?
        // setNumTextView: "calcTextView + calc.size()" 라는 태그를 가진 텍스트 뷰에 string 을 생성
        // calc 의 size 가 변하지 않아야 해당 텍스트 뷰에 텍스트를 append 할 수 있다.
        for (int i = 0; i < calc.size(); i++) {
            if(i==0)
                holder.viewUtils.setNumTextView(calc, String.valueOf(calc.get(i)));
            else
                holder.viewUtils.appendTextView(calc, "calcTextView", String.valueOf(calc.get(i)));
        }
    }

    @Override
    public int getItemCount() {
        return res.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.history_item) LinearLayout historyItem;
        private ViewUtils viewUtils;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            viewUtils = new ViewUtils(inflater.getContext(), historyItem);
        }
    }
}

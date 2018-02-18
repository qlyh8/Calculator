package com.tistory.qlyh8.calculator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private ViewUtils viewUtils;

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

        for (int i = 0; i < calc.size(); i++) {
            if(calc.get(i).contains("@")){
                if(calc.get(i).contains("#")){
                    String fractionWithWhole[] = calc.get(i).split("#");    // 0: 대분수  1: 분모,분자
                    String fraction[] = fractionWithWhole[1].split("@");    //0: 분모   1: 분자
                    viewUtils.setFractionHistoryTextView(i,
                            fraction[0].replaceAll("±", "－"),
                            fraction[1].replaceAll("±", "－"),
                            fractionWithWhole[0].replaceAll("±", "－"));
                }
                else{
                    String fraction[] = calc.get(i).split("@");
                    viewUtils.setFractionHistoryTextView(i,
                            fraction[0].replaceAll("±", "－"),
                            fraction[1].replaceAll("±", "－"), "");
                }
            } else {
                viewUtils.setNumTextView(calc,
                        String.valueOf(calc.get(i)).replaceAll("±", "－"));
            }
        }
        holder.historyResult.setText("= " + res.get(position).getResult());

    }

    @Override
    public int getItemCount() {
        return res.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.history_item) LinearLayout historyItem;
        @BindView(R.id.history_result) TextView historyResult;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            viewUtils = new ViewUtils(inflater.getContext(), historyItem);
        }
    }
}

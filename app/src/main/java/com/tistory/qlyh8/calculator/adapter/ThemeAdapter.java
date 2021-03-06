package com.tistory.qlyh8.calculator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tistory.qlyh8.calculator.R;
import com.tistory.qlyh8.calculator.model.ThemeObject;
import com.tistory.qlyh8.calculator.utils.PreferenceUtils;
import com.tistory.qlyh8.calculator.utils.ThemeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2018-01-28.
 */

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

    public interface OnThemeClickListener {
        void setThemeValue(int num);
    }

    private OnThemeClickListener onThemeClickListener;
    private List<ThemeObject> res;
    private LayoutInflater inflater;
    private PreferenceUtils preferenceUtils;

    public ThemeAdapter(Context context, List<ThemeObject> res, OnThemeClickListener onThemeClickListener){
        inflater = LayoutInflater.from(context);
        this.res = res;
        this.onThemeClickListener = onThemeClickListener;
        preferenceUtils = new PreferenceUtils(context);
        preferenceUtils.setupSharedPreferences();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.theme_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.themeImage.setImageDrawable(res.get(position).getImage());
        holder.themeText.setText(res.get(position).getText());
        holder.themeText.setTextColor(inflater.getContext().getResources().getColor(ThemeUtil.themeNumTextColor));
        holder.themeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceUtils.setVibration();
                onThemeClickListener.setThemeValue(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return res.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.theme_image) ImageView themeImage;
        @BindView(R.id.theme_text) TextView themeText;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}

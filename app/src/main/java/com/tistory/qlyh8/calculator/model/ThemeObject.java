package com.tistory.qlyh8.calculator.model;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by cmtyx on 2018-01-28.
 */

public class ThemeObject {
    private Drawable image;
    private String text;

    public ThemeObject(Drawable image, String text) {
        this.image = image;
        this.text = text;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

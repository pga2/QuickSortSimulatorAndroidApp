package com.ledzinygamedevelopment.quicksortsimulator.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.ledzinygamedevelopment.quicksortsimulator.R;
import com.ledzinygamedevelopment.quicksortsimulator.Simulation;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ListViewCreator {

    private LinkedHashMap<Integer, String> numbers;
    private final int NUMBER_INPUT_WIDTH_MULTIPLIER = 9;
    private GradientDrawable drawable;
    private int allNumbersSize = 0;

    public void createButtons(RelativeLayout relativeLayout, View view, LinkedHashMap<Integer, String> numbersForCreator, FragmentActivity activity, Resources resources, Context context, int rowChanger) {
        if(numbersForCreator.size() > allNumbersSize) {
            allNumbersSize = numbersForCreator.size();
        }
        if(numbers != null) {
            for(int i = 0; i < numbers.size(); i++) {
                relativeLayout.removeView(relativeLayout.findViewById(Simulation.<String>getByIndex(numbers).get(i)));
            }
        }
        numbers = numbersForCreator;
        if(!(numbers.size() < 1) && !(numbers.get(Simulation.<String>getByIndex(numbersForCreator).get(0)).equals(""))) {

            LinkedList<LinkedHashMap<Integer, String>> eachNumbersRow = new LinkedList<>();
            LinkedList<Float> eachRowWidth = new LinkedList<>();

            float tempNumbersWidth = 0;
            int currentArrayPosition = 0;
            for (int i = 0; i < numbers.size(); i++) {
                tempNumbersWidth += numbers.get(Simulation.<String>getByIndex(numbersForCreator).get(i)).length() * NUMBER_INPUT_WIDTH_MULTIPLIER + 40;
                if (tempNumbersWidth > 380 || i + 1 == numbers.size()) {
                    LinkedHashMap<Integer, String> tempOneRowNumbers = new LinkedHashMap<>();
                    for (int j = currentArrayPosition; j <= i; j++) {
                        tempOneRowNumbers.put(Simulation.<String>getByIndex(numbersForCreator).get(j), numbers.get(Simulation.<String>getByIndex(numbersForCreator).get(j)));
                    }
                    eachNumbersRow.add(tempOneRowNumbers);
                    eachRowWidth.add(tempNumbersWidth);
                    tempNumbersWidth = 0;
                    currentArrayPosition = i + 1;
                }
            }
            int row = eachNumbersRow.size() + rowChanger;
            LinkedList<TextView> allTextViews = new LinkedList<>();
            for (int i = 0; i < eachNumbersRow.size(); i++) {
                allTextViews.addAll(createEachRowOfNumbers(eachNumbersRow.get(i), --row, activity, resources, context));
            }

            for(int i = 1; i <= allNumbersSize; i++) {
                for(TextView textView : allTextViews) {
                    if(textView.getId() == i) {
                        try {
                            relativeLayout.addView(textView, textView.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    private LinkedList<TextView> createEachRowOfNumbers(LinkedHashMap<Integer, String> numbersPart, int row, FragmentActivity activity, Resources resources, Context context) {
        LinkedList<TextView> textViews = new LinkedList<>();
        for(int i = 0; i < numbersPart.size(); i++) {
            TextView textView = new TextView(activity);


            textView.setId(Simulation.getByIndex(numbersPart).get(i)); //Set id to remove in the future.

            textView.setPadding(10,10,10,10);

            textView.setBackgroundResource(R.drawable.rounded_croners);
            drawable = (GradientDrawable) textView.getBackground();
            int nightModeFlags = context.getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK;
            if(nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                drawable.setColor(resources.getColor(R.color.purple_500));

                textView.setTextColor(resources.getColor(R.color.white));
            } else {
                drawable.setColor(resources.getColor(R.color.purple_200));
                textView.setTextColor(resources.getColor(R.color.black));
            }
            textView.setText(numbersPart.get(Simulation.getByIndex(numbersPart).get(i)));
            float textViewWidht = (float) Resources.getSystem().getDisplayMetrics().widthPixels * ((float) numbersPart.get(Simulation.<String>getByIndex(numbersPart).get(i)).length() * NUMBER_INPUT_WIDTH_MULTIPLIER) / 480f;

            float distFromRightScreenSide = 0;
            for(int j = 0; j < i; j++) {
                distFromRightScreenSide += (((float) Resources.getSystem().getDisplayMetrics().widthPixels * (float) (numbersPart.get(Simulation.<String>getByIndex(numbersPart).get(j)).length()*NUMBER_INPUT_WIDTH_MULTIPLIER) / 480f)+((float) Resources.getSystem().getDisplayMetrics().widthPixels * 0.0833f));
            }

            RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            lParams.height = Resources.getSystem().getDisplayMetrics().heightPixels/15;
            lParams.topMargin = Resources.getSystem().getDisplayMetrics().heightPixels / 2 - (Resources.getSystem().getDisplayMetrics().heightPixels * row / 13);
            lParams.leftMargin = (int) distFromRightScreenSide;
            lParams.rightMargin = (int) (((float) Resources.getSystem().getDisplayMetrics().widthPixels - (distFromRightScreenSide) - ((textViewWidht + Resources.getSystem().getDisplayMetrics().widthPixels * 70 / 480))));
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(lParams);

            textViews.add(textView);
        }
        return textViews;
    }

    public int getNumbersSize() {
        return numbers.size();
    }
}


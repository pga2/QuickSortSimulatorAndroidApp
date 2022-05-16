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
    private LinkedList<LinkedList<TextView>> lineTextViews;
    private int linesId;

    public void createButtons(RelativeLayout relativeLayout, View view, LinkedHashMap<Integer, String> numbersForCreator, FragmentActivity activity, Resources resources, Context context, int rowChanger, LinkedList<LinkedHashMap<Integer, Integer>> allArraysInCalculation) {
        if(numbersForCreator.size() > allNumbersSize) {
            allNumbersSize = numbersForCreator.size();
        }

        if(numbers != null) {
            for(int i = 0; i < numbers.size(); i++) {
                relativeLayout.removeView(relativeLayout.findViewById(Simulation.<String>getByIndex(numbers).get(i)));
            }
        }

        if(lineTextViews == null)
            lineTextViews = new LinkedList<>();
        for(LinkedList<TextView> textViewEachRow : lineTextViews) {
            for(TextView textView : textViewEachRow) {
                for (int i = 0; i < relativeLayout.getChildCount(); i++) {
                    if (textView.equals(relativeLayout.getChildAt(i))) {
                        relativeLayout.removeView(relativeLayout.getChildAt(i));
                    }
                }
            }
        }

        lineTextViews = new LinkedList<>();
        numbers = numbersForCreator;
        linesId = numbersForCreator.size()+1;
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
                allTextViews.addAll(createEachRowOfNumbers(eachNumbersRow.get(i), --row, activity, resources, context, relativeLayout, allArraysInCalculation));
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
            for(LinkedList<TextView> textViewEachRow : lineTextViews) {
                for (TextView textView : textViewEachRow) {
                    try {
                        //System.out.println("kkkkk");
                        relativeLayout.addView(textView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private LinkedList<TextView> createEachRowOfNumbers(LinkedHashMap<Integer, String> numbersPart, int row, FragmentActivity activity, Resources resources, Context context, RelativeLayout relativeLayout, LinkedList<LinkedHashMap<Integer, Integer>> allArraysInCalculation) {
        LinkedList<TextView> textViews = new LinkedList<>();
        LinkedList<TextView> lineTextViewsEach = new LinkedList<>();
        int lparamsRightFromBefore = 0;
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

            //lines showing each array calculation
            LinkedList<Integer> keysToCreate = new LinkedList<>();
            if(allArraysInCalculation != null) {
                for(LinkedHashMap<Integer, Integer> eachList : allArraysInCalculation) {
                   keysToCreate.add(Simulation.getByIndex(eachList).get(0));
                }
            }
            if(allArraysInCalculation != null) {
                for(Integer key :keysToCreate) {
                    if (key == Simulation.getByIndex(numbersPart).get(i)) {
                        TextView textViewLine = new TextView(activity);
                        textViewLine.setPadding(10, 10, 10, 10);

                        if(nightModeFlags == Configuration.UI_MODE_NIGHT_NO)
                            Simulation.setColor(textViewLine, Color.BLACK);
                        else
                            Simulation.setColor(textViewLine, Color.WHITE);
                        RelativeLayout.LayoutParams lParamsLines = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT);
                        lParamsLines.height = Resources.getSystem().getDisplayMetrics().heightPixels / 15;
                        lParamsLines.topMargin = Resources.getSystem().getDisplayMetrics().heightPixels / 2 - (Resources.getSystem().getDisplayMetrics().heightPixels * row / 13);
                        int distBetweenTextViews = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.08525f);
                        lParamsLines.leftMargin = Resources.getSystem().getDisplayMetrics().widthPixels - lparamsRightFromBefore - distBetweenTextViews;
                        lParamsLines.rightMargin = Resources.getSystem().getDisplayMetrics().widthPixels - lParams.leftMargin - distBetweenTextViews;
                        textViewLine.setGravity(Gravity.CENTER);
                        textViewLine.setLayoutParams(lParamsLines);
                        lineTextViewsEach.add(textViewLine);
                    }
                }
            }
            lparamsRightFromBefore = lParams.rightMargin;
            textViews.add(textView);
        }
        System.out.println(textViews);
        lineTextViews.add(lineTextViewsEach);
        return textViews;
    }

    public int getNumbersSize() {
        if(numbers != null)
            return numbers.size();
        else
            return 0;
    }
}


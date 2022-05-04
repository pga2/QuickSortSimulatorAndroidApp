package com.ledzinygamedevelopment.quicksortsimulator;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ledzinygamedevelopment.quicksortsimulator.utils.ListViewCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Simulation extends Fragment {


    private ListViewCreator listViewCreator;
    private SetupAndMainPage setupAndMainPage;

    private enum QuickSortListPart {SHOW_PIWOT, SHOW_GREATER_NUMBERS, MOVE_GREATER_NUMBERS}

    ;
    private QuickSortListPart currentState;
    private LinkedHashMap<Integer, Integer> numbersWithId;
    private LinkedList<LinkedHashMap<Integer, Integer>> allRequrencyQuickSortData;
    private final int LOW_KEY_POSITION = -2;
    private final int HIGH_KEY_POSITION = -3;
    private int currentSortingArray;
    private LinkedHashMap<Integer, String> mapForCreator;
    private RelativeLayout relativeLayout;
    private View view;


    public Simulation(SetupAndMainPage setupAndMainPage) {
        this.setupAndMainPage = setupAndMainPage;
        listViewCreator = new ListViewCreator();
        allRequrencyQuickSortData = new LinkedList<>();
        numbersWithId = new LinkedHashMap<>();
        currentState = QuickSortListPart.SHOW_PIWOT;
        currentSortingArray = 0;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_simulation, container, false);

        relativeLayout = view.findViewById(R.id.frameLayout);
        listViewCreator.createButtons(relativeLayout, view, setupAndMainPage.getHashMapForCreator(), getActivity(), getResources(), getContext(), -2);
        for (int i = 0; i < setupAndMainPage.getHashMapForCreator().size(); i++) {
            numbersWithId.put(getByIndex(setupAndMainPage.getHashMapForCreator()).get(i), Integer.valueOf(setupAndMainPage.getHashMapForCreator().get(getByIndex(setupAndMainPage.getHashMapForCreator()).get(i))));
        }

        numbersWithId.put(LOW_KEY_POSITION, 0); //key for low = -2, key for high = -3
        numbersWithId.put(HIGH_KEY_POSITION, getByIndex(numbersWithId).size() - 1);
        int low = 0;
        allRequrencyQuickSortData.add(numbersWithId);
        Button start_simulation_button = view.findViewById(R.id.return_to_setup);
        Button next_step_button = view.findViewById(R.id.next_step);
        start_simulation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.whole_simulation_fragment_container, setupAndMainPage);
                ft.commit();
            }
        });

        Button repeat_simulation_button = view.findViewById(R.id.repeat_simulation);
        repeat_simulation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.whole_simulation_fragment_container, new Simulation(setupAndMainPage));
                ft.commit();
            }
        });

        next_step_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allRequrencyQuickSortData.size() > currentSortingArray) {
                    if (allRequrencyQuickSortData.get(currentSortingArray).size() > 3) {

                        switch (currentState) {
                            case SHOW_PIWOT:
                                refreshTextViews(allRequrencyQuickSortData);
                                TextView pivot = relativeLayout.findViewById(allRequrencyQuickSortData.get(currentSortingArray).get(HIGH_KEY_POSITION));
                                setColor(pivot, Color.RED);
                                currentState = QuickSortListPart.SHOW_GREATER_NUMBERS;
                                break;
                            case SHOW_GREATER_NUMBERS:
                                for (int i = 0; i < allRequrencyQuickSortData.get(currentSortingArray).size() - 3; i++) {
                                    if (allRequrencyQuickSortData.get(currentSortingArray).get(getByIndex(allRequrencyQuickSortData.get(currentSortingArray)).get(i)) > allRequrencyQuickSortData.get(currentSortingArray).get(allRequrencyQuickSortData.get(currentSortingArray).get(-3))) {
                                        TextView higherNumber = relativeLayout.findViewById(getByIndex(allRequrencyQuickSortData.get(currentSortingArray)).get(i));
                                        setColor(higherNumber, Color.GREEN);
                                    } else if (allRequrencyQuickSortData.get(currentSortingArray).get(getByIndex(allRequrencyQuickSortData.get(currentSortingArray)).get(i)) < allRequrencyQuickSortData.get(currentSortingArray).get(allRequrencyQuickSortData.get(currentSortingArray).get(-3))) {
                                        TextView higherNumber = relativeLayout.findViewById(getByIndex(allRequrencyQuickSortData.get(currentSortingArray)).get(i));
                                        setColor(higherNumber, Color.GRAY);
                                    }
                                }
                                currentState = QuickSortListPart.MOVE_GREATER_NUMBERS;
                                break;
                            case MOVE_GREATER_NUMBERS:
                                int in = -1;

                                LinkedHashMap<Integer, Integer> newTempMap;
                                newTempMap = allRequrencyQuickSortData.get(currentSortingArray);
                                for (int i = 0; i < newTempMap.size() - 2; i++) {
                                    if (newTempMap.get(getByIndex(newTempMap).get(i)) < newTempMap.get(newTempMap.get(HIGH_KEY_POSITION))) {
                                        in++;
                                        newTempMap = swap(newTempMap, in, i);
                                    }

                                }
                                newTempMap = swap(newTempMap, in + 1, newTempMap.size() - 3);

                                mapForCreator = new LinkedHashMap<>();
                                for (int i = 0; i < currentSortingArray; i++) {
                                    for (int j = 0; j < allRequrencyQuickSortData.get(i).size(); j++) {
                                        if (getByIndex(allRequrencyQuickSortData.get(i)).get(j) >= 0)
                                            mapForCreator.put(getByIndex(allRequrencyQuickSortData.get(i)).get(j), String.valueOf(allRequrencyQuickSortData.get(i).get(getByIndex(allRequrencyQuickSortData.get(i)).get(j))));
                                    }
                                }
                                for (Integer key : newTempMap.keySet()) {
                                    if (key >= 0)
                                        mapForCreator.put(key, String.valueOf(newTempMap.get(key)));
                                }
                                for (int i = 1; i < allRequrencyQuickSortData.size(); i++) {
                                    for (int j = 0; j < allRequrencyQuickSortData.get(i).size(); j++) {
                                        if (getByIndex(allRequrencyQuickSortData.get(i)).get(j) >= 0)
                                            mapForCreator.put(getByIndex(allRequrencyQuickSortData.get(i)).get(j), String.valueOf(allRequrencyQuickSortData.get(i).get(getByIndex(allRequrencyQuickSortData.get(i)).get(j))));
                                    }
                                }
                                listViewCreator.createButtons(relativeLayout, view, mapForCreator, getActivity(), getResources(), getContext(), -2);

                                pivot = relativeLayout.findViewById(allRequrencyQuickSortData.get(currentSortingArray).get(HIGH_KEY_POSITION));
                                setColor(pivot, Color.RED);
                                LinkedHashMap<Integer, Integer> lowPartMap = new LinkedHashMap<>();
                                LinkedHashMap<Integer, Integer> pivotMap = new LinkedHashMap<>();
                                LinkedHashMap<Integer, Integer> highPartMap = new LinkedHashMap<>();
                                for (int i = 0; i < newTempMap.size(); i++) {
                                    if (newTempMap.get(getByIndex(newTempMap).get(i)) >= newTempMap.get(newTempMap.get(HIGH_KEY_POSITION))
                                            && getByIndex(newTempMap).get(i) != newTempMap.get(HIGH_KEY_POSITION)
                                            && (getByIndex(newTempMap).get(i) != HIGH_KEY_POSITION && getByIndex(newTempMap).get(i) != LOW_KEY_POSITION)) {
                                        TextView textView = relativeLayout.findViewById(getByIndex(newTempMap).get(i));
                                        setColor(textView, Color.GREEN);
                                        highPartMap.put(getByIndex(newTempMap).get(i), newTempMap.get(getByIndex(newTempMap).get(i)));
                                    } else if (getByIndex(newTempMap).get(i) != newTempMap.get(HIGH_KEY_POSITION)
                                            && (getByIndex(newTempMap).get(i) != HIGH_KEY_POSITION && getByIndex(newTempMap).get(i) != LOW_KEY_POSITION)) {
                                        lowPartMap.put(getByIndex(newTempMap).get(i), newTempMap.get(getByIndex(newTempMap).get(i)));
                                        TextView textView = relativeLayout.findViewById(getByIndex(newTempMap).get(i));
                                        setColor(textView, Color.GRAY);
                                    } else if (getByIndex(newTempMap).get(i) != HIGH_KEY_POSITION && getByIndex(newTempMap).get(i) != LOW_KEY_POSITION) {
                                        pivotMap.put(getByIndex(newTempMap).get(i), newTempMap.get(getByIndex(newTempMap).get(i)));
                                    }
                                }

                                if (lowPartMap.size() > 1) {
                                    lowPartMap.put(LOW_KEY_POSITION, 0);
                                    lowPartMap.put(HIGH_KEY_POSITION, getByIndex(lowPartMap).get(lowPartMap.size() - 2));
                                }
                                if (highPartMap.size() > 1) {
                                    highPartMap.put(LOW_KEY_POSITION, 0);
                                    highPartMap.put(HIGH_KEY_POSITION, getByIndex(highPartMap).get(highPartMap.size() - 2));
                                }

                                LinkedList<LinkedHashMap<Integer, Integer>> newAllNumbersMap = new LinkedList<>();
                                for (int i = 0; i < currentSortingArray; i++) {
                                    newAllNumbersMap.add(allRequrencyQuickSortData.get(i));
                                }
                                if (lowPartMap.size() > 0)
                                    newAllNumbersMap.add(lowPartMap);
                                newAllNumbersMap.add(pivotMap);
                                if (highPartMap.size() > 0)
                                    newAllNumbersMap.add(highPartMap);
                                allRequrencyQuickSortData.remove(currentSortingArray);
                                for (int i = currentSortingArray; i < allRequrencyQuickSortData.size(); i++) {
                                    newAllNumbersMap.add(allRequrencyQuickSortData.get(i));
                                }
                                allRequrencyQuickSortData = newAllNumbersMap;
                                currentState = QuickSortListPart.SHOW_PIWOT;
                                break;
                        }
                    } else {
                        refreshTextViews(allRequrencyQuickSortData);
                        TextView pivot = relativeLayout.findViewById(getByIndex(allRequrencyQuickSortData.get(currentSortingArray)).get(0));
                        setColor(pivot, Color.RED);
                        final Toast toast = Toast.makeText(getActivity(), "This part of array is to small to sort", Toast.LENGTH_SHORT);
                        toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }
                        }, 200);
                        currentSortingArray++;
                    }
                } else {
                    refreshTextViews(allRequrencyQuickSortData);
                    Toast.makeText(getActivity(), "Array sorted! :)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private static LinkedHashMap swap(LinkedHashMap<Integer, Integer> hashMap, int i, int j) {
        ArrayList<Integer> keySetList = getByIndex(hashMap);
        Collections.swap(keySetList, i, j);
        LinkedHashMap<Integer, Integer> newTempMap2 = new LinkedHashMap<>();
        for (Integer oldSwappedKey : keySetList) {
            newTempMap2.put(oldSwappedKey, hashMap.get(oldSwappedKey));
        }
        return newTempMap2;
    }

    public static <T> ArrayList<Integer> getByIndex(LinkedHashMap<Integer, T> hMap) {
        return new ArrayList<Integer>(hMap.keySet());
    }

    private static void setColor(TextView textView, int color) {
        textView.setBackgroundResource(R.drawable.rounded_croners);
        GradientDrawable drawable = (GradientDrawable) textView.getBackground();
        drawable.setColor(color);
    }

    private void refreshTextViews(LinkedList<LinkedHashMap<Integer, Integer>> list) {
        mapForCreator = new LinkedHashMap<>();
        for (LinkedHashMap<Integer, Integer> eachMap : list) {
            for (Integer key : eachMap.keySet()) {
                if (key >= 0)
                    mapForCreator.put(key, String.valueOf(eachMap.get(key)));
            }
        }
        listViewCreator.createButtons(relativeLayout, view, mapForCreator, getActivity(), getResources(), getContext(), -2);
    }
}
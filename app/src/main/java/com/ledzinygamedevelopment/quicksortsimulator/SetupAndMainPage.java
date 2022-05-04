package com.ledzinygamedevelopment.quicksortsimulator;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.ledzinygamedevelopment.quicksortsimulator.utils.ListViewCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;


public class SetupAndMainPage extends Fragment {

    private EditText edit_numbers_in_array;
    private Button add_numberButton;
    private int textViewId;
    private final int NUMBER_INPUT_WIDTH_MULTIPLIER = 9;
    private LinkedList<String> numbersList;
    private RelativeLayout relativeLayout;
    private View view;
    private ListViewCreator listViewCreator;
    private String[] numbersForCreator;
    private LinkedHashMap<Integer, String> hashMapForCreator;

    public SetupAndMainPage(){
        listViewCreator = new ListViewCreator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setup_and_main_page, container, false);
        Button start_simulation_button = view.findViewById(R.id.start_simulation_button);
        relativeLayout = view.findViewById(R.id.frameLayout2);
        start_simulation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listViewCreator.getNumbersSize() >= 5) {
                    FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                    ft.replace(R.id.whole_simulation_fragment_container, new Simulation(SetupAndMainPage.this));
                    ft.commit();
                } else {
                    Toast.makeText(getActivity(), "You need at least 5 fragments in array", Toast.LENGTH_SHORT).show();
                }
            }
        });
        add_numberButton = view.findViewById(R.id.add_number);
        edit_numbers_in_array = view.findViewById(R.id.edit_numbers_in_array);

        add_numberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numbersForCreator = edit_numbers_in_array.getText().toString().split(",");
                LinkedList<String> tempList = new LinkedList<>(Arrays.asList(numbersForCreator));
                tempList.removeAll(Arrays.asList(""));

                hashMapForCreator = new LinkedHashMap<>();
                int i = 0;
                for(String value : tempList) {
                    i++;
                    hashMapForCreator.put(i, value);
                }
                numbersForCreator = tempList.toArray(new String[0]);
                listViewCreator.createButtons(relativeLayout, view, hashMapForCreator, getActivity(), getResources(), getContext());
            }
        });
        return view;
    }

    public LinkedHashMap<Integer, String> getHashMapForCreator() {
        return hashMapForCreator;
    }
}
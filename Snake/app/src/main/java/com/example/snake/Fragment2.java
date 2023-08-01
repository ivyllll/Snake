package com.example.snake;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import static com.example.snake.R.id.setmodel_rg;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {
    RadioGroup setmodel_rg;
    Button bt_sure;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_fragment2, container, false);
        setmodel_rg  =view.findViewById(R.id.setmodel_rg);
        bt_sure = view.findViewById(R.id.setting_sure2);
        sp = requireActivity().getSharedPreferences("difficulty", Context.MODE_PRIVATE);
        editor = sp.edit();
        setmodel_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.setmodel_rbt1:
                        editor.putInt("model",400);
                        editor.apply();
                        break;

                    case R.id.setmodel_rbt2:
                        editor.putInt("model",200);
                        editor.apply();
                        break;

                    case R.id.setmodel_rbt3:
                        editor.putInt("model",100);
                        editor.apply();
                        break;
                }
            }
        });
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });
        return view;
    }

}

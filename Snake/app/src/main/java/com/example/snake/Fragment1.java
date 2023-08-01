package com.example.snake;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import java.io.Serializable;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {
    private RadioGroup rg;
    Button music_sure,music_back;
    int music = 0;
    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_fragment1, container, false);
        rg = view.findViewById(R.id.rg);
        music_sure = view.findViewById(R.id.music_sure);
        music_back = view.findViewById(R.id.music_back);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.musicl:
                        music = 1;
                        break;
                    case R.id.music2:
                        music = 2;
                        break;
                    case R.id.music3:
                        music = 3;
                        break;
                    case R.id.music4:
                        music = 4;
                        break;
                }
            }
        });
        music_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music==4||music==0){
                    Intent music_to_serviec = new Intent(requireActivity(), SnakeService.class);
                    requireActivity().stopService(music_to_serviec);
                }else {
                    Intent music_to_serviec = new Intent(requireActivity(), SnakeService.class);
                    music_to_serviec.putExtra("music", music);
                    requireActivity().stopService(music_to_serviec);
                    requireActivity().startService(music_to_serviec);
                }
            }
        });
        music_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });
        return view;
    }


}

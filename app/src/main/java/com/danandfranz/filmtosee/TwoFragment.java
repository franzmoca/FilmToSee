package com.danandfranz.filmtosee;

/**
 * Created by Agilulfo on 19/02/2016.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Scroller;

import com.danandfranz.filmtosee.R;


public class TwoFragment extends Fragment{

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false);
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        EditText e = (EditText) view.findViewById(R.id.insertText);
        final Toolbar t =(Toolbar)  getActivity().findViewById(R.id.toolbarGroup);
        Button b=(Button)  getActivity().findViewById(R.id.sendBtn);
        e.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 1);
                RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.relativeLayout);
                r.setVisibility(View.GONE);
                t.setVisibility(View.GONE);
                return false;
            }

        });

        e.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.relativeLayout);
                    r.setVisibility(View.VISIBLE);
                    t.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }


        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.relativeLayout);
                r.setVisibility(View.VISIBLE);
                t.setVisibility(View.VISIBLE);

            }
        });




        e.setScroller(new Scroller(getContext()));
        e.setMaxLines(3);
        e.setVerticalScrollBarEnabled(true);
        e.setMovementMethod(new ScrollingMovementMethod());
    }



}
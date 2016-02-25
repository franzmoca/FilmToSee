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
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Scroller;
import android.widget.TextView;

import com.danandfranz.filmtosee.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TwoFragment extends Fragment{
    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    public boolean BoolForBackButton=false;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       // initControls();



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
                /* PER ANDARE INDIETRO
                if((keyCode == KeyEvent.KEYCODE_BACK) && BoolForBackButton==false){
                    getActivity().finish();
                }
*/
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

                 view.findViewById(R.id.insertText).clearFocus();

            }
        });




        e.setScroller(new Scroller(getContext()));
        e.setMaxLines(3);
        e.setVerticalScrollBarEnabled(true);
        e.setMovementMethod(new ScrollingMovementMethod());



        //initiControls
        messagesContainer = (ListView) getView().findViewById(R.id.messagesContainer);
        messageET = (EditText) getView().findViewById(R.id.insertText);
        sendBtn = (Button) getView().findViewById(R.id.sendBtn);


        RelativeLayout container = (RelativeLayout) getView().findViewById(R.id.container);

        loadDummyHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);

                messageET.setText("");

                displayMessage(chatMessage);
            }
        });
        //end initcontrols
    }







    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Oh! I want to see that!");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("When???");
        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);

        adapter = new ChatAdapter(TwoFragment.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
    }

}
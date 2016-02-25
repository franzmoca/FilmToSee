package com.danandfranz.filmtosee;

/**
 * Created by Agilulfo on 19/02/2016.
 */


        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;

        import com.danandfranz.filmtosee.R;


public class OneFragment extends Fragment{

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View InputFragmentView = inflater.inflate(
                R.layout.fragment_one, container, false);
        final Button btnLike=(Button) InputFragmentView.findViewById(R.id.buttonLike);
        final Button btnUnlike=(Button) InputFragmentView.findViewById(R.id.buttonUnlike);

        final TextView textViewLike=(TextView) InputFragmentView.findViewById(R.id.textLikes);

        final TextView textViewUnlike=(TextView) InputFragmentView.findViewById(R.id.textUnlikes);
        final boolean[] bool = {true};
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bool[0] ==true){
                    btnLike.setBackgroundResource(R.drawable.thumbs_up_selected);
                    int textLike = Integer.parseInt(textViewLike.getText().toString());
                    textLike = textLike + 1;
                    textViewLike.setText("" + textLike);
                    btnLike.setEnabled(false);
                    bool[0] =false;
                }

            }
        });

        btnUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bool[0] ==true) {

                    btnUnlike.setBackgroundResource(R.drawable.thumbs_down_selected);
                    int textUnlike = Integer.parseInt(textViewUnlike.getText().toString());
                    textUnlike = textUnlike + 1;
                    textViewUnlike.setText("" + textUnlike);
                    btnLike.setEnabled(false);
                    bool[0] =false;
                }
            }
        });
        return InputFragmentView;
    }





}
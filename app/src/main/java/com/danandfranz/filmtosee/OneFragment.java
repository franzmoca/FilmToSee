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


public class OneFragment extends Fragment{
    private View InputFragmentView;

    //Attributi Film Details
    private TextView textViewLike;
    private TextView textViewUnlike;
    private TextView title;
    private TextView director;
    private TextView writer;
    private TextView year;
    private TextView genre;
    private TextView runtime;
    private TextView actors;
    private TextView plot;
    private TextView imdbVote;



    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        InputFragmentView = inflater.inflate(
                R.layout.fragment_one, container, false);
        final Button btnLike=(Button) InputFragmentView.findViewById(R.id.buttonLike);
        final Button btnUnlike=(Button) InputFragmentView.findViewById(R.id.buttonUnlike);

        textViewLike = (TextView) InputFragmentView.findViewById(R.id.textLikes);
        textViewUnlike = (TextView) InputFragmentView.findViewById(R.id.textUnlikes);
        title = (TextView) InputFragmentView.findViewById(R.id.title);
        director = (TextView) InputFragmentView.findViewById(R.id.director);
        writer = (TextView) InputFragmentView.findViewById(R.id.writer);
        year = (TextView) InputFragmentView.findViewById(R.id.year);
        genre = (TextView) InputFragmentView.findViewById(R.id.genre);
        runtime = (TextView) InputFragmentView.findViewById(R.id.runtime);
        actors = (TextView) InputFragmentView.findViewById(R.id.actors);
        plot = (TextView) InputFragmentView.findViewById(R.id.plot);
        imdbVote = (TextView) InputFragmentView.findViewById(R.id.imdbVote);


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

    public void setMovieDetails(Film film){

        title.setText(film.getTitle());
        director.setText(film.getDirector());
        writer.setText(film.getWriter());
        year.setText(film.getYear());
        genre.setText(film.getGenre());
        runtime.setText(film.getRuntime());
        actors.setText(film.getActors());
        plot.setText(film.getPlot());
        imdbVote.setText(film.getImdbScore());
    }




}
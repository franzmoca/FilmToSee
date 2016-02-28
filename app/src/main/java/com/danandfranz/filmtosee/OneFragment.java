package com.danandfranz.filmtosee;

/**
 * Created by Agilulfo on 19/02/2016.
 */


        import android.os.Bundle;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.LinearLayoutManager;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.RelativeLayout;
        import android.widget.ScrollView;
        import android.widget.TextView;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.util.ArrayList;

        import it.gmariotti.cardslib.library.internal.Card;
        import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
        import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
        import okhttp3.Call;
        import okhttp3.Callback;
        import okhttp3.FormBody;
        import okhttp3.OkHttpClient;
        import okhttp3.RequestBody;
        import okhttp3.Response;


public class OneFragment extends Fragment{
    OkHttpClient client;

    private View InputFragmentView;
    private RelativeLayout relativeDetails;
    private RelativeLayout relativeFilmAdd;
    private Button btnLike;
    private Button btnUnlike;
    boolean bool;

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
    private String TAG = "OneFragment";


    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        client = new OkHttpClient();
        InputFragmentView = inflater.inflate(
                R.layout.fragment_one, container, false);
        btnLike = (Button) InputFragmentView.findViewById(R.id.buttonLike);
        btnUnlike = (Button) InputFragmentView.findViewById(R.id.buttonUnlike);
        relativeDetails =(RelativeLayout)InputFragmentView.findViewById(R.id.RelativeFilmDetails);
        relativeFilmAdd =(RelativeLayout)InputFragmentView.findViewById(R.id.RelativeAddFilm);
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

        //// TODO
        //prendere numero like e unlike e settarli
        //quando faccio like o unlike chiamata post
        //aggiorno


        return InputFragmentView;
    }

    public void setMovieDetails(final Film film) {

        if (!film.isAdd()) {
            relativeFilmAdd.setVisibility(View.GONE);

            relativeDetails.setVisibility(View.VISIBLE);
            title.setText(film.getTitle());
            director.setText(film.getDirector());
            writer.setText(film.getWriter());
            year.setText(film.getYear());
            genre.setText(film.getGenre());
            runtime.setText(film.getRuntime());
            actors.setText(film.getActors());
            plot.setText(film.getPlot());
            imdbVote.setText(film.getImdbScore());
            textViewLike = (TextView) InputFragmentView.findViewById(R.id.textLikes);
            textViewUnlike = (TextView) InputFragmentView.findViewById(R.id.textUnlikes);


            btnUnlike.setBackgroundResource(R.drawable.thumbs_down_unselected);
            btnLike.setBackgroundResource(R.drawable.thumbs_up_unselected);

           if(film.isLiked()){
               // btnLike.setEnabled(false);
               // btnUnlike.setEnabled(false);
                if(!film.isMyLike()){
                    btnUnlike.setBackgroundResource(R.drawable.thumbs_down_selected);
                    btnLike.setEnabled(false);
                    btnUnlike.setEnabled(true);
                }else{
                    btnLike.setBackgroundResource(R.drawable.thumbs_up_selected);
                    btnLike.setEnabled(true);
                    btnUnlike.setEnabled(false);
                }

               btnLike.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(!film.isLiked()){
                           //DA INVERTIRE NEL MODO GIUSTO GLI ID DOPO CHE FRANZ SI SVEGLIA
                           String groupId=((InsideGroupActivity)getActivity()).getUserRid();
                           String userId=((InsideGroupActivity)getActivity()).getGroupRid();
                           try {

                               tolgoLike(groupId,userId,film.getImdbID(),true);



                               btnLike.setBackgroundResource(R.drawable.thumbs_up_selected);
                               int textLike = Integer.parseInt(textViewLike.getText().toString());
                               textLike = textLike - 1;
                               textViewLike.setText("" + textLike);
                               btnLike.setEnabled(false);
                               btnUnlike.setEnabled(false);
                               film.setIsLiked(false);

                           } catch (IOException e) {
                               e.printStackTrace();
                               getActivity().runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       Snackbar.make(relativeFilmAdd, "Error occured", Snackbar.LENGTH_LONG)
                                               .setDuration(Snackbar.LENGTH_LONG).show();
                                   }
                               });
                           }


                       }

                   }
               });

               btnUnlike.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(!film.isLiked()){
                           //DA INVERTIRE NEL MODO GIUSTO GLI ID DOPO CHE FRANZ SI SVEGLIA
                           String groupId=((InsideGroupActivity)getActivity()).getUserRid();
                           String userId=((InsideGroupActivity)getActivity()).getGroupRid();
                           try {
                               tolgoLike(groupId, userId, film.getImdbID(), false);


                               btnUnlike.setBackgroundResource(R.drawable.thumbs_down_selected);
                               int textUnlike = Integer.parseInt(textViewUnlike.getText().toString());
                               textUnlike = textUnlike - 1;
                               textViewUnlike.setText("" + textUnlike);
                               btnLike.setEnabled(false);
                               btnUnlike.setEnabled(false);
                               film.setIsLiked(false);

                           } catch (IOException e) {
                               e.printStackTrace();
                               getActivity().runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       Snackbar.make(relativeFilmAdd, "Error occured", Snackbar.LENGTH_LONG)
                                               .setDuration(Snackbar.LENGTH_LONG).show();
                                   }
                               });
                           }







                       }
                   }
               });


            }else {
               btnLike.setEnabled(true);
               btnUnlike.setEnabled(true);

               btnLike.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (!film.isLiked()) {
                           //DA INVERTIRE NEL MODO GIUSTO GLI ID DOPO CHE FRANZ SI SVEGLIA
                           String groupId = ((InsideGroupActivity) getActivity()).getUserRid();
                           String userId = ((InsideGroupActivity) getActivity()).getGroupRid();
                           try {

                               setLike(groupId, userId, film.getImdbID(), true);


                               btnLike.setBackgroundResource(R.drawable.thumbs_up_selected);
                               int textLike = Integer.parseInt(textViewLike.getText().toString());
                               textLike = textLike + 1;
                               textViewLike.setText("" + textLike);
                               btnLike.setEnabled(false);
                               btnUnlike.setEnabled(false);
                               film.setIsLiked(false);

                           } catch (IOException e) {
                               e.printStackTrace();
                               getActivity().runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       Snackbar.make(relativeFilmAdd, "Error occured", Snackbar.LENGTH_LONG)
                                               .setDuration(Snackbar.LENGTH_LONG).show();
                                   }
                               });
                           }


                       }

                   }
               });

               btnUnlike.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (!film.isLiked()) {
                           //DA INVERTIRE NEL MODO GIUSTO GLI ID DOPO CHE FRANZ SI SVEGLIA
                           String groupId = ((InsideGroupActivity) getActivity()).getUserRid();
                           String userId = ((InsideGroupActivity) getActivity()).getGroupRid();
                           try {
                               setLike(groupId, userId, film.getImdbID(), false);


                               btnUnlike.setBackgroundResource(R.drawable.thumbs_down_selected);
                               int textUnlike = Integer.parseInt(textViewUnlike.getText().toString());
                               textUnlike = textUnlike + 1;
                               textViewUnlike.setText("" + textUnlike);
                               btnLike.setEnabled(false);
                               btnUnlike.setEnabled(false);
                               film.setIsLiked(false);

                           } catch (IOException e) {
                               e.printStackTrace();
                               getActivity().runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       Snackbar.make(relativeFilmAdd, "Error occured", Snackbar.LENGTH_LONG)
                                               .setDuration(Snackbar.LENGTH_LONG).show();
                                   }
                               });
                           }


                       }
                   }
               });

               textViewLike.setText("" + film.getLike());
               textViewUnlike.setText("" + film.getDislike());
           }
        }else {

            relativeDetails.setVisibility(View.GONE);
            relativeFilmAdd.setVisibility(View.VISIBLE);

            //Sostituisci con qualcosa!
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //Fix Crash
        relativeDetails =(RelativeLayout)InputFragmentView.findViewById(R.id.RelativeFilmDetails);
        relativeFilmAdd =(RelativeLayout)InputFragmentView.findViewById(R.id.RelativeAddFilm);

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
    }



    public void setLike(String userRid,String groupRid,String id_film,boolean thumbs)throws IOException {
        Log.d("user",userRid);
        Log.d("groupRid",groupRid);
        Log.d("id_film",id_film);
        Log.d("bool_like",String.valueOf(thumbs));
        RequestBody body = new FormBody.Builder()
                .add("get", "likes")
                .add("userRid", userRid)
                .add("groupRid", groupRid)
                .add("id_film", id_film)
                .add("bool_like", String.valueOf(thumbs))
                .build();

        Util.post(body, client, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                //System.out.println(response.body().toString());
                String json = response.body().string();
                try {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(relativeFilmAdd, "Like!", Snackbar.LENGTH_LONG)
                                    .setAction("Close", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Perform anything for the action selected
                                        }
                                    }).setDuration(Snackbar.LENGTH_LONG).show();
                        }
                    });

                } catch (Exception e){
                    e.printStackTrace();
                }

            }

        });

    }

    public void tolgoLike(String userRid,String groupRid,String id_film,boolean thumbs)throws IOException {
        Log.d("user",userRid);
        Log.d("groupRid",groupRid);
        Log.d("id_film",id_film);
        Log.d("bool_like",String.valueOf(thumbs));
        RequestBody body = new FormBody.Builder()
                .add("get", "tolgolikes")
                .add("userRid", userRid)
                .add("groupRid", groupRid)
                .add("id_film", id_film)
                .add("bool_like", String.valueOf(thumbs))
                .build();

        Util.post(body, client, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                //System.out.println(response.body().toString());
                String json = response.body().string();
                try {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(relativeFilmAdd, "Like!", Snackbar.LENGTH_LONG)
                                    .setAction("Close", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Perform anything for the action selected
                                        }
                                    }).setDuration(Snackbar.LENGTH_LONG).show();
                        }
                    });

                } catch (Exception e){
                    e.printStackTrace();
                }

            }

        });

    }
}
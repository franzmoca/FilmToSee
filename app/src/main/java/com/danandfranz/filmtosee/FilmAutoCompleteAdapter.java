package com.danandfranz.filmtosee;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tsuru on 28/02/16.
 */
public class FilmAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private List<Film> resultList = new ArrayList<Film>();
    OkHttpClient client;


    public FilmAutoCompleteAdapter(Context context) {
        mContext = context;
        client =new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Film getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_dropdown_item_2line, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position).getTitle());
        ((TextView) convertView.findViewById(R.id.text2)).setText("("+getItem(position).getYear()+")");
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Film> books = null;
                    try {
                        books = findFilms(mContext, constraint.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = books;
                    if (books != null) {
                        filterResults.count = books.size();
                    }else {
                        filterResults.count = 0;                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Film>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    /**
     * Returns a search result for the given film title.
     */
    private List<Film> findFilms(Context context, String bookTitle) throws IOException, JSONException {
        List<Film> result = new ArrayList<Film>();
        JSONArray search= new JSONArray(getFilms(bookTitle));
        for(int i = 0 ; i<search.length();i++){
            result.add(new Film(search.getJSONObject(i),true));
        }
        return result;

    }



    String getFilms(String query) throws IOException {
        String api = "http://normandy.dmi.unipg.it/blockchainvis/Film/orient.php";

        RequestBody body = new FormBody.Builder()
                .add("get", "searchFilmByName")
                .add("search", query)
                .build();
        Request request = new Request.Builder()
                .url(api)
                .post(body)

                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        //Log.d("ADAPTER",json);
        return json;
    }
}

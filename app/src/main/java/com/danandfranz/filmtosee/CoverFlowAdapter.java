package com.danandfranz.filmtosee;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;



import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;

public class CoverFlowAdapter extends BaseAdapter {

    private ArrayList<Film> data;
    private AppCompatActivity activity;
    public ImageLoader imageLoader;
    DisplayImageOptions options;
    ProgressDialog sp;


    public CoverFlowAdapter(AppCompatActivity context, ArrayList<Film> objects) {
        this.activity = context;
        this.data = objects;
        //Creo Cache
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "FilmToSeeCache");
        // Get singletone instance of ImageLoader
        imageLoader = ImageLoader.getInstance();
        // Create configuration for ImageLoader (all options are optional)
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                // You can pass your own memory cache implementation
                //.diskCacheExtraOptions(1024, 1024, null)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        imageLoader.init(config);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        sp = new ProgressDialog(context);
        sp.setIndeterminate(true);
        sp.setMessage("Loading...");



    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Film getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_flow_view, null, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //viewHolder.filmImage.setImageResource(data.get(position).getImageSource());
        display(viewHolder.filmImage, data.get(position).getImageLink());
        viewHolder.filmName.setText(data.get(position).getTitle());

        convertView.setOnClickListener(onClickListener(position));

        return convertView;
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.dialog_game_info);
                dialog.setCancelable(true); // dimiss when touching outside
                //dialog.setTitle("Film Details");

                TextView text = (TextView) dialog.findViewById(R.id.name);
                text.setText(getItem(position).getTitle());
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                display(image, data.get(position).getImageLink() );
                //image.setImageResource(getItem(position).getImageSource());
                Button delete = (Button) dialog.findViewById(R.id.removeFilm);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                        new MaterialDialog.Builder(activity)
                                .title("Confim?")
                                .content("Are you sure you want to remove " + getItem(position).getTitle() + " from this group?")
                                .theme(Theme.LIGHT)
                                .positiveText("Yes")
                                .negativeText("Cancel")
                                .icon(ContextCompat.getDrawable(activity, R.drawable.ic_delete_24dp))
                                .show();

                    }
                });

                dialog.show();
            }
        };
    }
    public void display(ImageView img, String url)
    {
        imageLoader.displayImage(url, img, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                sp.show();
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                sp.hide();

            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                sp.hide();
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }

        });
    }


    private static class ViewHolder {
        private TextView filmName;
        private ImageView filmImage;

        public ViewHolder(View v) {
            filmImage = (ImageView) v.findViewById(R.id.image);
            filmName = (TextView) v.findViewById(R.id.name);
        }
    }
}
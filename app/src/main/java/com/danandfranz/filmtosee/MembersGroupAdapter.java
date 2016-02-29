package com.danandfranz.filmtosee;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agilulfo on 29/02/2016.
 */
public class MembersGroupAdapter extends BaseAdapter {
    private final List<String> membersGroup;
    private MembersGroup context;
    ColorGenerator generator = ColorGenerator.MATERIAL;
    // declare the builder object once.
    TextDrawable.IBuilder builder;



    public MembersGroupAdapter(MembersGroup context, List<String> MembersGroup) {
        this.context = context;
        this.membersGroup = MembersGroup;
        builder = TextDrawable.builder()
                .beginConfig()
                .width(120)  // width in px
                .height(120)
                .withBorder(1)
                .endConfig()
                .round();
    }


    @Override
    public int getCount() {
        if (membersGroup != null) {
            return membersGroup.size();
        } else {
            return 0;
        }
    }

    @Override
    public String getItem(int position) {
        if (membersGroup != null) {
            return membersGroup.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup vg) {
        if (v==null)
        {
            v=LayoutInflater.from(context).inflate(R.layout.list_item_members_group, null);
        }

        String name= getItem(position);
        TextView txt=(TextView) v.findViewById(R.id.memberName);
        txt.setText(name);
        ImageView avatar = (ImageView) v.findViewById(R.id.userAvatar);
        final Resources res = context.getResources();
        final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);
        int color2 = generator.getColor(name);
        TextDrawable ic2 = builder.build(String.valueOf(name.charAt(0)) , color2);


        avatar.setImageDrawable(ic2);
        return v;

    }

}

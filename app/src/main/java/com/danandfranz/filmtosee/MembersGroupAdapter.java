package com.danandfranz.filmtosee;

import android.content.Context;
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

import java.util.List;

/**
 * Created by Agilulfo on 29/02/2016.
 */
public class MembersGroupAdapter extends BaseAdapter {
    private final List<MembersGroup> membersGroup;
    private MembersGroup context;

    public MembersGroupAdapter(MembersGroup context, List<MembersGroup> MembersGroup) {
        this.context = context;
        this.membersGroup = MembersGroup;
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
    public MembersGroup getItem(int position) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
     return null;
    }

    public void add(MembersGroup member) {
        membersGroup.add(member);
    }

    public void add(List<MembersGroup> member) {
        membersGroup.addAll(member);
    }



    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder(v);
        holder.memberName = (TextView) v.findViewById(R.id.memberName);
       // holder.listMembers = (LinearLayout) v.findViewById(R.id.listMembers);

        holder.userAvatar=(ImageView) v.findViewById(R.id.userAvatar);

        return holder;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView memberName;
        public LinearLayout listMembers;
        public ImageView userAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}

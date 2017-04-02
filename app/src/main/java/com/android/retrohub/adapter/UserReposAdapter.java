package com.android.retrohub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.retrohub.R;
import com.android.retrohub.models.UserRepos;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by batsa on 16.02.2017.
 */

public class UserReposAdapter extends ArrayAdapter<UserRepos> {

    List<UserRepos> userList;
    Context context;
    private LayoutInflater layoutInflater;

    public UserReposAdapter(Context context, List<UserRepos> objects) {
        super(context,0, objects);
        this.context = context;
        this.layoutInflater = layoutInflater.from(context);
        userList = objects;
    }

    public UserRepos getItem(int position){
        return userList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = layoutInflater.inflate(R.layout.layout_row_view, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        UserRepos item = getItem(position);

        vh.textViewName.setText(item.getName());
        vh.textViewEmail.setText(item.getFullName());
        vh.textViewLanguage.setText(item.getLanguage());
//        Picasso.with(context).load(item.getOwner().getAvatarUrl()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.imageView);
//        Picasso.with(context).load("xxx").placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder{
        public final RelativeLayout rootView;
        public final ImageView imageView;
        public final TextView textViewName;
        public final TextView textViewEmail;
        public final TextView textViewLanguage;

        public ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewName, TextView textViewEmail, TextView textViewLanguage) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewName = textViewName;
            this.textViewEmail = textViewEmail;
            this.textViewLanguage = textViewLanguage;
        }

        public static ViewHolder create(RelativeLayout rootView){
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewName = (TextView) rootView.findViewById(R.id.textViewName);
            TextView textViewEmail = (TextView) rootView.findViewById(R.id.textViewEmail);
            TextView textViewLanguage = (TextView) rootView.findViewById(R.id.textViewLanguage);
            return new ViewHolder(rootView,imageView,textViewName,textViewEmail,textViewLanguage);
        }
    }


}

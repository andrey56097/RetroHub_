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
import com.android.retrohub.model.SearchRepos;

import java.util.List;

/**
 * Created by batsa on 29.03.2017.
 */

public class SearchReposAdapter extends ArrayAdapter<SearchRepos> {
    List<SearchRepos> reposes;
    Context context;
    private LayoutInflater layoutInflater;

    public SearchReposAdapter(Context context, List<SearchRepos> objects) {
        super(context,0, objects);
        this.context = context;
        this.layoutInflater = layoutInflater.from(context);
        reposes = objects;
    }

    public SearchRepos getItem(int position){
        return reposes.get(position);
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

        SearchRepos item = getItem(position);

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

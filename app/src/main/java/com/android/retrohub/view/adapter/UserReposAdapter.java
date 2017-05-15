package com.android.retrohub.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.retrohub.R;
import com.android.retrohub.model.data.UserRepos;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by batsa on 16.02.2017.
 */

public class UserReposAdapter extends ArrayAdapter<UserRepos> {

    private List<UserRepos> userList;
    Context context;
    private LayoutInflater layoutInflater;

    public UserReposAdapter(Context context, List<UserRepos> objects) {
        super(context, 0, objects);
        this.context = context;
        this.layoutInflater = layoutInflater.from(context);
        userList = objects;
    }

    public UserRepos getItem(int position) {
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

        return vh.rootView;
    }

     static class ViewHolder {
        @BindView(R.id.imageView) ImageView imageView;
        @BindView(R.id.textViewName) TextView textViewName;
        @BindView(R.id.textViewEmail) TextView textViewEmail;
        @BindView(R.id.textViewLanguage) TextView textViewLanguage;
        private final RelativeLayout rootView;

         private ViewHolder(RelativeLayout rootView) {
            ButterKnife.bind(this, rootView);
            this.rootView = rootView;
        }

         private static ViewHolder create(RelativeLayout rootView) {
            return new ViewHolder(rootView);
        }
    }
}

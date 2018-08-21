package com.example.roskata.newsappvol2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    private static class ViewHolder {
        TextView section, title, authorName, publishedDate;
        ImageView imageView;
    }

    ArticleAdapter(@NonNull Context context, List<Article> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        // Get the data item for this position
        Article currArticle = getItem(position);

        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.news_item, parent, false);

            holder.section = convertView.findViewById(R.id.txtSection);
            holder.title = convertView.findViewById(R.id.txtTitle);
            holder.authorName = convertView.findViewById(R.id.txtAuthor);
            holder.publishedDate = convertView.findViewById(R.id.txtDate);
            holder.imageView = convertView.findViewById(R.id.imageView);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder != null) {

            String articleSection;
            if (currArticle != null) {
                articleSection = currArticle.getSection();
                holder.section.setText(articleSection);
            } else {
                holder.section.setVisibility(View.GONE);
            }

            if (currArticle != null) {
                holder.title.setText(currArticle.getTitle());
            }

            if (currArticle != null) {
                holder.authorName.setText(currArticle.getAuthor());
            }

            if (currArticle != null) {
                holder.publishedDate.setText(currArticle.getFormattedDate());
            }

            String imgUrl;
            if (currArticle != null) {
                imgUrl = currArticle.getImageUrl();
                Picasso.get().load(imgUrl).into(holder.imageView);
            } else {
                holder.imageView.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}

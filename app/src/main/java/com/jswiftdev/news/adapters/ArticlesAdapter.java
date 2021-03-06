package com.jswiftdev.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jswiftdev.news.ArticlesPage;
import com.jswiftdev.news.R;
import com.jswiftdev.news.models.Article;
import com.jswiftdev.news.utils.Constants;
import com.jswiftdev.news.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> {
    /**
     * carries context information for the calling party
     */
    private Context context;

    /**
     * provides a list of articles to be displayed
     */
    private List<Article> articleList;

    public ArticlesAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public ArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_article, parent, false);
        return new ArticlesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ArticlesViewHolder holder, int position) {
        final Article currentArticle = articleList.get(position);
        holder.tvTitle.setText(currentArticle.getTitle());
        holder.tvAuthor.setText(currentArticle.getAuthor());
        holder.tvDesciption.setText(currentArticle.getDescription());
        try {
            holder.tvTime.setText(Utils.getRelativeTime(currentArticle.getPublishedAt()));
        } catch (Exception e) {
            Log.d(Constants.LOG_TAG, "Could not parse time " + e.getMessage());
        }

        Glide.with(context)
                .load(currentArticle.getUrlToImage())
                .into(holder.imArticleImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isConnected(context)) {
                    Toast.makeText(context, R.string.internet_connection_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent showArticleContent = new Intent(context, ArticlesPage.class);
                showArticleContent.putExtra(Constants.KEY_URL, currentArticle.getUrl());
                context.startActivity(showArticleContent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ArticlesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_author)
        TextView tvAuthor;

        @BindView(R.id.im_article_image)
        ImageView imArticleImage;

        @BindView(R.id.tv_description)
        TextView tvDesciption;

        @BindView(R.id.tv_relative_time)
        TextView tvTime;

        ArticlesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

package com.ext.adarsh.Adapter;

/**
 * Created by ExT-Emp-001 on 30-10-2017.
 */


import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ext.adarsh.Activity.KnowledgeArticleDetailActivity;
import com.ext.adarsh.Bean.BeanArticalSubComment;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Utility;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ExT-Emp-001 on 11-08-2017.
 */

public class AdapterKnowledgeSubComent extends RecyclerView.Adapter<AdapterKnowledgeSubComent.MyViewHolder> {

    private List<BeanArticalSubComment> knowledgeList;
    Activity activity;
    Dialog dd;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_coment_person, tv_comment, tv_comment_like, tv_comment_like_red, tv_no_of_like;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            tv_coment_person = (TextView) view.findViewById(R.id.tv_coment_person);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_comment_like = (TextView) view.findViewById(R.id.tv_comment_like);
            tv_comment_like_red = (TextView) view.findViewById(R.id.tv_comment_like_red);
            tv_no_of_like = (TextView) view.findViewById(R.id.tv_no_of_like);
            tv_coment_person.setTypeface(Utility.getTypeFace());
            tv_comment.setTypeface(Utility.getTypeFace());
            tv_comment.setTypeface(Utility.getTypeFace());
            tv_comment_like.setTypeface(Utility.getTypeFace());
            tv_comment_like_red.setTypeface(Utility.getTypeFace());

            tv_no_of_like.setTypeface(Utility.getTypeFace());

        }
    }


    public AdapterKnowledgeSubComent(List<BeanArticalSubComment> knowledgeList, Activity activity) {
        this.knowledgeList = knowledgeList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_knowledge_sub_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BeanArticalSubComment knowledge = knowledgeList.get(position);

        holder.tv_coment_person.setText(knowledge.fullName);
        holder.tv_comment.setText(knowledge.comments);
        holder.tv_no_of_like.setText(knowledge.likes);

        if (knowledge.likeStatus.equals("A")) {
            holder.tv_comment_like_red.setVisibility(View.VISIBLE);
            holder.tv_comment_like.setVisibility(View.GONE);
        } else {
            holder.tv_comment_like_red.setVisibility(View.GONE);
            holder.tv_comment_like.setVisibility(View.VISIBLE);
        }

        holder.tv_comment_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.ArticleCommentId = knowledge.articleCommentId;
                // KnowledgeMain.commentReply();
                holder.tv_comment_like_red.setVisibility(View.VISIBLE);
                holder.tv_comment_like.setVisibility(View.GONE);
                KnowledgeArticleDetailActivity.knowledge_coment_like();
                int a = Integer.parseInt(holder.tv_no_of_like.getText().toString());
                holder.tv_no_of_like.setText(String.valueOf(a + 1));
            }
        });

        holder.tv_comment_like_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.ArticleCommentId = knowledge.articleCommentId;

                holder.tv_comment_like_red.setVisibility(View.GONE);
                holder.tv_comment_like.setVisibility(View.VISIBLE);
                KnowledgeArticleDetailActivity.knowledge_coment_like();
                int a = Integer.parseInt(holder.tv_no_of_like.getText().toString());
                holder.tv_no_of_like.setText(String.valueOf(a - 1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return knowledgeList.size();
    }


}




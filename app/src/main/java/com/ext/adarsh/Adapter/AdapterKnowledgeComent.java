package com.ext.adarsh.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ext.adarsh.Bean.BeanKnowledgeComment;
import com.ext.adarsh.R;
import com.ext.adarsh.Utils.AppConstant;
import com.ext.adarsh.Utils.Utility;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ExT-Emp-001 on 11-08-2017.
 */

public class AdapterKnowledgeComent extends RecyclerView.Adapter<AdapterKnowledgeComent.MyViewHolder> {

    private List<BeanKnowledgeComment> knowledgeList;
    Activity activity;
    Dialog dd;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_coment_person, tv_comment, tv_comment_like, tv_comment_reply, tv_no_of_like;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            tv_coment_person = (TextView) view.findViewById(R.id.tv_coment_person);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_comment_like = (TextView) view.findViewById(R.id.tv_comment_like);
            tv_comment_reply = (TextView) view.findViewById(R.id.tv_comment_reply);
            tv_no_of_like = (TextView) view.findViewById(R.id.tv_no_of_like);

            tv_coment_person.setTypeface(Utility.getTypeFace());
            tv_comment.setTypeface(Utility.getTypeFace());
            tv_comment_like.setTypeface(Utility.getTypeFace());
            tv_comment_reply.setTypeface(Utility.getTypeFace());
            tv_no_of_like.setTypeface(Utility.getTypeFace());
        }
    }


    public AdapterKnowledgeComent(List<BeanKnowledgeComment> knowledgeList, Activity activity) {
        this.knowledgeList = knowledgeList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_knowledge_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final BeanKnowledgeComment knowledge = knowledgeList.get(position);

        holder.tv_coment_person.setText(knowledge.FullName);
        holder.tv_comment.setText(knowledge.Comments);
        holder.tv_no_of_like.setText(knowledge.Likes);
        holder.tv_comment_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.TopicCommentId = knowledge.TopicCommentId;
               // KnowledgeMain.commentReply();
              //  KnowledgeMain.knowledge_coment_like();
            }
        });
        holder.tv_comment_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.TopicCommentId = knowledge.TopicCommentId;
                AppConstant.likes=knowledge.Likes;
              //  KnowledgeMain.commentReply(position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return knowledgeList.size();
    }


}



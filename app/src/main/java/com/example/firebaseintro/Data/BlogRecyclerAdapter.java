package com.example.firebaseintro.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseintro.Model.Blog;
import com.example.firebaseintro.R;

import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerAdapter.ViewHolder holder, int position) {

            Blog blog = blogList.get(position);
            String imageUrl=null;

            holder.title.setText(blog.getTitle());
            holder.description.setText(blog.getDescription());
            holder.timestamp.setText(blog.getTimestamp());

            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());

            holder.timestamp.setText(formattedDate);

            imageUrl = blog.getImage();
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView description;
        public TextView timestamp;
        public ImageView image;
        public String userID;

        public ViewHolder(View view, Context ctx){
            super(view);

            context = ctx;
            title = (TextView) view.findViewById(R.id.postTitle);
            description = (TextView) view.findViewById(R.id.postDescription);
            image= (ImageView) view.findViewById(R.id.postImageList);
            timestamp = (TextView) view.findViewById(R.id.timestampList);

            userID= null;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }


}


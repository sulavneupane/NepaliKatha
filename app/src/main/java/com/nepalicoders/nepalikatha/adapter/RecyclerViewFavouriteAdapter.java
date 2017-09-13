package com.nepalicoders.nepalikatha.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nepalicoders.nepalikatha.R;
import com.nepalicoders.nepalikatha.activities.PoemDetailActivity;
import com.nepalicoders.nepalikatha.database.DatabaseHandler;
import com.nepalicoders.nepalikatha.database.DatabaseHelper;
import com.nepalicoders.nepalikatha.objects.Poem;
import com.nepalicoders.nepalikatha.utils.Messages;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class RecyclerViewFavouriteAdapter extends RecyclerView.Adapter<RecyclerViewFavouriteAdapter.PoemViewHolder> {
    DatabaseHandler handler;
    private Context context;
    private List<Poem> poems;

    public RecyclerViewFavouriteAdapter(Context context, List<Poem> poems) {
        this.context = context;
        this.poems = poems;
    }

    @Override
    public RecyclerViewFavouriteAdapter.PoemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_story_style, parent, false);
        RecyclerViewFavouriteAdapter.PoemViewHolder holder = new PoemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewFavouriteAdapter.PoemViewHolder holder, final int position) {
        Poem poem = poems.get(position);

        //change date format
        SimpleDateFormat dbDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat displayDate = new SimpleDateFormat("MMM dd, yyyy");
        String date = null;
        try {
            Date convertedCurrentDate = dbDate.parse(poem.getPublishedOn());
            date = displayDate.format(convertedCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.title.setText(Html.fromHtml(poem.getTitle()));
        holder.written_by.setText("- " + Html.fromHtml(poem.getWriter()));
        holder.submitted_by.setText("Submitted By: " + Html.fromHtml(poem.getSubmittedBy()) + " on " + Html.fromHtml(date));
        //holder.published_on.setText(Html.fromHtml(date));

        if (checkPoemInFavList(poem.getId())) {
            holder.list_favourite.setImageResource(R.drawable.ic_favourite);
            holder.list_favourite.setColorFilter(context.getResources().getColor(R.color.colorAccent));
        }

        holder.list_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Poem poem = poems.get(position);
                poem.setId(poem.getId());
                poem.setTitle(poem.getTitle());
                poem.setWriter(poem.getWriter());
                poem.setContent(poem.getContent());
                poem.setCategory(poem.getCategory());
                poem.setPublishedOn(poem.getPublishedOn());
                poem.setSubmittedBy(poem.getSubmittedBy());
                poem.setEmail(poem.getEmail());

                if (checkPoemInFavList(poem.getId())) {
                    int result = handler.removePoemFromFavourite(poem.getId());
                    if (result > 0) {
                        holder.list_favourite.setImageResource(R.drawable.ic_favourites);
                        holder.list_favourite.setColorFilter(context.getResources().getColor(R.color.colorAccent));

                        removeAt(holder.getPosition());
                        Messages.snackBarShort(v, context.getString(R.string.action_favourite_removed));
                    }
                } else {
                    long poemid = handler.addPoemToFavourite(poem);
                    if (poemid > 0) {
                        holder.list_favourite.setImageResource(R.drawable.ic_favourite);
                        holder.list_favourite.setColorFilter(context.getResources().getColor(R.color.colorAccent));

                        Messages.snackBarShort(v, context.getString(R.string.action_favourite));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return poems.size();
    }

    class PoemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, written_by, submitted_by, published_on;
        ImageView list_favourite;

        public PoemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            list_favourite = (ImageView) itemView.findViewById(R.id.list_favourite);
            title = (TextView) itemView.findViewById(R.id.poem_title);
            written_by = (TextView) itemView.findViewById(R.id.written_by);
            submitted_by = (TextView) itemView.findViewById(R.id.submitted_by);
            //published_on = (TextView) itemView.findViewById(R.id.published_on);
        }

        @Override
        public void onClick(View v) {
            Poem poem = poems.get(getPosition());
            Intent intent = new Intent(context, PoemDetailActivity.class);
            intent.putExtra("Poem", poem);
            context.startActivity(intent);
        }
    }

    private boolean checkPoemInFavList(Long id) {
        try {
            handler = new DatabaseHandler(context).Open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor cursor = handler.getAllPoemsFromFavourite();
        //if(cursor!=null && cursor.getCount()>0){
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            if (id == cursor.getLong(cursor.getColumnIndex(DatabaseHelper.KEY_FAVOURITES_ITEM_ID))) {
                return true;
            }
        }
        return false;
    }

    public void removeAt(int position) {
        poems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, poems.size());
    }

}

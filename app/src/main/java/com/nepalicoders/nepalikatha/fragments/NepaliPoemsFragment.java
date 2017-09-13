package com.nepalicoders.nepalikatha.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nepalicoders.nepalikatha.R;
import com.nepalicoders.nepalikatha.adapter.RecyclerViewPoemsAdapter;
import com.nepalicoders.nepalikatha.database.DatabaseHandler;
import com.nepalicoders.nepalikatha.database.DatabaseHelper;
import com.nepalicoders.nepalikatha.objects.Poem;
import com.nepalicoders.nepalikatha.utils.Messages;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NepaliPoemsFragment extends Fragment {

    DatabaseHandler handler;
    RecyclerView recyclerView;
    RecyclerViewPoemsAdapter adapter;
    View view;

    public NepaliPoemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nepali_stories, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.nepali_poems_list);

        try {
            handler = new DatabaseHandler(getActivity()).Open();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Cursor cursor = handler.getAllNepaliPoemsFromDatabase();
        if (cursor != null && cursor.getCount() > 0) {

            //Log.d("NepaliKatha", "Poems Found!");

            List<Poem> poems = new ArrayList<>();

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                Poem poem = new Poem();

                poem.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_ID)));
                poem.setTitle(String.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_TITLE))));
                poem.setWriter(String.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_WRITER))));
                poem.setContent(String.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_CONTENT))));
                poem.setCategory(String.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_CATEGORY))));
                poem.setPublishedOn(String.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_PUBLISHED_ON))));
                poem.setSubmittedBy(String.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_SUBMITTED_BY))));
                poem.setEmail(String.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ITEM_EMAIL))));

                poems.add(poem);
            }

            adapter = new RecyclerViewPoemsAdapter(getActivity(), poems);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        } else {

            Messages.snackBarShort(view, getString(R.string.no_nepali_poems));

        }
    }
}

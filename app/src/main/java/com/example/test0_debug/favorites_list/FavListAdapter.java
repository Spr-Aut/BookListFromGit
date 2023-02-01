package com.example.test0_debug.favorites_list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test0_debug.activity.QueryActivity;
import com.example.test0_debug.R;
import com.example.test0_debug.tools.db.DBHelper;
import com.example.test0_debug.tools.db.DBStatistics;
import com.example.test0_debug.word_list.WordListData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.ViewHolder> {
    private static final String TAG = "myTAG";
    private final List<WordListData> listDatas;
    private final Context context;


    public FavListAdapter(Context context, List<WordListData> listDatas) {
        this.listDatas = listDatas;
        this.context = context;


    }

    @NotNull
    @Override
    public FavListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        final FavListAdapter.ViewHolder holder = new FavListAdapter.ViewHolder(view);
        holder.recordView.setOnClickListener(v -> {
        });
        holder.content.setOnClickListener(v -> {
            if (DBStatistics.getFavoritesWordNum(context)==0)
                return;
            int position = holder.getAdapterPosition();
            WordListData wordData=listDatas.get(position);
            Intent intent=new Intent(context, QueryActivity.class);
            intent.putExtra("wordData",wordData);
            context.startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(FavListAdapter.ViewHolder holder, int position) {
        WordListData wordListDatas = listDatas.get(position);
        holder.content.setText(wordListDatas.getName());
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        View recordView;

        public ViewHolder(View itemView) {
            super(itemView);
            recordView = itemView;
            content = itemView.findViewById(R.id.record_content);
        }
    }



}

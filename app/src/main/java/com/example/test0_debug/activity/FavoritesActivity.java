package com.example.test0_debug.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test0_debug.R;
import com.example.test0_debug.favorites_list.FavListAdapter;
import com.example.test0_debug.tools.db.DBHelper;
import com.example.test0_debug.tools.ui.CostumActionBar;
import com.example.test0_debug.tools.ui.StatusBarUtil;
import com.example.test0_debug.word_list.WordListData;

import java.util.ArrayList;
import java.util.List;


public class FavoritesActivity extends AppCompatActivity {
    private Context context;
    private List<WordListData> favDataList;
    private Message message;
    private FavListAdapter adapter;
    private final int FAV_DATASET_DONE = 0x11;

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == FAV_DATASET_DONE) {

                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DBHelper.getThemeNight(this, 1) == DBHelper.THEME_NIGHT) {
            StatusBarUtil.setStatusBarMode(this, false, R.color.status_bar_color);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            StatusBarUtil.setStatusBarMode(this, true, R.color.status_bar_color);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_favorites_layout);
        context = FavoritesActivity.this;

        favDataList = new ArrayList<>();
        CostumActionBar actionBar = findViewById(R.id.action_bar);
        actionBar.setStyleNoBack("我的收藏");
        RecyclerView recycler_booklist = findViewById(R.id.fav_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycler_booklist.setLayoutManager(layoutManager);
        adapter = new FavListAdapter(context, favDataList);
        recycler_booklist.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        favDataList.clear();
        initData();
    }

    private void initData() {
        message = Message.obtain();
        new Thread(() -> {
            DBHelper dbHelper = new DBHelper(context, "Users.db", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "select * from wordlist where favorites=1;";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    WordListData wordListData = new WordListData();
                    wordListData.setWordId(cursor.getInt(cursor.getColumnIndex("word_id")));
                    wordListData.setDesc(cursor.getString(cursor.getColumnIndex("desc_")));
                    wordListData.setName(cursor.getString(cursor.getColumnIndex("name")));
                    wordListData.setSymbol(cursor.getString(cursor.getColumnIndex("symbol")));
                    wordListData.setMsg(cursor.getString(cursor.getColumnIndex("msg")));
                    wordListData.setClass_id(cursor.getString(cursor.getColumnIndex("class_id")));
                    wordListData.setSound(cursor.getString(cursor.getColumnIndex("sound")));
                    wordListData.setMemory(cursor.getInt(cursor.getColumnIndex("memory")));
                    wordListData.setExampleSentence(cursor.getString(cursor.getColumnIndex("example_sentence")));
                    wordListData.setFavorites(cursor.getInt(cursor.getColumnIndex("favorites")));
                    favDataList.add(wordListData);
                } while (cursor.moveToNext());
            } else {
                WordListData wordListData = new WordListData();
                wordListData.setName("暂无收藏");
                favDataList.add(wordListData);
            }
            cursor.close();
            db.close();

            message.what = FAV_DATASET_DONE;
            handler.sendMessage(message);
        }).start();
    }



}
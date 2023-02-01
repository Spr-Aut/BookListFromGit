package com.example.test0_debug.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test0_debug.R;
import com.example.test0_debug.tools.QueryThread;
import com.example.test0_debug.tools.db.DBHelper;
import com.example.test0_debug.tools.httpHelper.ApiAddress;
import com.example.test0_debug.tools.httpHelper.jsonParse.YouDaoDict;
import com.example.test0_debug.tools.httpHelper.xmlParse.WordSenDataSet;
import com.example.test0_debug.tools.ui.CostumActionBar;
import com.example.test0_debug.tools.ui.StatusBarUtil;
import com.example.test0_debug.word_list.WordListData;

import java.util.List;

public class QueryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button favorites;
    private TextView show_name;
    private TextView show_symbol;
    private TextView show_desc;
    private TextView task_view;
    private TextView show_exa_sentence;
    private CostumActionBar actionBar;
    private MediaPlayer mediaPlayer;
    private Button knowing;
    private Button vagueness;
    private Button memorizing;
    private Button forgetting;
    private Context context;
    private WordListData wordData;
    private YouDaoDict dictData;
    private LinearLayout fav_and_snd;
    private QueryThread queryThread;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*爱词霸字典查询结果*/
            super.handleMessage(msg);
            if (msg.what == QueryThread.SUCCESS) {
                String data = (String) msg.obj;
                data = WordSenDataSet.parseXmlWithPull(data, true);
                show_exa_sentence.setText(data);
            }
            if (msg.what == QueryThread.FAIL) {
                show_exa_sentence.setText("例句初始化失败");
                show_exa_sentence.setGravity(Gravity.CENTER);
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
        setContentView(R.layout.activity_query);
        context = QueryActivity.this;

        initView();
        initData();
    }

    private void initView() {
        show_name = findViewById(R.id.show_name);
        show_symbol = findViewById(R.id.show_symbol);
        show_desc = findViewById(R.id.show_desc);
        task_view = findViewById(R.id.task_view);
        show_exa_sentence = findViewById(R.id.example_sentence);
        show_exa_sentence.setMovementMethod(ScrollingMovementMethod.getInstance());
        show_desc.setMovementMethod(ScrollingMovementMethod.getInstance());
        memorizing = findViewById(R.id.memorizing);
        knowing = findViewById(R.id.knowing);
        vagueness = findViewById(R.id.vagueness);
        forgetting = findViewById(R.id.forgetting);
        Button sound = findViewById(R.id.sound);
        favorites = findViewById(R.id.favorites);
        actionBar = findViewById(R.id.action_bar);
        fav_and_snd = findViewById(R.id.fav_and_snd);
        favorites.setOnClickListener(this);
        memorizing.setOnClickListener(this);
        knowing.setOnClickListener(this);
        vagueness.setOnClickListener(this);
        sound.setOnClickListener(this);
        forgetting.setOnClickListener(this);
        show_desc.setOnClickListener(this);
        show_exa_sentence.setOnClickListener(this);
    }

    private void initData() {

        wordData = (WordListData) getIntent().getSerializableExtra("wordData");
        dictData = (YouDaoDict) getIntent().getSerializableExtra("dictData");
        if (wordData != null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(wordData.getSound()));
            actionBar.setStyleNoBack("\"" + wordData.getName() + "\"详情");
            show_name.setText(wordData.getName());
            show_symbol.setText(wordData.getSymbol());
            show_desc.setText(wordData.getDesc());
            if (wordData.getExampleSentence() == null
                    || wordData.getExampleSentence().equals("null")) {
                String url = ApiAddress.getIcibaAddress(wordData.getName());
                queryThread = new QueryThread(url, handler);
                queryThread.start();
                return;
            }
            show_exa_sentence.setText(wordData.getExampleSentence());
            return;
        }

        if (dictData != null) {
            fav_and_snd.setVisibility(View.GONE);
            actionBar.setStyleNoBack("\"" + dictData.getQuery() + "\"查询结果");
            if (dictData.getErrorCode() != 0) {
                show_name.setVisibility(View.GONE);
                show_symbol.setVisibility(View.GONE);
                show_desc.setVisibility(View.GONE);
                show_exa_sentence.setText("查询失败");
                show_exa_sentence.setGravity(Gravity.CENTER);
                return;
            }
            show_name.setText(dictData.getQuery());
            if (dictData.getBasic() != null) {
                if (dictData.getBasic().getExplains() != null)
                    show_desc.setText(getStringFromList(dictData.getBasic().getExplains()));
                if (dictData.getBasic().getPhonetic() != null)
                    show_symbol.setText(dictData.getBasic().getPhonetic());
            }
            show_exa_sentence.setText("");
            if (dictData.getWeb() == null)
                return;
            for (int i = 0; i < dictData.getWeb().size(); i++) {
                show_exa_sentence.append(dictData.getWeb().get(i).getKey());
                show_exa_sentence.append("：\n");
                show_exa_sentence.append(dictData.getWeb().get(i).getValue().toString()
                        .replace("[", "").replace("]", "").replace(",", "，"));
                show_exa_sentence.append("\n\n");
            }

        }

    }

    private String getStringFromList(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sound:
                mediaPlayer.start();
                break;
            case R.id.favorites:
                int wordId = wordData.getWordId();
                if (favorites.getText().toString().equals("取消收藏")) {
                    DBHelper.setFavorites(context, DBHelper.NO_FAVORITES, wordId);
                    favorites.setText("收藏");
                } else {
                    DBHelper.setFavorites(context, DBHelper.FAVORITES, wordId);
                    favorites.setText("取消收藏");
                }
        }
    }
}
package com.example.test0_debug.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test0_debug.R;
import com.example.test0_debug.tools.QueryThread;
import com.example.test0_debug.tools.db.DBHelper;
import com.example.test0_debug.tools.httpHelper.ApiAddress;
import com.example.test0_debug.tools.httpHelper.HttpUtil;
import com.example.test0_debug.tools.httpHelper.jsonParse.DaySenDataSet;
import com.example.test0_debug.tools.httpHelper.jsonParse.TransDataSet;
import com.example.test0_debug.tools.ui.SourceView;
import com.example.test0_debug.tools.ui.StatusBarUtil;
import com.example.test0_debug.tools.ui.TargetView;
import com.example.test0_debug.tools.ui.TransSpinnerView;
import com.google.gson.Gson;

public class TranslatorActivity extends AppCompatActivity {

    private Context context;
    private TargetView targetView;
    private SourceView sourceView;
    private TextView targetTextView;
    private ProgressDialog tipDialog;
    private TransSpinnerView spinnerView;
    private static int apiType;
    private static final int TRANS_DATA = 0;
    private static final int DAYSEN_DATA = 1;
    private String from;
    private String to;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            targetTextView.setText(null);
            dealMessage(msg);
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
        setContentView(R.layout.activity_translator);
        context = this;
        initView();
        if (HttpUtil.networkAvailable(context)) {
            initData();
        } else {
            targetTextView.append("网络不可用");
            targetTextView.setGravity(Gravity.CENTER);
            sourceView.setEnable(false);
        }
    }

    private void initView() {
        spinnerView = findViewById(R.id.trans_actionbar);
        spinnerView.setSpinnerOnClickListener((type, position, text) -> {
            if (type == TransSpinnerView.TYPE_SOURCE) {
                from = text;
            }
            if (type == TransSpinnerView.TYPE_TARGET) {
                to = text;
            }
        });
        sourceView = findViewById(R.id.sourceview);
        sourceView.setSearchViewListener(query -> {
            String transAddress = ApiAddress.getTransAddress(from, to, query);
            tipDialog.show();
            QueryThread queryThread = new QueryThread(transAddress, handler);
            queryThread.start();
            apiType = TRANS_DATA;
            hideInput();//隐藏软键盘
        });
        targetView = findViewById(R.id.targetview);
        targetView.setTargetViewListener(text -> {
            ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData myClip = ClipData.newPlainText("text", text);
            myClipboard.setPrimaryClip(myClip);
            Toast.makeText(context, " 已复制", Toast.LENGTH_SHORT).show();
        });
        targetTextView = targetView.getTargetTextView();
        tipDialog = new ProgressDialog(context);
        tipDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        tipDialog.setIcon(R.drawable.translate);
        tipDialog.setCancelable(false);
        tipDialog.setCanceledOnTouchOutside(false);
        tipDialog.setTitle("百度翻译");
        tipDialog.setMessage("正在查询. . .");
    }

    private void initData() {
        from = getResources().getStringArray(R.array.source)[0];
        to = getResources().getStringArray(R.array.target)[0];
        String daySenAddress = ApiAddress.getDaySenAddress();
        QueryThread queryThread = new QueryThread(daySenAddress, handler);
        queryThread.start();
        apiType = DAYSEN_DATA;
    }

    private void dealMessage(Message msg) {
        if (msg.what == QueryThread.SUCCESS) {
            String data = (String) msg.obj;
            switch (apiType) {
                case TRANS_DATA:
                    targetTextView.setGravity(Gravity.START);
                    TransDataSet transDataSet = new Gson().fromJson(data, TransDataSet.class);
                    if (transDataSet.getTrans_result() == null) {
                        tipDialog.dismiss();
                        targetTextView.append("查询失败");
                        targetTextView.setGravity(Gravity.CENTER);
                        break;
                    }
                    int length = transDataSet.getTrans_result().size();
                    for (int i = 0; i < length; i++)
                        targetTextView.append(transDataSet.getTrans_result().get(i).getDst());
                    tipDialog.dismiss();
                    targetView.setCopyBtnVisibility(View.VISIBLE);
                    break;
                case DAYSEN_DATA:
                    DaySenDataSet daySenDataSet = new Gson().fromJson(data, DaySenDataSet.class);
                    targetTextView.setGravity(Gravity.CENTER);
                    targetTextView.append(daySenDataSet.getContent());
                    targetTextView.append("\n\n");
                    targetTextView.append(daySenDataSet.getNote());
                    targetTextView.append("\n\n\n\n");
                    targetView.setCopyBtnVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
        if (msg.what == QueryThread.FAIL) {
            tipDialog.dismiss();
            targetTextView.append("每日一句获取失败");
            targetTextView.setGravity(Gravity.CENTER);
        }


    }
    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    private void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
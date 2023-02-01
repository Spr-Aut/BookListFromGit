package com.example.test0_debug.tools.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test0_debug.R;

public class SourceView extends LinearLayout implements View.OnClickListener {

    /**
     * 输入框
     */
    private EditText input;
    private Button query;

    /**
     * 删除键
     */
    private Button delete;

    /**
     * 上下文对象
     */
    private final Context mContext;


    /**
     * 搜索回调接口
     */
    private SearchViewListener mListener;

    /**
     * 设置搜索回调接口
     *
     */
    public void setEnable(boolean enable){
        query.setEnabled(enable);
        delete.setEnabled(enable);
    }
    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    public SourceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.sourceview_layout, this);
        initViews();
    }

    private void initViews() {
        input = findViewById(R.id.input);
        delete = findViewById(R.id.delete);
        delete.setVisibility(GONE);
        /**
         * 返回按钮
         */
        query = findViewById(R.id.query);
        delete.setOnClickListener(this);
        query.setOnClickListener(this);
        input.setOnClickListener(this);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())){
                    delete.setVisibility(INVISIBLE);
                }else{
                    delete.setVisibility(VISIBLE);
                }
            }
        });
    }

    /**
     * 通知监听者 进行搜索操作
     *
     * @param text
     */
    private void notifyStartSearching(String text) {
        if (mListener != null) {
            mListener.onSearch(input.getText().toString());
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input:
              
                break;
            case R.id.delete:
                input.setText("");
                delete.setVisibility(GONE);
                break;
            case R.id.query:
                String query = input.getText().toString();
                mListener.onSearch(query);
                break;
        }
    }

    /**
     * search view回调方法
     */
    public interface SearchViewListener {

        /**
         * 开始搜索
         *
         * @param text 传入输入框的文本
         */
        void onSearch(String text);

//        /**
//         * 提示列表项点击时回调方法 (提示/自动补全)
//         */
//        void onTipsItemClick(String text);
    }

}

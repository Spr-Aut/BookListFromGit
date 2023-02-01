package com.example.test0_debug.tools.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test0_debug.R;

public class TargetView extends LinearLayout implements View.OnClickListener {

    /**
     * 输入框
     */
    private TextView output;

    /**
     * 删除键
     */
    private Button copy;

    /**
     * 上下文对象
     */
    private final Context mContext;


    /**
     * 搜索回调接口
     */
    private TargetViewListener mListener;

    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setTargetViewListener(TargetViewListener listener) {
        mListener = listener;
    }

    public TargetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.targetview_layout, this);
        initViews();
    }

    private void initViews() {
        output = findViewById(R.id.output);
        output.setMovementMethod(ScrollingMovementMethod.getInstance());
        copy = findViewById(R.id.copy);
        output.setOnClickListener(this);
        copy.setOnClickListener(this);
        copy.setVisibility(GONE);
    }

    public void setCopyBtnVisibility(int visiblility) {
        this.copy.setVisibility(visiblility);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.output:

                break;

            case R.id.copy:
                String result = output.getText().toString();
                mListener.onCopy(result);
                break;
        }
    }
public TextView getTargetTextView(){
        return output;
}
    /**
     * search view回调方法
     */
    public interface TargetViewListener {

        /**
         * 开始搜索
         *
         * @param text 传入输入框的文本
         */
        void onCopy(String text);

    }

}

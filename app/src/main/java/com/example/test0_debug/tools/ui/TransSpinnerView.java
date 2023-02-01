package com.example.test0_debug.tools.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.test0_debug.R;

public class TransSpinnerView extends LinearLayout {

    private Button change;
    private View headView;
    private Spinner sourceSpinner;
    private Context context;
    private Spinner targetSpinner;
    private SpinnerOnClickListener spinnerOnClickListener;
    public static final int TYPE_SOURCE=0;
    public static final int TYPE_TARGET=1;

    @Override
    public void setEnabled(boolean enabled) {
        sourceSpinner.setEnabled(enabled);
        targetSpinner.setEnabled(enabled);
        change.setEnabled(enabled);
    }

    public TransSpinnerView(Context context) {
        super(context);
        init(context);
    }

    public TransSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        headView = mInflater.inflate(R.layout.trans_spinner_layout, this);
        this.context = context;
        initView();
    }

    private void initView() {
        ImageView headerBack = headView.findViewById(R.id.header_back);
        sourceSpinner = headView.findViewById(R.id.source);
        targetSpinner = headView.findViewById(R.id.target);
        change = headView.findViewById(R.id.change);
        headerBack.setOnClickListener(v -> ((Activity) getContext()).finish());
        String[] sourceItem = getResources().getStringArray(R.array.source);
        String[] targetItem = getResources().getStringArray(R.array.target);
        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, sourceItem);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> targetAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, targetItem);
        targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(sourceAdapter);
        targetSpinner.setAdapter(targetAdapter);
        sourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerOnClickListener.onSelect(TYPE_SOURCE,position, dealType(sourceAdapter.getItem(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        targetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerOnClickListener.onSelect(TYPE_TARGET,position, dealType(targetAdapter.getItem(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        change.setOnClickListener(v -> {

        });

    }

    public void setSpinnerOnClickListener(SpinnerOnClickListener spinnerOnClickListener) {
        this.spinnerOnClickListener = spinnerOnClickListener;

    }

    public interface SpinnerOnClickListener {
        void onSelect(int type,int position, String text);
    }

    private String dealType(String data) {
        switch (data) {
            case "中文":
                return "zh";
            case "英文":
                return "en";
            case "日文":
                return "jp";
            case "法文":
                return "fra";
            case "德文":
                return "de";
            case "韩文":
                return "kor";
            default:
                return "auto";
        }
    }

}


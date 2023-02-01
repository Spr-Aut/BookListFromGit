package com.example.test0_debug.book_list;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test0_debug.R;

import com.example.test0_debug.tools.httpHelper.HttpUtil;
import com.example.test0_debug.tools.httpHelper.jsonParse.WordList;
import com.example.test0_debug.tools.db.DBHelper;
import com.example.test0_debug.tools.httpHelper.ApiAddress;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    private static final String TAG = "wordUrl";
    private static final int GET_WORD_SUCCESS = 1;
    private static final int GET_WORD_FAIL = 0;
    private final List<BookListDatas> mHistoryList;
    private int position;//数目列表点击位置
    private final Context context;
    private final TextView bookTitle;
    private final DBHelper dbHelper;
    private final ProgressDialog progressDialog;
    private Message message;
    private boolean isUpdated = false;
    private boolean netAvailable = false;
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_WORD_SUCCESS) {
                Log.e(TAG, "start :" + msg.arg1 + "  total: " + msg.arg2);
                String sql = (String) msg.obj;
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                db.execSQL(sql);
                double done = msg.arg1;
                double total = msg.arg2;
                int progress = (int) ((done / total) * 100);
                Log.e(TAG, "done :" + msg.arg1 + "  total: " + msg.arg2 + " progress: " + progress);

                progressDialog.setProgress(progress);
                if (msg.arg1 == msg.arg2) {
                    db.close();
                    progressDialog.dismiss();
                    Toast.makeText(context, "选择成功！", Toast.LENGTH_SHORT).show();
                    //如果更换了书本则会重置今日进度，书本名，书本位置
                    if (!mHistoryList.get(position).getClass_id().equals(DBHelper.getBookClassId(context, 1))) {
                        DBHelper.setBookClassId(context, 1, mHistoryList.get(position).getClass_id());
                        DBHelper.setTodayProgress(context, 1, 1);
                        DBHelper.setWordPosition(context, 0, 1);
                        DBHelper.setExistence_item(context, mHistoryList.get(position).getClass_id(), DBHelper.EXISTENCE);
                        bookTitle.setText(mHistoryList.get(position).getTitle());
                    }
                }
            }
            if (msg.what == GET_WORD_FAIL) {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    };

    public BookListAdapter(Context context, List<BookListDatas> mHistoryList, TextView bookTitle) {
        this.mHistoryList = mHistoryList;
        this.context = context;
        this.bookTitle = bookTitle;
        netAvailable = HttpUtil.networkAvailable(context);
        dbHelper = new DBHelper(context, "Users.db", null, 1);
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIcon(R.drawable.book);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.recordView.setOnClickListener(v -> {
        });
        holder.content.setOnClickListener(v -> {
            if (!netAvailable) {
                Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT).show();
                return;
            }

            position = holder.getAdapterPosition();
            BookListDatas bookListDatas = mHistoryList.get(position);
            progressDialog.setTitle(bookListDatas.getTitle());
            progressDialog.setMessage("正在初始化...");
            /*if it have had word data in the database, we load it directly. if not, we get data from api*/
            if (!DBHelper.getExistence_item(context, bookListDatas.getClass_id())) {
                progressDialog.show();
                isUpdated = false;
                ExecutorService executorService = new ThreadPoolExecutor(1, 200, 120,
                        TimeUnit.SECONDS, new LinkedBlockingDeque<>());
                int total = Integer.parseInt(bookListDatas.getCourse_num());
                for (int i = 1; i <= total; i++) {
                    String finalUrl = new ApiAddress(context).getWordListAddress(bookListDatas.getClass_id(), i + "");
                    int finalI = i;
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            message = Message.obtain();
                            message.arg1 = finalI;
                            message.arg2 = total;
                            int what = GET_WORD_FAIL;
                            ;
                            String obj = null;
                            String result;
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder().url(finalUrl).build();
                            try {
                                Response response = client.newCall(request).execute();
                                result = Objects.requireNonNull(response.body()).string();
                                what = GET_WORD_SUCCESS;
                            } catch (IOException e) {
                                result = obj = e.getMessage();
                            }
                            if (what == GET_WORD_SUCCESS) {
                                Gson gson = new Gson();
                                WordList wordList;
                                wordList = gson.fromJson(result, WordList.class);
                                if (wordList.getCode().equals("202")) {
                                    try {
                                        executorService.awaitTermination(3, TimeUnit.SECONDS);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    return;
                                }
                                if (wordList.getCode().equals("206")) {
                                    /*api访问过于频繁*/
                                    alterUser();
                                }
                                ArrayList<com.example.test0_debug.tools.httpHelper.jsonParse.WordListData> dataList_word = wordList.getDatas();
                                StringBuilder sql = new StringBuilder(
                                        "insert into wordlist (msg,class_id,course_num,symbol,sound,name,class_title,desc_) values ");
                                for (int i = 0; i < dataList_word.size(); i++) {
                                    sql.append("('").append(wordList.getMsg()).append("',").
                                            append("'").append(wordList.getDatas().get(i).getClass_id()).append("',").
                                            append("'").append(wordList.getDatas().get(i).getCourse()).append("',").
                                            append("'").append(wordList.getDatas().get(i).getSymbol()).append("',").
                                            append("'").append(wordList.getDatas().get(i).getSound()).append("',").
                                            append("'").append(wordList.getDatas().get(i).getName()).append("',").
                                            append("'").append(wordList.getDatas().get(i).getClass_title()).append("',").
                                            append("'").append(wordList.getDatas().get(i).getDesc()).append("'),");
                                }
                                sql.deleteCharAt(sql.length() - 1).append(";");
                                obj = sql.toString();
                            }
                            message.what = what;
                            message.obj = obj;
                            handler.sendMessage(message);
                        }
                    });
                }
            }else{
                DBHelper.setBookClassId(context, 1, mHistoryList.get(position).getClass_id());
                DBHelper.setTodayProgress(context, 1, 1);
                DBHelper.setWordPosition(context, 0, 1);
                bookTitle.setText( mHistoryList.get(position).getTitle());
                Toast.makeText(context, "更换成功", Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookListDatas bookListDatas = mHistoryList.get(position);
        holder.content.setText(bookListDatas.getTitle());
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
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

    private void alterUser() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("update users set user='11590',appkey= 'fd9a685a09cefb3c3833b6a4b1171392' where appkey='62cb32159c84a2e6566eebcc9893b3a5'");
        db.close();
    }


}



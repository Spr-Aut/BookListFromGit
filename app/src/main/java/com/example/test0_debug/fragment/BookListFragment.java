package com.example.test0_debug.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test0_debug.activity.FavoritesActivity;
import com.example.test0_debug.activity.QueryActivity;
import com.example.test0_debug.R;
import com.example.test0_debug.activity.TranslatorActivity;
import com.example.test0_debug.book_list.BookListAdapter;
import com.example.test0_debug.book_list.BookListDatas;
import com.example.test0_debug.tools.QueryThread;
import com.example.test0_debug.tools.httpHelper.HttpUtil;
import com.example.test0_debug.tools.httpHelper.jsonParse.BookList;
import com.example.test0_debug.tools.httpHelper.jsonParse.BookListData;
import com.example.test0_debug.tools.db.DBHelper;
import com.example.test0_debug.tools.httpHelper.ApiAddress;
import com.example.test0_debug.tools.httpHelper.jsonParse.YouDaoDict;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookListFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "BookListFragment";
    private final List<BookListDatas> bookDataList = new ArrayList<>();
    private DBHelper dbHelper;
    private BookListAdapter adapter;
    private MyAsyncTask myAsyncTask;
    private boolean hasCathe = false;
    private Context context;
    private boolean netAvailable = false;
    private QueryThread queryThread;
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            /*有道字典查询结果*/
            super.handleMessage(msg);
            if (msg.what == QueryThread.SUCCESS) {
                String queryResult = (String) msg.obj;
                YouDaoDict dict = new Gson().fromJson(queryResult, YouDaoDict.class);
                Intent intent = new Intent(context, QueryActivity.class);
                intent.putExtra("dictData", dict);
                context.startActivity(intent);
            }
            if (msg.what == QueryThread.FAIL) {
                Log.e(TAG, (String) msg.obj);
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        netAvailable = HttpUtil.networkAvailable(context != null ? context : null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booklist_fragment, container, false);
        initBookList();
        initView(view);
        initData();
        return view;
    }

    private void initView(View v) {
        TextView bookTitle = v.findViewById(R.id.is_checked);
        TextView my_favorites = v.findViewById(R.id.my_favorites);
        TextView translator=v.findViewById(R.id.translator);
        my_favorites.setOnClickListener(this);
        translator.setOnClickListener(this);
        RecyclerView recycler_booklist = v.findViewById(R.id.recycler_booklist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycler_booklist.setLayoutManager(layoutManager);
        adapter = new BookListAdapter(context, bookDataList, bookTitle);
        recycler_booklist.setAdapter(adapter);
        if (hasCathe)
            bookTitle.setText(DBHelper.getBookTitle(context, 1));
        else
            bookTitle.setText("无");
        SearchView searchView = v.findViewById(R.id.search);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("单词查询");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单机搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (HttpUtil.networkAvailable(context)) {
                    String queryUrl = ApiAddress.getYoudaoAddress(query);
                    queryThread = new QueryThread(queryUrl, handler);
                    queryThread.start();
                } else {
                    Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            //用户输入字符时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                //如果newText不是长度为0的字符串
                //Toast.makeText(context, "newText", Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(newText)) {


                } else {

                }
                return true;
            }
        });
    }


    private void initBookList() {
        dbHelper = new DBHelper(context, "Users.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select*from booklist;";
        Cursor cursor = db.rawQuery(sql, null);
        hasCathe = cursor.moveToFirst() && cursor.getCount() != 0;
        if (hasCathe) {
            do {
                BookListDatas bookListDatas = new BookListDatas();
                bookListDatas.setClass_id(cursor.getString(cursor.getColumnIndex("class_id")));
                bookListDatas.setCourse_num(cursor.getString(cursor.getColumnIndex("course_num")));
                bookListDatas.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                bookListDatas.setWord_num(cursor.getString(cursor.getColumnIndex("word_num")));
                bookListDatas.setMsg(cursor.getString(cursor.getColumnIndex("msg")));
                bookDataList.add(bookListDatas);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    private void initData() {
        DBHelper.setDateNum(context, 1);

        if (!hasCathe) {
            if (netAvailable) {
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(new ApiAddress(context).getBookListAddress());
            } else {
                BookListDatas bookListDatas = new BookListDatas();
                bookListDatas.setTitle("无缓存数据且网络不可用");
                bookDataList.add(bookListDatas);
//                adapter.notifyDataSetChanged();
            }
        } else
            hasCathe = false;//缓存数据已经加载完，故设为false

    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(params[0]).build();
            try {
                Response response = client.newCall(request).execute();
                // publishProgress(++i);
                return Objects.requireNonNull(response.body()).string();
            } catch (IOException e) {
                return e.getMessage();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //    更新ProgressDialog的进度条

        }

        @Override
        protected void onPostExecute(String params) {
            Log.e(TAG, params);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            db.execSQL("delete from booklist;");
            Gson gson = new Gson();
            BookList bookList = gson.fromJson(params, BookList.class);
            ArrayList<BookListData> dataList = bookList.getData();
//            if (dataList.size() != 0) {
//                bookDataList.remove(0);
//                adapter.notifyDataSetChanged();
//            }
            StringBuilder sql = new StringBuilder(
                    "insert into booklist (msg,title,class_id,word_num,course_num,existence_item) values ");
            for (int i = 0; i < dataList.size(); i++) {
                for (int j = 0; j < dataList.get(i).getChild_list().size(); j++) {
                    sql.append("('").append(bookList.getMsg()).append("','")
                            .append(dataList.get(i).getChild_list().get(j).getTitle()).append("','")
                            .append(dataList.get(i).getChild_list().get(j).getClass_id()).append("','")
                            .append(dataList.get(i).getChild_list().get(j).getWord_num()).append("','")
                            .append(dataList.get(i).getChild_list().get(j).getCourse_num()).append("','")
                            .append(0).append("'),");
                    BookListDatas bookListDatas = new BookListDatas();
                    bookListDatas.setClass_id(dataList.get(i).getChild_list().get(j).getClass_id());
                    bookListDatas.setCourse_num(dataList.get(i).getChild_list().get(j).getCourse_num());
                    bookListDatas.setTitle(dataList.get(i).getChild_list().get(j).getTitle());
                    bookListDatas.setWord_num(dataList.get(i).getChild_list().get(j).getWord_num());
                    bookListDatas.setMsg(bookList.getMsg());
                    bookDataList.add(bookListDatas);
                }

            }
            sql.deleteCharAt(sql.length() - 1).append(";");
            adapter.notifyDataSetChanged();
            db.execSQL(sql.toString());
            db.close();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("myTAG", "onCancelled: ");
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_favorites:
                context.startActivity(new Intent(context, FavoritesActivity.class));
                break;
            case R.id.translator:
                context.startActivity(new Intent(context, TranslatorActivity.class));
                break;

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (myAsyncTask != null) {
            myAsyncTask.cancel(true);
            dbHelper.close();
        }
    }

}
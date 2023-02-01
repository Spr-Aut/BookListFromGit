package com.example.test0_debug.tools.httpHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.test0_debug.tools.db.DBHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ApiAddress {
    private String appKey = "";
    private String uid = "";

    public ApiAddress(Context context) {
        DBHelper dbHelper = new DBHelper(context, "Users.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users;", null);
        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            appKey = cursor.getString(cursor.getColumnIndex("appkey"));
            uid = cursor.getString(cursor.getColumnIndex("user"));
        } else {
            Toast.makeText(context, "初始化失败，缺少用户参数", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    public String getAppkey() {
        return appKey;
    }

    public String getUid() {
        return uid;
    }

    public String getBookListAddress(String name) {

        return "https://rw.ylapi.cn/reciteword/list.u?uid=" + uid + "&appkey=" + appKey + "&name=" + name;
    }

    public String getBookListAddress() {

        return "http://rw.ylapi.cn/reciteword/list.u?uid=" + uid + "&appkey=" + appKey;
    }

    public String getWordListAddress(String class_id, String course) {
        return "http://rw.ylapi.cn/reciteword/wordlist.u?uid=" + uid + "&appkey=" + appKey
                + "&class_id=" + class_id + "&course=" + course;
    }

    /*爱词霸API*/
    public static String getIcibaAddress(String word) {
        return "http://dict-co.iciba.com/api/dictionary.php?w=" + word
                + "&key=0EAE08A016D6688F64AB3EBB2337BFB0";
    }

    /*有道字典API*/
    public static String getYoudaoAddress(String str) {
        try {
            str = URLEncoder.encode(str, "UTF-8").replace("+", "%20"); //加号转换
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        str = str.replaceFirst("%EF%BB%BF", "");
        String url = "https://fanyi.youdao.com/openapi.do?keyfrom=aaa123ddd&key=336378893&type=data&doctype=json&version=1.1&q=" + str + "&only=dict";
        Log.e("url", url);
        return url;
    }

    public static String getTransAddress(String source, String target, String query) {
        String result;
        String id = "20190228000272190";
        String key = "aJM5JOieJ6wmMGCkL6kP";
        int salt = 1435660288;
        String parm = id + query + salt + key;
        String md5;
        try {
            String unicodeToUtf8 = new String(parm.getBytes("utf-8"), "utf-8");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputByteArray = unicodeToUtf8.getBytes("utf-8");
            messageDigest.update(inputByteArray);
            byte[] resultByteArray = messageDigest.digest();
            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            char[] resultCharArray = new char[resultByteArray.length * 2];
            int index = 0;
            for (byte b : resultByteArray) {
                resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
                resultCharArray[index++] = hexDigits[b & 0xf];
            }
            md5 = new String(resultCharArray);
            result = URLEncoder.encode(query, "UTF-8")
                    .replace("+", "%20");
        } catch (Exception ex) {
            return null;
        }
        return "https://api.fanyi.baidu.com/api/trans/vip/translate?" +
                "q=" + result + "&from=" + source + "&to=" + target + "&appid=" + id + "&salt=" +
                salt + "&sign=" + md5;
    }

    public static String getDaySenAddress(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String time=simpleDateFormat.format(new Date(System.currentTimeMillis()));
        String address="https://sentence.iciba.com/index.php?c=dailysentence&m=getdetail&title="+time;
        Log.e("http", "getDaySenAddress: "+address);
        return address;
    }
}

package com.example.test0_debug.tools.httpHelper.jsonParse;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Until;

import java.io.Serializable;
import java.util.List;


public class YouDaoDict implements Serializable {


    /**
     * basic : {"us-phonetic":"ˈæpl","phonetic":"ˈæpl","uk-phonetic":"ˈæpl","explains":["n. 苹果"]}
     * query : apple
     * errorCode : 0
     * web : [{"value":["苹果","苹果公司","苹果电脑","苹果汁"],"key":"Apple"},{"value":["苹果公司","美国苹果公司","果公司","苹果"],"key":"apple inc"},{"value":["苹果商店","苹果零售店","苹果店","苹果直营店"],"key":"Apple Store"}]
     */

    private BasicBean basic;
    private String query;
    private int errorCode;
    private List<WebBean> web;

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<WebBean> getWeb() {
        return web;
    }

    public void setWeb(List<WebBean> web) {
        this.web = web;
    }

    public static class BasicBean implements Serializable {
        /**
         * us-phonetic : ˈæpl
         * phonetic : ˈæpl
         * uk-phonetic : ˈæpl
         * explains : ["n. 苹果"]
         */

        @SerializedName("us-phonetic")
        private String usPhonetic;
        private String phonetic;
        @SerializedName("uk-phonetic")
        private String ukPhonetic;
        private List<String> explains;

        public String getUsPhonetic() {
            return usPhonetic;
        }

        public void setUsPhonetic(String usPhonetic) {
            this.usPhonetic = usPhonetic;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getUkPhonetic() {
            return ukPhonetic;
        }

        public void setUkPhonetic(String ukPhonetic) {
            this.ukPhonetic = ukPhonetic;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }
    }

    public static class WebBean implements Serializable {
        /**
         * value : ["苹果","苹果公司","苹果电脑","苹果汁"]
         * key : Apple
         */

        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }
}

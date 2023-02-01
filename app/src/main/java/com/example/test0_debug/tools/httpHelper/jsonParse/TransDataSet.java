package com.example.test0_debug.tools.httpHelper.jsonParse;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class TransDataSet implements Serializable {

    /**
     * from : zh
     * to : en
     * trans_result : [{"src":"在知道了 图10.1.3 框架下的注意力机制的主要成分。回顾一下，查询（自主提示）和键（非自主提示）之间的交互形成了 注意力汇聚（attention pooling）。注意力汇聚有选择地聚合了值（感官输入）以生成最终的输出。在本节中，我们将介绍注意力汇聚的更多细节，以便从宏观上了解注意力机制在实践中的运作方式。具体来说，1964 年提出的 Nadaraya-Watson 核回归模型是一个简单但完整的例子，可以用于演示具有注意力机制的机器学习。","dst":"After knowing the main components of the attention mechanism in the framework of figure 10.1.3. Recall that the interaction between queries (autonomous prompts) and keys (non autonomous prompts) forms attention pooling. Attention focus selectively aggregates values (sensory inputs) to produce the final output. In this section, we will introduce more details of attention gathering in order to understand the operation of attention mechanism in practice from a macro perspective. Specifically, the nadaraya Watson kernel regression model proposed in 1964 is a simple but complete example, which can be used to demonstrate machine learning with attention mechanism."}]
     */

    private String from;
    private String to;
    private List<TransResultBean> trans_result;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<TransResultBean> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<TransResultBean> trans_result) {
        this.trans_result = trans_result;
    }

    public static class TransResultBean implements Serializable {
        /**
         * src : 在知道了 图10.1.3 框架下的注意力机制的主要成分。回顾一下，查询（自主提示）和键（非自主提示）之间的交互形成了 注意力汇聚（attention pooling）。注意力汇聚有选择地聚合了值（感官输入）以生成最终的输出。在本节中，我们将介绍注意力汇聚的更多细节，以便从宏观上了解注意力机制在实践中的运作方式。具体来说，1964 年提出的 Nadaraya-Watson 核回归模型是一个简单但完整的例子，可以用于演示具有注意力机制的机器学习。
         * dst : After knowing the main components of the attention mechanism in the framework of figure 10.1.3. Recall that the interaction between queries (autonomous prompts) and keys (non autonomous prompts) forms attention pooling. Attention focus selectively aggregates values (sensory inputs) to produce the final output. In this section, we will introduce more details of attention gathering in order to understand the operation of attention mechanism in practice from a macro perspective. Specifically, the nadaraya Watson kernel regression model proposed in 1964 is a simple but complete example, which can be used to demonstrate machine learning with attention mechanism.
         */

        private String src;
        private String dst;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getDst() {
            dst = toURLDecoder(dst);
            return dst;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }
    }

    public static String toURLDecoder(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String url = new String(paramString.getBytes(), "UTF-8");
            url = URLDecoder.decode(url, "UTF-8");
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}

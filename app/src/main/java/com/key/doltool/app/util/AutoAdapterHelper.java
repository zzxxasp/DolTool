package com.key.doltool.app.util;

import com.key.doltool.adapter.AutoTextViewAdapter;

/**
 * 自动填充事件
 * Created by Administrator on 2016/12/15.
 */
public class AutoAdapterHelper {
    private static final String[] AUTO_EMAILS = {"@163.com", "@sina.com", "@qq.com", "@126.com", "@gmail.com", "@apple.com"};
    public void autoAddEmails(AutoTextViewAdapter adapter_email,String input) {
        String autoEmail;
        if (input.length() > 0) {
            for (String AUTO_EMAIL : AUTO_EMAILS) {
                if (input.contains("@")) {//包含“@”则开始过滤
                    String filter = input.substring(input.indexOf("@") + 1, input.length());//获取过滤器，即根据输入“@”之后的内容过滤出符合条件的邮箱
                    System.out.println("filter-->" + filter);
                    if (AUTO_EMAIL.contains(filter)) {//符合过滤条件
                        autoEmail = input.substring(0, input.indexOf("@")) + AUTO_EMAIL;//用户输入“@”之前的内容加上自动填充的内容即为最后的结果
                        adapter_email.mList.add(autoEmail);
                    }
                } else {
                    autoEmail = input + AUTO_EMAIL;
                    adapter_email.mList.add(autoEmail);
                }
            }
        }
    }
}

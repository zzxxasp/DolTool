package com.key.doltool.event;

import com.key.doltool.data.item.QuizItem;
import com.key.doltool.util.NumberUtil;

public class QuizEvent {
    //随机生成10个题目

    //发现物问题
    private QuizItem troveQuiz(){
        int mode=NumberUtil.getRandom(0,3);
        //随机模式：0：根据图片回答名称，1：根据图片回答星级，2：根据图片回答卡片点数
        switch (mode){
            case 0:break;
            case 1:break;
            case 2:break;
        }
        return null;
    }
}

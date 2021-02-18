package com.key.doltool.util;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * 全局字体管理
 * 版本：1.0
 * 原理：利用反射替换系统字体，然后在全局字体样式设置系统字体
 * **/
public class FontManager {
    public static String DEFAULT_FONT= "fonts/Wendy.ttf";
    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
}

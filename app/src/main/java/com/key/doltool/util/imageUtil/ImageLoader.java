package com.key.doltool.util.imageUtil;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.key.doltool.R;
import com.squareup.picasso.Picasso;

public class ImageLoader {

    /**
     * picasso 图片加载器，默认配置
     * **/
    public static void picassoLoad(Context context,String url,ImageView view){
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.dol_trove_defalut)
                .error(R.drawable.dol_trove_defalut)
                .config(Bitmap.Config.RGB_565)
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .fit()
                .centerCrop()
                .into(view);
    }
    public static void picassoLoadCirle(Context context,String url,ImageView view){
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.dol_trove_defalut)
                .error(R.drawable.dol_trove_defalut)
//                .config(Bitmap.Config.RGB_565)
                .fit()
                .centerCrop()
                .transform(new CircleTransform())
                .into(view);
    }
    public static void picassoLoadCirle(Context context,ImageView view){
        Picasso.with(context)
                .load(R.drawable.dol_trove_defalut)
                .placeholder(R.drawable.dol_trove_defalut)
                .error(R.drawable.dol_trove_defalut)
//                .config(Bitmap.Config.RGB_565)
                .fit()
                .centerCrop()
                .transform(new CircleTransform())
                .into(view);
    }
}


package com.zhai.shuyangwx.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        boolean isGif = ((String) path).contains("gif");
        if (isGif) {
            Glide.with(context)
                    .asGif() //判断加载的url资源是否为gif格式的资源
                    .load(path)
                    .into(imageView);
        } else {
            //Glide 加载图片简单用法
            Glide.with(context)
                    .load(path)
                    .into(imageView);
        }
    }
}
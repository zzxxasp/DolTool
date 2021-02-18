package com.key.doltool.event.rx;

import com.key.doltool.util.ExitApplication;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


/**
 * Rx通用工具类
 * Created by Administrator on 2017/2/28.
 */
public class RxCommonEvent {
    public static void delayExit(){
        Observable<Long> temp=Observable.interval(5, TimeUnit.SECONDS);
        temp.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                ExitApplication.getInstance().exit();
            }
        });
    }
}

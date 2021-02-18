package com.key.doltool.event.rx;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Rx系列总线调用
 * Created by Administrator on 2017/1/12.
 */
public class RxBusEvent {

    private static RxBusEvent instance;

    /**船只信息获取**/
    public final static String SAILBOAT="SAILBOAT";
    /**船只菜单获取**/
    public final static String SAILMENU="SAILMENU";
    /**更新事件**/
    public final static String UPDATE="UPDATE";
    public static RxBusEvent get() {
        if (instance == null) {
            synchronized (RxBusEvent.class) {
                if (instance == null) {
                    instance = new RxBusEvent();
                }
            }
        }
        return instance;
    }

    private RxBusEvent() {
    }

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    public boolean isRegister(@NonNull Object tag){
        List<Subject> subjects = subjectMapper.get(tag);
        return null != subjects;
    }


    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        Subject<T> subject;
        subjectList.add(subject = PublishSubject.create());
        return subject;
    }

    public void unregister(@NonNull Object tag) {
        List<Subject> subjects = subjectMapper.get(tag);
        if(isRegister(tag)){
            subjects.clear();
        }
    }

    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }

    public <T> void post(@NonNull Object tag, @NonNull T content) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (!isEmpty(subjectList)) {
            for (Subject subject : subjectList) {
                //noinspection unchecked
                subject.onNext(content);
            }
        }
    }

    private boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }
}
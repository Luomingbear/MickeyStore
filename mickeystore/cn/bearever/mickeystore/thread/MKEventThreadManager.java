package cn.bearever.mickeystore.thread;

import android.net.Uri;

import cn.bearever.mickeystore.db.MKDatabase;
import cn.bearever.mickeystore.db.MKDbManager;
import cn.bearever.mickeystore.info.MickeyCallbackInfo;
import cn.bearever.mickeystore.info.OnMickeyStoreListener;
import cn.bearever.util.UriSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用线程池来处理数据读写请求
 * 作者：luoming on 2018/10/18.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public class MKEventThreadManager {
    private Gson mGson;
    private ThreadPoolExecutor mThreadPoolExecutor; //执行请求的线程池，单线程

    public MKEventThreadManager() {
        mGson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();
        mThreadPoolExecutor = new ThreadPoolExecutor(1, 1000,
                1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
    }

    public void put(MickeyCallbackInfo info) {
        MickeyRunnable runnable = new MickeyRunnable(info);
        mThreadPoolExecutor.execute(runnable);
    }

    public boolean setData(String key, Object obj) {
        String v = mGson.toJson(obj);
        return MKDbManager.getInstance().setData(key, v);
    }

    public String getData(String key) {
        String json = MKDbManager.getInstance().getData(key);
        if (MKDatabase.EMPTY.equals(json)) {
            return null;
        }
        return json;
    }

    public <T> T getData(String key, Class<T> classOfT) {
        String json = getData(key);
        if (json == null) {
            return null;
        }
        return mGson.fromJson(getData(key), classOfT);
    }

    public void shutdown() {
        mThreadPoolExecutor.shutdown();
    }

    private class MickeyRunnable implements Runnable {
        MickeyCallbackInfo callbackInfo;

        private MickeyRunnable(MickeyCallbackInfo callbackInfo) {
            this.callbackInfo = callbackInfo;
        }

        @Override
        public void run() {
            if (callbackInfo.getEvent() == MickeyCallbackInfo.Event.GET) {
                //获取数据
                getData();
            } else if (callbackInfo.getEvent() == MickeyCallbackInfo.Event.SET) {
                //插入数据
                setData();
            } else if (callbackInfo.getEvent() == MickeyCallbackInfo.Event.REMOVE) {
                //移除数据
                removeData();
            } else if (callbackInfo.getEvent() == MickeyCallbackInfo.Event.CLEAR) {
                //清空数据
                clearData();
            }
        }

        private void getData() {
            String v = MKDbManager.getInstance().getData(callbackInfo.getKey());
            OnMickeyStoreListener listener = callbackInfo.getListener();
            if (listener != null) {
                if (MKDatabase.EMPTY.equals(v)) {
                    listener.failed();
                } else {
                    if (callbackInfo.getClazz() == null) {
                        listener.succeed(v);
                    } else {
                        try {
                            Object obj = mGson.fromJson(v, callbackInfo.getClazz());
                            listener.succeed(obj);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            listener.failed();
                        }
                    }
                }
            }
        }

        private void setData() {
            String v = mGson.toJson(callbackInfo.getValue());
            boolean suc = MKDbManager.getInstance().setData(callbackInfo.getKey(), v);
            OnMickeyStoreListener listener = callbackInfo.getListener();
            if (listener != null) {
                if (suc) {
                    listener.succeed(callbackInfo.getKey());
                } else {
                    listener.failed();
                }
            }
        }

        private void removeData() {
            boolean suc = MKDbManager.getInstance().removeData(callbackInfo.getKey());
            OnMickeyStoreListener listener = callbackInfo.getListener();
            if (listener != null) {
                if (suc) {
                    listener.succeed(callbackInfo.getKey());
                } else {
                    listener.failed();
                }
            }
        }

        private void clearData() {
            boolean suc = MKDbManager.getInstance().clear();
            OnMickeyStoreListener listener = callbackInfo.getListener();
            if (listener != null) {
                if (suc) {
                    listener.succeed(suc);
                } else {
                    listener.failed();
                }
            }
        }
    }

}

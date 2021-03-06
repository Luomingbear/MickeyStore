package cn.bearever.mickeystore;

import android.content.Context;

import cn.bearever.mickeystore.db.MKDbManager;
import cn.bearever.mickeystore.info.MickeyCallbackInfo;
import cn.bearever.mickeystore.info.OnMickeyStoreListener;
import cn.bearever.mickeystore.thread.MKEventThreadManager;


/**
 * 一个简单的key-value数据存取库
 * 使用方式查看 https://github.com/Luomingbear/MickeyStore
 * 作者：luoming on 2018/10/17.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public class MickeyStore {
    private MKEventThreadManager mMickeyEventThreadManager; //管理行为线程池的

    private static MickeyStore instance;

    public static MickeyStore getInstance() {
        if (instance == null) {
            synchronized (MickeyStore.class) {
                if (instance == null) {
                    instance = new MickeyStore();
                }
            }
        }
        return instance;
    }

    private MickeyStore() {
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        init(context, "");
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context, String tab) {
        MKDbManager.getInstance().init(context, tab);
        mMickeyEventThreadManager = new MKEventThreadManager();
    }

    /**
     * 同步设置数据
     *
     * @param key
     * @param obj
     * @return 是否成功
     */
    public boolean setData(String key, Object obj) {
        if (mMickeyEventThreadManager == null) {
            throw new NullPointerException("请先执行init()初始化");
        }

        return mMickeyEventThreadManager.setData(key, obj);
    }

    /**
     * 保存数据
     * 异步
     *
     * @param key
     * @param value
     */
    public void setData(String key, Object value, OnMickeyStoreListener listener) {
        if (mMickeyEventThreadManager == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        MickeyCallbackInfo info = new MickeyCallbackInfo();
        info.setEvent(MickeyCallbackInfo.Event.SET);
        info.setKey(key);
        info.setValue(value);
        info.setListener(listener);
        mMickeyEventThreadManager.put(info);
    }

    /**
     * 获取数据
     * 异步
     *
     * @param key
     * @return
     */
    public void getData(String key, OnMickeyStoreListener listener) {
        getData(key, null, listener);
    }

    /**
     * 获取数据
     * 同步
     *
     * @param key
     * @return
     */
    public String getData(String key) {
        if (mMickeyEventThreadManager == null) {
            throw new NullPointerException("请先执行init()初始化");
        }

        return mMickeyEventThreadManager.getData(key);
    }

    /**
     * 获取数据
     * 同步
     *
     * @param key
     * @return
     */
//    public <T> List<T> getArrayData(String key,Class<T> class4T) {
//        if (mMickeyEventThreadManager == null) {
//            throw new NullPointerException("请先执行init()初始化");
//        }
//
//        String json = getData(key);
//        if (TextUtils.isEmpty(json)) {
//            return Collections.emptyList();
//        }
//        Type type = new TypeToken<List<T>>() {
//        }.getType();
//        return new Gson().fromJson(json, type);
//    }

    /**
     * 获取数据
     * 同步
     *
     * @param key
     * @return
     */
    public <T> T getData(String key, Class<T> classOfT) {
        if (mMickeyEventThreadManager == null) {
            throw new NullPointerException("请先执行init()初始化");
        }


        return mMickeyEventThreadManager.getData(key, classOfT);
    }


    /**
     * 获取数据
     * 异步
     *
     * @param key
     * @return
     */
    public <T> void getData(String key, Class<T> clazz, OnMickeyStoreListener listener) {
        if (mMickeyEventThreadManager == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        MickeyCallbackInfo info = new MickeyCallbackInfo();
        info.setEvent(MickeyCallbackInfo.Event.GET);
        info.setKey(key);
        info.setClazz(clazz);
        info.setListener(listener);
        mMickeyEventThreadManager.put(info);
    }

    /**
     * 移除一个数据
     * 异步
     *
     * @param key
     */
    public void removeData(String key, OnMickeyStoreListener listener) {
        if (mMickeyEventThreadManager == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        MickeyCallbackInfo info = new MickeyCallbackInfo();
        info.setEvent(MickeyCallbackInfo.Event.REMOVE);
        info.setKey(key);
        info.setListener(listener);
        mMickeyEventThreadManager.put(info);
    }

    /**
     * 清空数据
     */
    public void clear() {
        if (mMickeyEventThreadManager == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        MickeyCallbackInfo info = new MickeyCallbackInfo();
        info.setEvent(MickeyCallbackInfo.Event.CLEAR);
        mMickeyEventThreadManager.put(info);
    }

}

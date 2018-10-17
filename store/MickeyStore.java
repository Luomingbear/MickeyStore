package com.lingsir.market.appcommon.store;

import android.content.Context;

import com.google.gson.Gson;
import com.lingsir.market.appcommon.store.db.MickeyDbUtil;

/**
 * 一个简单的key-value数据存取库
 * 作者：luoming on 2018/10/17.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public class MickeyStore {
    private MickeyDbUtil mMickeyDbUtil;
    private Gson mGson;

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
        mMickeyDbUtil = new MickeyDbUtil(context, tab);
        mGson = new Gson();
    }

    /**
     * 保存数据
     *
     * @param key
     * @param value
     */
    public void setData(String key, Object value) {
        if (mMickeyDbUtil == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        String v = mGson.toJson(value);
        mMickeyDbUtil.open();
        mMickeyDbUtil.insertOrUpdate(key, v);
        mMickeyDbUtil.close();
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public <T> T getData(String key, Class<T> classOfT) {
        if (mMickeyDbUtil == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        mMickeyDbUtil.open();
        String v = mMickeyDbUtil.getValue(key);
        mMickeyDbUtil.close();
        if (MickeyDbUtil.EMPTY.equals(v)) {
            return null;
        }
        return mGson.fromJson(v, classOfT);
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public String getData(String key) {
        if (mMickeyDbUtil == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        mMickeyDbUtil.open();
        String v = mMickeyDbUtil.getValue(key);
        mMickeyDbUtil.close();
        if (MickeyDbUtil.EMPTY.equals(v)) {
            return "";
        }
        return v;
    }

    /**
     * 移除一个数据
     *
     * @param key
     */
    public void removeData(String key) {
        if (mMickeyDbUtil == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        mMickeyDbUtil.open();
        mMickeyDbUtil.delete(key);
        mMickeyDbUtil.close();
    }

    /**
     * 清空数据
     */
    public void clear() {
        if (mMickeyDbUtil == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        mMickeyDbUtil.open();
        mMickeyDbUtil.clear();
        mMickeyDbUtil.close();
    }

}

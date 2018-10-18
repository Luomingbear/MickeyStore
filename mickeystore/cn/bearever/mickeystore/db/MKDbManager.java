package cn.bearever.mickeystore.db;

import android.content.Context;

/**
 * 作者：luoming on 2018/10/18.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public class MKDbManager {
    private MKDatabase mDatabase;
    private static MKDbManager instance;

    public static MKDbManager getInstance() {
        if (instance == null) {
            synchronized (MKDbManager.class) {
                if (instance == null) {
                    instance = new MKDbManager();
                }
            }
        }
        return instance;
    }

    protected MKDbManager() {

    }

    public void init(Context context, String tab) {
        mDatabase = new MKDatabase(context, tab);
    }

    /**
     * 保存数据
     *
     * @param key
     * @param value
     */
    public boolean setData(String key, String value) {
        if (mDatabase == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        mDatabase.open();
        boolean suc = mDatabase.insertOrUpdate(key, value);
        mDatabase.close();
        return suc;
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public String getData(String key) {
        if (mDatabase == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        mDatabase.open();
        String v = mDatabase.getValue(key);
        mDatabase.close();
        return v;
    }

    /**
     * 移除一个数据
     *
     * @param key
     */
    public boolean removeData(String key) {
        if (mDatabase == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        mDatabase.open();
        boolean suc = mDatabase.delete(key);
        mDatabase.close();
        return suc;
    }

    /**
     * 清空数据
     */
    public boolean clear() {
        if (mDatabase == null) {
            throw new NullPointerException("请先执行init()初始化");
        }
        mDatabase.open();
        mDatabase.clear();
        mDatabase.close();
        return true;
    }
}

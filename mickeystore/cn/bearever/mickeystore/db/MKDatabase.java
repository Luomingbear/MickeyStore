package cn.bearever.mickeystore.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

/**
 * 数据保存的数据库底层
 * 作者：luoming on 2018/10/17.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public class MKDatabase {
    private static final String TAG = "MKDatabase";
    private static String mDatabaseName = "mickeydb.db";
    private static int mDatabaseVersion = 1;
    private String mDataTab = "mickeys";
    private static String KEY = "vkey";
    private static String VALUE = "vvalue";
    public static String EMPTY = "EMPTY-EMPTY-EMPTY-luoming-EMPTY-EMPTY-EMPTY"; //获取的key value不存在的返回值
    private String CREATE_TAB;
    private String CLEAR_TAB;

    private Context mContext;
    private MickeySqlLiteHelper mMickeySqlLiteHelper;
    private SQLiteDatabase mSqLiteDatabase;

    public MKDatabase(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public MKDatabase(Context context, String tabName) {
        this.mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty(tabName)) {
            this.mDataTab = tabName;
        }
        CREATE_TAB = "CREATE TABLE " + mDataTab + "( " + KEY + " varchar(255)," + VALUE + " text ,PRIMARY KEY (" + KEY + "))";
        CLEAR_TAB = "DELETE FROM " + mDataTab;
    }

    public void open() {
        if (mMickeySqlLiteHelper == null) {
            mMickeySqlLiteHelper = new MickeySqlLiteHelper(mContext);
        }
        mSqLiteDatabase = mMickeySqlLiteHelper.getWritableDatabase();
    }

    public void close() {
        if (mMickeySqlLiteHelper != null) {
            mMickeySqlLiteHelper.close();
        }
    }

    /**
     * 插入数据
     *
     * @param key
     * @param value
     */
    public boolean insert(String key, String value) {
        if (mSqLiteDatabase == null) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY, key);
        contentValues.put(VALUE, value);
        return mSqLiteDatabase.insert(mDataTab, null, contentValues) != -1;
    }

    /**
     * 更新数据
     *
     * @param key
     * @param value
     */
    public boolean update(String key, String value) {
        if (mSqLiteDatabase == null) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY, key);
        contentValues.put(VALUE, value);
        return mSqLiteDatabase.update(mDataTab, contentValues, KEY + " = ?", new String[]{key}) > 0;
    }

    /**
     * 插入获取更新数据
     * 之前没有这个key的数据就直接插入，否则更新value的值
     *
     * @param key
     * @param value
     */
    public boolean insertOrUpdate(String key, String value) {
        if (mSqLiteDatabase == null) {
            return false;
        }
        if (EMPTY.equals(getValue(key))) {
            return insert(key, value);
        } else {
            return update(key, value);
        }
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public String getValue(String key) {
        if (mSqLiteDatabase == null) {
            return EMPTY;
        }
        Cursor cursor = null;
        try {
            cursor = mSqLiteDatabase.query(mDataTab, new String[]{VALUE}, KEY + " = ?",
                    new String[]{key}, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(VALUE));
            } else {
                return EMPTY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return EMPTY;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 删除数据
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        if (mSqLiteDatabase == null) {
            return false;
        }
        return mSqLiteDatabase.delete(mDataTab, KEY + " = ?", new String[]{key}) > 0;
    }

    /**
     * 清空数据
     */
    public void clear() {
        if (mSqLiteDatabase == null) {
            return;
        }
        mSqLiteDatabase.execSQL(CLEAR_TAB);
    }

    private class MickeySqlLiteHelper extends SQLiteOpenHelper {
        public MickeySqlLiteHelper(Context context) {
            super(context, mDatabaseName, null, mDatabaseVersion);
        }

        public MickeySqlLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, mDatabaseName, factory, mDatabaseVersion);
        }

        public MickeySqlLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                   int version, DatabaseErrorHandler errorHandler) {
            super(context, mDatabaseName, factory, mDatabaseVersion, errorHandler);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "onCreate: " + CREATE_TAB);
            db.execSQL(CREATE_TAB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "onUpgrade: ");
            db.execSQL(CLEAR_TAB);
            db.execSQL(CREATE_TAB);
        }
    }
}


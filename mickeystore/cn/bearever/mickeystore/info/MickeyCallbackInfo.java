package cn.bearever.mickeystore.info;

/**
 * 作者：luoming on 2018/10/18.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public class MickeyCallbackInfo {
    private Event event = Event.SET;
    private String key;
    private Object value;
    private Class clazz;
    private OnMickeyStoreListener listener;

    public enum Event {
        GET,
        SET,
        REMOVE,
        CLEAR,
    }

    public MickeyCallbackInfo() {
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public OnMickeyStoreListener getListener() {
        return listener;
    }

    public void setListener(OnMickeyStoreListener listener) {
        this.listener = listener;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}

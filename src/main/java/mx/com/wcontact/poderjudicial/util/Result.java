package mx.com.wcontact.poderjudicial.util;

public class Result<T> {

    private int state;
    private T object;
    private String result;

    public Result() { }

    public Result(int state, T object, String result) {
        this.state = state;
        this.object = object;
        this.result = result;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

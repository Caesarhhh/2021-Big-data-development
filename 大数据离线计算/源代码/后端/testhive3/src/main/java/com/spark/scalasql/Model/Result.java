package com.spark.scalasql.Model;

public class Result {
    private int status;
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    public static Result succeed(Object data) {
        return new Result(200, data);
    }

    public static Result fail(Object data) {
        return new Result(400, data);
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}

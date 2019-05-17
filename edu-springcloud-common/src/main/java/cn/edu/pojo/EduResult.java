package cn.edu.pojo;

/**
 * 自定义响应结构
 */
public class EduResult {

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    public static EduResult build(Integer status, String msg, Object data) {
        return new EduResult(status, msg, data);
    }

    public static EduResult ok(Object data) {
        return new EduResult(data);
    }

    public static EduResult ok() {
        return new EduResult(null);
    }

    public EduResult() {

    }

    public static EduResult build(Integer status, String msg) {
        return new EduResult(status, msg, null);
    }

    public EduResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public EduResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }



}

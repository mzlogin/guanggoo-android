package org.mazhuang.guanggoo.data.task;

/**
 * @author mazhuang
 * @date 2018/4/26
 */
public class BaseResponse {
    private String message;
    private int success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}

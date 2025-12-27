package com.tiny.message.sender;

import lombok.Data;

/**
 * 发送结果
 */
@Data
public class SendResult {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 第三方消息ID
     */
    private String thirdPartyId;

    /**
     * 请求数据
     */
    private String requestData;

    /**
     * 响应数据
     */
    private String responseData;

    /**
     * 成功结果
     */
    public static SendResult success() {
        SendResult result = new SendResult();
        result.setSuccess(true);
        return result;
    }

    /**
     * 成功结果(带第三方ID)
     */
    public static SendResult success(String thirdPartyId) {
        SendResult result = success();
        result.setThirdPartyId(thirdPartyId);
        return result;
    }

    /**
     * 失败结果
     */
    public static SendResult fail(String errorMsg) {
        SendResult result = new SendResult();
        result.setSuccess(false);
        result.setErrorMsg(errorMsg);
        return result;
    }

    /**
     * 失败结果(带错误码)
     */
    public static SendResult fail(String errorCode, String errorMsg) {
        SendResult result = fail(errorMsg);
        result.setErrorCode(errorCode);
        return result;
    }
}

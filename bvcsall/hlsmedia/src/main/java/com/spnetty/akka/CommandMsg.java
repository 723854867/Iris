package com.spnetty.akka;

/**
 * 命令
 */
public class CommandMsg {

    public enum Type {
        /**
         * 登录
         */
        login(),
        /**
         * 登出
         */
        logout(),
        /**
         * 关闭连接
         */
        disconnect(),
        /**
         * 查询用户名
         */
        findByUsername(),

        /**
         * 更改用户名
         */
        username(),
        /**
         * 查询时间
         */
        datetime(),
        /**
         * 查询全部用户
         */
        users()
    }

    /**
     * 发给谁,uid
     */
    String uid;

    /**
     * 命令类型
     */
    Type type;

    /**
     * 成功
     */
    public static final String RESULT_OK = "ok";
    /**
     * 成功,但是结果是空
     */
    public static final String RESULT_NULL = "null";
    /**
     * 成功,但是结果是空
     */
    public static final String RESULT_FAIL = "fail";

    /**
     * 服务器返回给客户端的结果信息
     */
    String result;
    /**
     * 命令内容
     */
    String content;


    Object addtions[];

    public Object[] getAddtions() {
        return addtions;
    }

    public void setAddtions(Object[] addtions) {
        this.addtions = addtions;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Command{" +
                "uid=" + uid +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}

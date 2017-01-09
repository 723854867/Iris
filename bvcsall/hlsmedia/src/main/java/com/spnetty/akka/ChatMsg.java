package com.spnetty.akka;

import java.util.Arrays;

/**
 * 聊天消息,单聊
 */
public class ChatMsg {

    public enum ContentType {
        /**
         * 文本
         */
        txt(),
        /**
         * 图片
         */
        jpg(),
        /**
         * 动态图片
         */
        gif(),
        /**
         * 声音文件
         */
        mp3(),
        /**
         * 图文混排
         */
        rtf()
    }


    /**
     * 来自谁,uid
     */
    String fromWhoUID;

    /**
     * 来自谁,名字
     */
    String fromWhoName;


    /**
     * 发给谁,uid
     */
    String toWhoUID;
    /**
     * 发给谁,名字
     */
    String toWhoName;

    /**
     * 消息内容,当内容类型是图片或者是rtf图文混排时
     */
    byte[] raw;
    /**
     * 消息内容类型
     */
    ContentType contentType = ContentType.txt;
    /**
     * 文本内容,当
     */
    String content;
    /**
     * 标题
     */
    String title;

    public enum ResultType {
        ok(),
        exception(),
        timeout(),
        user_not_available(),
        service_not_available()
    }

    /**
     * 结果
     */
    ResultType result;

    public String getFromWhoUID() {
        return fromWhoUID;
    }

    public void setFromWhoUID(String fromWhoUID) {
        this.fromWhoUID = fromWhoUID;
    }

    public String getFromWhoName() {
        return fromWhoName;
    }

    public void setFromWhoName(String fromWhoName) {
        this.fromWhoName = fromWhoName;
    }

    public String getToWhoUID() {
        return toWhoUID;
    }

    public void setToWhoUID(String toWhoUID) {
        this.toWhoUID = toWhoUID;
    }

    public String getToWhoName() {
        return toWhoName;
    }

    public void setToWhoName(String toWhoName) {
        this.toWhoName = toWhoName;
    }

    public byte[] getRaw() {
        return raw;
    }

    public void setRaw(byte[] raw) {
        this.raw = raw;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ResultType getResult() {
        return result;
    }

    public void setResult(ResultType result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "fromWhoUID=" + fromWhoUID +
                ", fromWhoName=" + fromWhoName +
                ", toWhoUID=" + toWhoUID +
                ", toWhoName=" + toWhoName +
                ", title='" + title + '\'' +
                ", contentType=" + contentType +
                ", content='" + content + '\'' +
                ", raw=" + Arrays.toString(raw) +
                ", result='" + result + '\'' +
                '}';
    }
}

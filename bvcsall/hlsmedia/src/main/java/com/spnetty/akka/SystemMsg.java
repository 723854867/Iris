package com.spnetty.akka;

import java.util.Arrays;

/**
 * 系统通知
 */
public class SystemMsg {

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
     * 发给谁,uid
     */
    String toWhoUID;
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

    public String getToWhoUID() {
        return toWhoUID;
    }

    public void setToWhoUID(String toWhoUID) {
        this.toWhoUID = toWhoUID;
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

    @Override
    public String toString() {
        return "Chat{" +
                "toWhoUID=" + toWhoUID +
                ", title='" + title + '\'' +
                ", contentType=" + contentType +
                ", content='" + content + '\'' +
                ", raw=" + Arrays.toString(raw) +
                '}';
    }
}

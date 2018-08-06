package com.kuliao.oa.app.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author 董文强
 * @Time 2018/6/28 15:46
 */
@Document
public class Message {

    @Id
    private String id;      // 注解属性id为ID
    private Date time;
    private String title;
    private String readState;
    public static final String UNREAD = "未读";
    public static final String READ = "已读";
    public static final String RECYCLE = "已删除";

    public Message() {}
    public Message(String title) {
        this.title = title;
    }
    public Message(String id, Date time, String title, String readState) {
        this.id = id;
        this.time = time;
        this.title = title;
        this.readState = readState;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", time=" + time +
                ", title='" + title + '\'' +
                ", readState='" + readState + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }
}

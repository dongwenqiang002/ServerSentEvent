package com.kuliao.oa.app.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author 董文强
 * @Time 2018/7/3 12:36
 */
@Document
public class User {


    String id;
    @Id
    String empNo;
    String name;
    List<String> messageId;



    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", empNo='" + empNo + '\'' +
                ", name='" + name + '\'' +
                ", messageId=" + messageId +
                '}';
    }

    public User(String id, String empNo, String name, List<String> messageId) {
        this.id = id;
        this.empNo = empNo;
        this.name = name;
        this.messageId = messageId;
    }

    public List<String> getMessageId() {
        return messageId;
    }

    public void setMessageId(List<String> messageId) {
        this.messageId = messageId;
    }
    public User(String id, String empNo, String name) {
        this.id = id;
        this.empNo = empNo;
        this.name = name;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

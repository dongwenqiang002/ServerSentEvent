package com.kuliao.oa.app.service;

import com.kuliao.oa.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author 董文强
 * @Time 2018/6/26 10:19
 * 这是一个事件的消息源
 */
@Component
public class EventSource<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventSource.class);

    @Autowired
    UserRepository userRepository;


    /**
     * 实时发送消息，完成消息与用户的绑定
     *
     * @param message 消息
     * @param empNo   用户标识符
     */
    public void sentEvent(T message, String empNo) {
        if (empNo == null) {
            this.notifyObservers(message);
        } else {
            this.notifyClient(empNo, message);
        }

    }

    /**
     * 用户订阅消息，订阅后用户可以源源不断的获取新到来的消息
     *
     * @param empNo 用户标识符
     */
    public Client Subscribe(Client<? super T> client, String empNo) {
        this.addObserver(client, empNo);
        return client;
    }

    /**
     * 用户取消订阅
     * */
    public void unsubscribe(Client<? super T> client, String empNo) {
        this.deleteObserver(empNo);
    }

    /**
     * 接收消息的客户端
     */
    @FunctionalInterface
    public interface Client<V> {
        void update(EventSource o, V arg);
    }

    private boolean changed = false;
    //private Vector<Client> obs;
    private Map<String, Client> obs;

    public EventSource() {
        obs = new HashMap<>();
    }


    public synchronized void addObserver(Client o, String empNo) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.containsKey(empNo)) {
            obs.put(empNo, o);
        }
    }


    public synchronized void deleteObserver(String empNo) {
        obs.remove(empNo);
    }


    public void notifyObservers() {
        notifyObservers(null);
    }


    public void notifyObservers(T arg) {

        Object[] arrLocal;

        synchronized (this) {

            if (!changed)
                return;
            arrLocal = obs.values().toArray();
            clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--)
            ((Client) arrLocal[i]).update(this, arg);
    }

    private void notifyClient(String empNo, T message) {
        Client client = this.obs.get(empNo);
        if (client != null) {
            client.update(this, message);
        }
    }

    public synchronized void deleteObservers() {
        obs.clear();
    }


    protected synchronized void setChanged() {
        changed = true;
    }


    protected synchronized void clearChanged() {
        changed = false;
    }


}

package com.baidu_lishuang10.ipcmessenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by baidu_lishuang10 on 15/11/20.
 */
public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MSG_FROM_CLIENT:
                    Log.i(TAG, "receive msg from Client" + msg.getData().getString("msg"));
                    Messenger client  = msg.replyTo;
                    Message replyMessage = Message.obtain(null,Constants.MSG_FROM_SERVICE);
                    Bundle bundle  = new Bundle();
                    bundle.putString("msg","已收到客户端的消息");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);//为客户端发送消息
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Messenger messenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}

package com.baidu_lishuang10.ipcmessenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


/**
 * Created by baidu_lishuang10 on 15/11/20.
 */
public class MessengerActivity extends Activity {
    private static final String TAG = "MessengerActivity";

    private Messenger mService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message message = Message.obtain(null, Constants.MSG_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "hello,this is client.");
            message.setData(bundle);
            message.replyTo = mClient;//设置接受消息的messenger
            try {
                mService.send(message);//向服务端发送消息,消息必须发到Message中
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_SERVICE:
                    Log.i(TAG, "msg from service : " + msg.getData().getString("msg"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Messenger mClient = new Messenger(new MessageHandler());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}

package com.jucongyuan.aidlclient.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jucongyuan.IConnect;
import com.jucongyuan.Student;
import com.jucongyuan.aidlclient.R;


public class ClientActivity extends Activity {

    private TextView tv;
    private IConnect connect;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                connect = IConnect.Stub.asInterface(service);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        tv = (TextView) findViewById(R.id.tv);
        Intent intent = new Intent();
        intent.setAction(IConnect.class.getName());
        intent.setClassName("com.jucongyuan.aidlserver", "com.jucongyuan.aidlserver.service.ServerService");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

        findViewById(R.id.btnGet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //修改部分
                    Student student = new Student();
                    student.setName("张三");
                    student = connect.getStr(student);
                    tv.setText(student.getName() + "的学号是：" + student.getNumber());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}

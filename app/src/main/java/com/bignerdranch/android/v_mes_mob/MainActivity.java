package com.bignerdranch.android.v_mes_mob;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends Activity {
    private EditText etName, etMes;
    private ListView lv;
    private ImageButton btnSend;

    private volatile Socket s;
    private PrintWriter pw;
    private ArrayList<Message> messages;
    private MessageAdapter adapter;
    public static Handler handler;

    public static final int MES_NAME = 1;
    public static final int MES_MES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                 try {
                     int serverPort = 6666;
                     String address = "192.168.1.4";

                     InetAddress ipAddress = InetAddress.getByName(address);
                     s = new Socket(ipAddress, serverPort);

                     Scanner sc = new Scanner(s.getInputStream());
                     while (true) {
                         String resName = sc.nextLine();
                         String resMes = sc.nextLine();
                         handler.sendMessage(handler.obtainMessage(MainActivity.MES_NAME, resName));
                         handler.sendMessage(handler.obtainMessage(MainActivity.MES_MES, resMes));
                     }
                } catch (Exception e) {e.printStackTrace();}
            }
        }).start();

        btnSend = (ImageButton)findViewById(R.id.btnSend);
        lv = (ListView) findViewById(R.id.lv);
        etName = (EditText) findViewById(R.id.et_name);
        etMes = (EditText) findViewById(R.id.et_mes);

        messages = new ArrayList<>();
        adapter = new MessageAdapter(this, messages);
        lv.setAdapter(adapter);

        handler = new MyHandler(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sendName = etName.getText().toString();
                final String sendMes = etMes.getText().toString();

                etMes.setText("");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            pw = new PrintWriter(s.getOutputStream());
                            pw.write(sendName + "\n");
                            pw.write(sendMes + "\n");
                            pw.flush();
                        } catch (IOException e) {e.printStackTrace();}
                    }
                }).start();
            }
        });
    }

    static class MyHandler extends Handler {
        private WeakReference<MainActivity> wr;

        public MyHandler(MainActivity activity) {
            wr = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            MainActivity activity = wr.get();

            if (activity != null) {
                if (msg.what == MainActivity.MES_NAME) {
                    activity.setTextToListView(MainActivity.MES_NAME, msg.obj.toString());
                } else if (msg.what == MainActivity.MES_MES) {
                    activity.setTextToListView(MainActivity.MES_MES, msg.obj.toString());
                }
            }
        }

    }
    public void setTextToListView(int key, String msg) {
        if (key == MainActivity.MES_NAME) {
            messages.add(new Message(msg + ": ", null));
        } else if (key == MainActivity.MES_MES) {
            messages.get(messages.size()-1).setMessage(msg);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        try {
            if(s.isConnected())
                s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}


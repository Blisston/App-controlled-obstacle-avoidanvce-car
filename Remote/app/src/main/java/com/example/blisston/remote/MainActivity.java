package com.example.blisston.remote;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class MainActivity extends AppCompatActivity {
    Socket socket;
     EditText e1;
    public static String ip1;
    public static int port1;
    Button b1,bup,bdown,bright,bleft;
    TextView t1;
    Socket Myappsocket=null;
    DataOutputStream out;
    DataInputStream in;
    String cmd="start";
    String send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.button);
        bup=(Button)findViewById(R.id.btn_up);
        bdown=(Button)findViewById(R.id.btn_down);
        bright=(Button)findViewById(R.id.btn_right);
        bleft=(Button)findViewById(R.id.btn_left);
        e1 = (EditText) findViewById(R.id.editText2);
        t1=(TextView)findViewById(R.id.textView2);
        t1.setText("No Obstacle");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Socket_AsyncTask abc = new Socket_AsyncTask();

                String port = e1.getText().toString();
                String temp[] = port.split(":");
                ip1 = temp[0];
                port1 = Integer.valueOf(temp[1]);

                abc.execute();

            }
        });
        bup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Socket_AsyncTask abc = new Socket_AsyncTask();
                String port = e1.getText().toString();
                String temp[] = port.split(":");
                ip1 = temp[0];
                port1 = Integer.valueOf(temp[1]);
                cmd="up";

                abc.execute();

            }
        });
        bdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Socket_AsyncTask abc = new Socket_AsyncTask();
                String port = e1.getText().toString();
                String temp[] = port.split(":");
                ip1 = temp[0];
                port1 = Integer.valueOf(temp[1]);
                cmd="down";
                abc.execute();

            }
        });
        bright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Socket_AsyncTask abc = new Socket_AsyncTask();
                String port = e1.getText().toString();
                String temp[] = port.split(":");
                ip1 = temp[0];
                port1 = Integer.valueOf(temp[1]);
                cmd="right";

                abc.execute();

            }
        });
        bleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Socket_AsyncTask abc = new Socket_AsyncTask();
                String port = e1.getText().toString();
                String temp[] = port.split(":");
                ip1 = temp[0];
                port1 = Integer.valueOf(temp[1]);
                cmd="left";
                abc.execute();

            }
        });
    }
                public class Socket_AsyncTask extends AsyncTask<Void, Void, Void> {
                    Socket socket;
                    @Override
                    protected Void doInBackground(Void... params) {

                        try {
                            InetAddress inet = InetAddress.getByName(MainActivity.ip1);
                            Myappsocket = new java.net.Socket(inet, MainActivity.port1);

                            out = new DataOutputStream(Myappsocket.getOutputStream());
                            out.writeBytes(cmd);
                            in = new DataInputStream(Myappsocket.getInputStream());
                            Byte sent = in.readByte();
                            Log.i(sent.toString(),sent.toString());
                            send = sent.toString();
                            if(send.equals("104")) {
                                send = "Obstacle Detected";

                            }
                            if(send.equals("97")) {
                                send = "No Obstacle";

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    t1.setText(send);
                                }
                            });


                        } catch (UnknownHostException e) {

                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                        return null;
                    }
                }
            }



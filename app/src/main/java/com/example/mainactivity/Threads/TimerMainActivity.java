package com.example.mainactivity.Threads;

import android.util.Log;

import com.example.mainactivity.Monitor;
import com.example.mainactivity.Singleton;

public class TimerMainActivity extends Thread {
    private Monitor monitor;

    public TimerMainActivity(Monitor monitor)
    {
        this.monitor = monitor;
    }
    @Override
    public void run()
    {
        int counter = 0;
        while(!Singleton.getInstance().isEndConnectionThreadMainActivity())
        {
            if(counter == 300)
            {
                monitor.setStopThread(false);
                monitor.activeThread();
                //monitor.setStopThread(true);
                counter = 0;
            }
            counter++;
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

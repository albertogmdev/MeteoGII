package com.example.mainactivity;

import android.util.Log;

public class Timer extends Thread {
    private Monitor monitor;

    public Timer(Monitor monitor)
    {
        this.monitor = monitor;
    }
    @Override
    public void run()
    {
        int counter = 0;
        while(!Singleton.getInstance().isEndConnectionThread())
        {
            if(counter == 10)
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

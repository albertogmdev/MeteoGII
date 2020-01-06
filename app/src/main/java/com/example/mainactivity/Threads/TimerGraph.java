package com.example.mainactivity.Threads;

import com.example.mainactivity.Monitor;
import com.example.mainactivity.Singleton;

public class TimerGraph extends Thread{
    private Monitor monitor;

    public TimerGraph(Monitor monitor)
    {
        this.monitor = monitor;
    }
    @Override
    public void run()
    {
        int counter = 0;
        while(!Singleton.getInstance().isEndConnectionThreadGraphActivity())
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

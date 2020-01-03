package com.example.mainactivity;

public class Monitor {
    private boolean stopThread;
    private boolean stopThreadResetTable;
    private boolean stopThreadHeader;
    private boolean stopThreadData;

    public Monitor()
    {
        this.stopThread = true;
        this.stopThreadResetTable = true;
        this.stopThreadHeader = true;
        this.stopThreadData = true;
    }

    public synchronized void makeThreadWaitResetTable()
    {
        while(stopThreadResetTable)
        {
            try
            {
                wait();
            }
            catch(Exception e)
            {

            }
        }
    }

    public synchronized void makeThreadWaitHeader()
    {
        while(stopThreadHeader)
        {
            try
            {
                wait();
            }
            catch(Exception e)
            {

            }
        }
    }
    public synchronized void makeThreadWaitData()
    {
        while(stopThreadData)
        {
            try
            {
                wait();
            }
            catch(Exception e)
            {

            }
        }
    }

    public synchronized void makeThreadWait()
    {
        while(stopThread)
        {
            try
            {
                wait();
            }
            catch(Exception e)
            {

            }
        }
    }

    public boolean isStopThread() {
        return stopThread;
    }

    public void setStopThread(boolean stopThread) {
        this.stopThread = stopThread;
    }

    public boolean isStopThreadResetTable() {
        return stopThreadResetTable;
    }

    public void setStopThreadResetTable(boolean stopThreadResetTable) {
        this.stopThreadResetTable = stopThreadResetTable;
    }

    public boolean isStopThreadHeader() {
        return stopThreadHeader;
    }

    public void setStopThreadHeader(boolean stopThreadHeader) {
        this.stopThreadHeader = stopThreadHeader;
    }

    public boolean isStopThreadData() {
        return stopThreadData;
    }

    public void setStopThreadData(boolean stopThreadData) {
        this.stopThreadData = stopThreadData;
    }

    public synchronized void activeThread()//To tell the threads inside the monitor to look if the "Resume" Button has been pressed
    {
        notifyAll();
    }
}

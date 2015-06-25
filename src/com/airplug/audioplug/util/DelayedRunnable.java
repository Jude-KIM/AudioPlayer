package com.airplug.audioplug.util;

import java.util.Timer;
import java.util.TimerTask;

public abstract class DelayedRunnable
{
  private long mDelay;
  private Timer mTimer;

  public DelayedRunnable(long paramLong)
  {
    this.mDelay = paramLong;
  }

  public void cancelRun()
  {
    if (this.mTimer != null)
    {
      this.mTimer.cancel();
      this.mTimer = null;
    }
  }

  public abstract void run();

  public void scheduleRun()
  {
    cancelRun();
    this.mTimer = new Timer();
    this.mTimer.schedule(new DelayedTimerTask(), this.mDelay);
  }

  private class DelayedTimerTask extends TimerTask
  {
    private DelayedTimerTask()
    {
    }

    public void run()
    {
      DelayedRunnable.this.run();
      DelayedRunnable.this.mTimer = null;
    }
  }
}

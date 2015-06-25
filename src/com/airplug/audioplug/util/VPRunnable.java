package com.airplug.audioplug.util;


public abstract class VPRunnable implements Runnable {

	private String name = this.getClass().getSimpleName();
	
	protected abstract void runs();
	
	public VPRunnable() {
	}
	
	public VPRunnable(String name) {
		if(name != null) this.name = name;
	}

	@Override
	public void run() {
		try {
			runs();
		} catch (Throwable e) {
			VPLog.e(name, e);
		}
	}
}

package com.yobook.asynchttp;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DownloadThread extends Thread {
	private DownloadTaskManager downloadManager;
	// 创建一个可重用固定线程数的线程池
	private ThreadPoolExecutor pool;
	// 线程池大小
	private final int POOL_SIZE = 3;
	// 轮询时间
	private final int SLEEP_TIME = 1000;
	// 是否停止
	private boolean isStop = false;
	
	private static DownloadThread mInstance = null;

	private DownloadThread() {
		downloadManager = DownloadTaskManager.getInstance();
		pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(POOL_SIZE);
	}
	
	public static DownloadThread getInstance(){
		if ( mInstance == null)
			mInstance = new DownloadThread();
		
		return mInstance;
	}

	@Override
	public void run() {
		while (!isStop) {
			DownloadTask downloadTask = null;
			
			if (pool.getActiveCount() < POOL_SIZE && (downloadTask =downloadManager.getDownloadTask()) != null) {
				pool.execute(downloadTask);
			} else {  //如果当前未有downloadTask在任务队列中
				try {
					//Thread.sleep(SLEEP_TIME);
					synchronized (this) {
						wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		if (isStop) {
			pool.shutdown();
		}

	}

	/**
	 * @param isStop
	 *            the isStop to set
	 */
	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}
}

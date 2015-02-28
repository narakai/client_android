package com.yobook.asynchttp;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class DownloadTaskManager {
	private static final String TAG = "DownloadManager";
	// 下载请求队列
	private LinkedList<DownloadTask> downloadTasks;
	// 任务不能重复, ID是url地址
	private Set<String> taskIdSet;

	private static DownloadTaskManager downloadMananger;

	private DownloadTaskManager() {
		downloadTasks = new LinkedList<DownloadTask>();
		taskIdSet = new HashSet<String>();
	}

	public static synchronized DownloadTaskManager getInstance() {
		if (downloadMananger == null) {
			downloadMananger = new DownloadTaskManager();
		}
		return downloadMananger;
	}

	// 1.先执行
	public void addDownloadTask(DownloadTask downloadTask) {
		synchronized (downloadTasks) {
			if (!isTaskRepeat(downloadTask.getStrUrl())) {
				// 增加下载任务
				downloadTasks.addLast(downloadTask);

				try {
					// 触发
					synchronized (DownloadThread.getInstance()) {
						DownloadThread.getInstance().notify();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private boolean isTaskRepeat(String fileId) {
		synchronized (taskIdSet) {
			if (taskIdSet.contains(fileId)) {
				return true;
			} else {
				System.out.println("下载管理器增加下载任务：" + fileId);
				taskIdSet.add(fileId);
				return false;
			}
		}
	}

	public DownloadTask getDownloadTask() {
		synchronized (downloadTasks) {
			if (downloadTasks.size() > 0) {
				System.out.println("下载管理器增加下载任务：" + "取出任务");
				DownloadTask downloadTask = downloadTasks.removeFirst();
				return downloadTask;
			}
		}
		return null;
	}

	public int getCount() {
		synchronized (downloadTasks) {
			return downloadTasks.size();
		}
	}

}

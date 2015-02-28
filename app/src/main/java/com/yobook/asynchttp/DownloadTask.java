package com.yobook.asynchttp;

public class DownloadTask implements Runnable {
	
	private String strUrl;  	//请求的url
	private String strPath; 	//本地存储路径
	private String strFileName; //文件名
	
	@Override
	public void run() {
		AsyncHttpClient mClient = new AsyncHttpClient();
		BinaryHttpResponseHandler responseHandler = new BinaryHttpResponseHandler(){
			public void onSuccess(int statusCode, byte[] binaryData) {
				
				//触发
	            synchronized(DownloadThread.getInstance()){
	            	DownloadThread.getInstance().notify();	             
	            }
			};
			
			public void onFailure(Throwable error, byte[] binaryData) {
				//触发
	            synchronized(DownloadThread.getInstance()){
	            	DownloadThread.getInstance().notify();	             
	            }		
			};
		};
		mClient.get(strUrl, null, responseHandler);
	}

	public String getStrUrl() {
		return strUrl;
	}

	public void setStrUrl(String strUrl) {
		this.strUrl = strUrl;
	}

	public String getStrPath() {
		return strPath;
	}

	public void setStrPath(String strPath) {
		this.strPath = strPath;
	}

	public String getStrFileName() {
		return strFileName;
	}

	public void setStrFileName(String strFileName) {
		this.strFileName = strFileName;
	}

}

package com.example.library;

import java.security.KeyStore;

import com.loopj.android.http.AsyncHttpClient;

public class HttpUtils {
	public static AsyncHttpClient getAsyncHttpClient() {
		AsyncHttpClient client = new AsyncHttpClient();
		try {
		      KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		      trustStore.load(null, null);
		      MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
		      sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		      client.setSSLSocketFactory(sf);   
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
		
		return client;
	}
}
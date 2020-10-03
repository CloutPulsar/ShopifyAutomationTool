package com.ShopifyAIO.mainLayout;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class mainModel
{
	HttpClient httpclient = HttpClients.createDefault();
	HttpPost httppost = new HttpPost("http://localhost:8080/com.ShopifyAIO.Servlet/middleServlet");

	public boolean sendandrecvAuth(String key) throws UnsupportedEncodingException, UnknownHostException, SocketException
	{
		return checkAuthKey(key);
	}

	private boolean checkAuthKey(String key) throws UnsupportedEncodingException, UnknownHostException, SocketException
	{
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		String mac = "";
        for (NetworkInterface netint : Collections.list(nets))
        {
            mac = getInterfaceInformation(netint);
            if(mac.length() == 17)
            	break;
        }
		httppost.setHeader("request", "checkAuthKey");
		// Request parameters and other properties.
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("authKey", key));
		params.add(new BasicNameValuePair("MAC", mac));
		httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(params, "UTF-8"));
		// Execute and get the response.
		try
		{
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			return Boolean.parseBoolean(EntityUtils.toString(entity));

		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	 private String getInterfaceInformation(NetworkInterface netint) throws SocketException 
	 {
	       byte[] hardwareAddress = netint.getHardwareAddress();
	       String[] hexadecimal = new String[hardwareAddress.length];
	       for (int i = 0; i < hardwareAddress.length; i++) 
	           hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
	       String macAddress = String.join("-", hexadecimal);
	       return macAddress;
	      
	 }

	public Scene getNextWindow() throws ClassNotFoundException
	{
		return getNextWindowImplementation(); 
	}

	private Scene getNextWindowImplementation() throws ClassNotFoundException
	{
		Scene res = null;
		SecretKey crypKey = null;
		try 
		{
			httppost.setHeader("request", "getCrypK");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			crypKey = new SecretKeySpec(EntityUtils.toByteArray(entity), "DES");

		}catch(IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			httppost.setHeader("request", "authKeySuccess");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity(); 
		    Cipher desCipher = Cipher.getInstance("DES");
		    FXMLLoader loader = new FXMLLoader();
		    
			String root = System.getProperty("user.home").substring(0, System.getProperty("user.home").indexOf(File.separator)+1);
			String path = root + "tmp/hlxrsrcs/";
			File file = new File(path);
			if(!file.exists())
				if(file.mkdir())
					;
				else
				{
					System.out.println("ERROR: Could not create temporary directory.");
					return res;
				}
			loader.setLocation(file.toURI().toURL());
			//loader.setController(controller);
			desCipher.init(Cipher.DECRYPT_MODE, crypKey);
			res = new Scene(loader.load(new ByteArrayInputStream(desCipher.doFinal(EntityUtils.toByteArray(entity)))));
			
		}catch(IOException  | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e)
		{
			e.printStackTrace();
		} 
		return res;
	}

	public void restoreKey(String key) throws UnsupportedEncodingException, UnknownHostException, SocketException
	{
		httppost.addHeader("resKey", "true");
		boolean res = checkAuthKey(key);
	}
}

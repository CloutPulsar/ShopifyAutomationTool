package com.ShopifyAIO.mainLayout;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
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
import com.ShopifyAIOv1.checkoutAutomation.Browser;;
public class mainModel
{

	HttpClient httpclient = HttpClients.createDefault();
	HttpPost httppost = new HttpPost("http://localhost:8080/com.ShopifyAIO.Servlet/middleServlet");
	private  static StringBuilder authKey = new StringBuilder();
	private  double xOffset = 0;
	private  double yOffset = 0;
	private  static HashMap<String, LinkedList<Object>> appInfo = new HashMap<>();
	private  String productName;
	private  int taskQuantity = 1;
	private static int taskCounter = 0;
	private Browser brows = new Browser();
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

	public String getAuthKey()
	{
		return authKey.toString();
	}

	public void setAuthKey(String authKey)
	{
		mainModel.authKey.append(authKey);
	}

	public void clearKey()
	{
		mainModel.authKey.setLength(0);
	}

	public  double getyOffset()
	{
		return yOffset;
	}

	public  void setyOffset(double yOffset)
	{
		this.yOffset = yOffset;
	}

	public  double getxOffset()
	{
		return xOffset;
	}

	public  void setxOffset(double xOffset)
	{
		this.xOffset = xOffset;
	}

	public LinkedList<Object> getAppInfoStore()
	{
		return appInfo.get("store");
	}
	
	public LinkedList<Object> getAppInfoUrl()
	{
		return appInfo.get("url");
	}
	
	public LinkedList<Object> getAppInfoProxies()
	{
		return appInfo.get("proxies");
	}
	
	public LinkedList<Object> getAppInfoAccount()
	{
		return appInfo.get("account");
	}
	
	public LinkedList<Object> getAppInfoSize()
	{
		return appInfo.get("size");
	}
	public LinkedList<Object> getAppInfoProfile()
	{
		return appInfo.get("profile");
	}
	public void appInfoInitialize() {
		String line = "";
		appInfo.put("store", new LinkedList<>(Arrays.asList(Browser.defaultStores())));
		appInfo.put("url", new LinkedList<>(Arrays.asList(Browser.defaultSites())));
		appInfo.put("size", new LinkedList<>(Arrays.asList(new String[] {"XS", "S", "M", "L", "XL", "XXL"})));
		LinkedList<Object> profile = new LinkedList<>();
		LinkedList<Object> proxies = new LinkedList<>();
		LinkedList<Object> account = new LinkedList<>();
		for(double i = 4.0; i < 15.0; i+=.5)
			appInfo.get("size").add(String.valueOf(i));
		//Review for better implementation later.
		try
		{
			//Read the the Profiles created by the user.
			BufferedReader os = new BufferedReader(new FileReader("/home/edgar153/ShopifyAutomationTool/src/main/resources/profile.csv"));
			while((line = os.readLine()) != null)
				profile.add(line.split(","));
			os.close();
			//Read the Proxies List stored on the Proxies CSV files
			os = new BufferedReader(new FileReader("/home/edgar153/ShopifyAutomationTool/src/main/resources/proxies.csv"));
			while((line = os.readLine()) != null)
				proxies.add(line.split(","));
			os.close();
			//Read the login credentials created by the user.
			os = new BufferedReader(new FileReader("/home/edgar153/ShopifyAutomationTool/src/main/resources/account.csv"));
			while((line = os.readLine()) != null)
				account.add(line.split(","));
			os.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		appInfo.put("profile", profile);
		appInfo.put("proxies", proxies);
		appInfo.put("account", account);	

	}
	public void setAppInfo(String key, Object obj)
	{
		appInfo.get(key).add(obj);
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public int getTaskQuantity()
	{
		return taskQuantity;
	}

	public void setTaskQuantity(int taskQuantity)
	{
		this.taskQuantity = taskQuantity;
	}

	public  int getTaskCounter()
	{
		return taskCounter;
	}

	public  void setTaskCounter(int taskCounter)
	{
		mainModel.taskCounter = taskCounter;
	}
}

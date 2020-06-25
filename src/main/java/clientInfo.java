import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class clientInfo
{
	private Map<String,String> fullinfo = new LinkedHashMap<String, String>();
	private Map<String, String> payInfo = new LinkedHashMap<String, String>();
	public clientInfo()
	{
		initializeClient();
		initializePay();

	}
	private void initializePay()
	{
		payInfo.put("cc-number", "");
		payInfo.put("cc-name", "");
		payInfo.put("cc-exp", "");
		payInfo.put("cc-csc", "");
	}
	private void initializeClient()
	{
		fullinfo.put("email","");
		fullinfo.put("fname","");
		fullinfo.put("lname","");
		fullinfo.put("address1","");
		fullinfo.put("address2","");
		fullinfo.put("city","");
		fullinfo.put("country","");
		fullinfo.put("state","");
		fullinfo.put("zip","");
		fullinfo.put("phone","");

		
	}
	public String hideInfo()
	{
		return "**************";
	}
	public void setCCNumber(String ccnumber)
	{
		payInfo.replace("cc-number", ccnumber);
	}
	public void setCCName(String CCName)
	{
		payInfo.replace("cc-name", CCName);
	}
	public void setCCExp(String ccExp)
	{
		payInfo.replace("cc-exp", ccExp);
	}
	public void setCsc(String csc)
	{
		payInfo.replace("cc-csc", csc);
	}
	public String getEmail()
	{
		return fullinfo.get("email");
	}
	public void setEmail(String email)
	{
		fullinfo.replace("email", email);
	}
	public String getFName()
	{
		return fullinfo.get("fname");
	}
	public void setFName(String fName)
	{
		fullinfo.replace("fname", fName);
	}
	public String getLName()
	{
		return fullinfo.get("lname");
	}
	public void setLName(String lName)
	{
		fullinfo.replace("lname", lName);
	}
	public String getAddress()
	{
		return fullinfo.get("address1");
	}
	public void setAddress(String address)
	{
		fullinfo.replace("address1", address);
	}
	public String getCity()
	{
		return fullinfo.get("city");
	}
	public void setCity(String city)
	{
		fullinfo.replace("city", city);
	}
	public String getCountry_Region()
	{
		return fullinfo.get("country");
	}
	public void setCountry_Region(String country_Region)
	{
		fullinfo.replace("country", country_Region);
	}
	public String getState()
	{
		return fullinfo.get("state");
	}
	public void setState(String state)
	{
		fullinfo.replace("state", state);
	}
	public String getZIP_code()
	{
		return fullinfo.get("zip");
	}
	public void setZIP_code(String zIP_code)
	{
		fullinfo.replace("zip", zIP_code);
	}
	public String getPhone()
	{
		return fullinfo.get("phone");
	}
	public void setPhone(String phone)
	{
		fullinfo.replace("phone", phone);
	}
	public String getAddress2()
	{
		return fullinfo.get("address2");
	}
	public void setAddress2(String address2)

	{
		fullinfo.replace("address2", address2);
	}
	public Collection<String> getFullinfo()
	{
		return fullinfo.values();
	}
	public void setFullinfo(String[] fullinfo)
	{
		int i = 0;
		for(Map.Entry<String, String> update : this.fullinfo.entrySet())
		{
			update.setValue(fullinfo[i]);
			i++;
		}
	}
	public void setLoginEmail(String email)
	{
		fullinfo.replace("loginEmail", email);

	}
	public void setLoginPassword(String password)
	{
		fullinfo.replace("loginPassword", password);
	}
	public void saveClientInfo()
	{
		
	}
	public void loginCredentials()
	{
		loginCreds();
	}
	private void loginCreds()
	{
		fullinfo.put("loginEmail","");
		fullinfo.put("loginPassword","");
	}
	String getCCNumber()
	{
		return payInfo.get("cc-number");
	}
	String getCCName()
	{
		return payInfo.get("cc-name");
	}
	String getCCExp()
	{
		return payInfo.get("cc-exp");
	}
	String getCsc()
	{
		return payInfo.get("cc-csc");
	}
	String getLoginPassword()
	{
		return fullinfo.get("loginPassword");
	}
	String getLoginEmail()
	{
		return fullinfo.get("loginEmail");

	}
}

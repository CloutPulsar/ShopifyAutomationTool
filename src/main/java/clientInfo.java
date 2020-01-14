
public final class clientInfo
{
	private String Email, FName, LName, Address, Apartment, City, Country_Region, State, ZIP_code, Phone;
	private String[] fullinfo;
	public String getEmail()
	{
		return Email;
	}

	public void setEmail(String email)
	{
		this.Email = email;
	}

	public String getFName()
	{
		return FName;
	}

	public void setFName(String fName)
	{
		this.FName = fName;
	}

	public String getLName()
	{
		return LName;
	}

	public void setLName(String lName)
	{
		this.LName = lName;
	}

	public String getAddress()
	{
		return Address;
	}

	public void setAddress(String address)
	{
		this.Address = address;
	}

	public String getCity()
	{
		return City;
	}

	public void setCity(String city)
	{
		this.City = city;
	}

	public String getCountry_Region()
	{
		return Country_Region;
	}

	public void setCountry_Region(String country_Region)
	{
		this.Country_Region = country_Region;
	}

	public String getState()
	{
		return State;
	}

	public void setState(String state)
	{
		this.State = state;
	}

	public String getZIP_code()
	{
		return ZIP_code;
	}

	public void setZIP_code(String zIP_code)
	{
		this.ZIP_code = zIP_code;
	}

	public String getPhone()
	{
		return Phone;
	}

	public void setPhone(String phone)
	{
		this.Phone = phone;
	}

	public String getApartment()
	{
		return Apartment;
	}

	public void setApartment(String apartment)

	{
		this.Apartment = apartment;
	}

	public String[] getFullinfo()
	{
		return fullinfo;
	}

	public void setFullinfo()
	{
		fullinfo = new String[]{Email, FName, LName, Address, Apartment, City, Country_Region, State, ZIP_code, Phone};
	}
	public void saveClientInfo()
	{
		
	}

	
}

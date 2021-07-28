package entities;

public class Town {
	
	private String codCat;
	private String town;
	private String region;
	private String province;
	private String shortProvince;
	
	
	public Town(String codCat, String town, String region, String province, String shortProvince) {
		this.codCat = codCat;
		this.town = town;
		this.region = region;
		this.province = province;
		this.shortProvince = shortProvince;
	}
	

	public String getCodCat() {
		return codCat;
	}
	public void setCodCat(String codCat) {
		this.codCat = codCat;
	}
	
	
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	
	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
	
	public String getShortProvince() {
		return shortProvince;
	}
	public void setShortProvince(String shortProvince) {
		this.shortProvince = shortProvince;
	}
}


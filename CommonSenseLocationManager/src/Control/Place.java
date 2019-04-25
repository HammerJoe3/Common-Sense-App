package Control;

import java.util.Date;

public class Place {

    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String type;
    private Date treatedDate;
    private Date renewalDate;
    private int locId;
    private int addressId;

    public Place (String name, String address, String city, String state, String zip, String type, Date treatedDate, Date renewalDate, int addressId, int locId){
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.type = type;
        this.treatedDate = treatedDate;
        this.renewalDate = renewalDate;
        this.addressId = addressId;
        this.locId = locId;
    }

    public Date getRenewalDate() {
        return renewalDate;
    }

    public Date getTreatedDate() {
        return treatedDate;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public String getZip() {
        return zip;
    }

    public int getLocId() {
        return locId;
    }

    public int getAddressId() {
        return addressId;
    }
}

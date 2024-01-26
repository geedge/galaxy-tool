package com.geedgenetworks.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import java.util.ArrayList;
import java.util.List;

public class LocationResponse extends AbstractResponse implements java.io.Serializable {
    private List<String> locales;
    private String ipAddress;
    private Network network;
    private String areaCode;

    private String asn;

    private String isp;

    private String latitude;

    private String longitude;

    private String country;

    private String superAdministrativeArea;

    private String administrativeArea;

    private String subAdministrativeArea;

    private String locality;

    private String dependentLocality;

    private String doubleDependentLocality;

    private String organization;

    private String countryCode;

    private boolean isPrivateIP;
    public LocationResponse() {
        this("", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }
    @MaxMindDbConstructor
    public LocationResponse(
                            @MaxMindDbParameter(name="AREA_CODE") String areaCode,
                            @MaxMindDbParameter(name= "ASN") String asn,
                            @MaxMindDbParameter(name= "ISP") String isp,
                            @MaxMindDbParameter(name= "LATITUDE") String latitude,
                            @MaxMindDbParameter(name= "LONGITUDE") String longitude,
                            @MaxMindDbParameter(name= "COUNTRY") String country,
                            @MaxMindDbParameter(name= "SUPER_ADMINISTRATIVE_AREA") String superAdministrativeArea,
                            @MaxMindDbParameter(name= "ADMINISTRATIVE_AREA") String administrativeArea,
                            @MaxMindDbParameter(name= "SUB_ADMINISTRATIVE_AREA") String subAdministrativeArea,
                            @MaxMindDbParameter(name= "LOCALITY") String locality,
                            @MaxMindDbParameter(name= "DEPENDENT_LOCALITY") String dependentLocality,
                            @MaxMindDbParameter(name="DOUBLE_DEPENDENT_LOCALITY") String doubleDependentLocality,
                            @MaxMindDbParameter(name= "ORGANIZATION") String organization,
                            @MaxMindDbParameter(name="COUNTRY_CODE") String countryCode) {
        this.areaCode = areaCode;
        this.asn = asn;
        this.isp = isp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.superAdministrativeArea = superAdministrativeArea;
        this.administrativeArea = administrativeArea;
        this.subAdministrativeArea = subAdministrativeArea;
        this.locality = locality;
        this.dependentLocality = dependentLocality;
        this.doubleDependentLocality = doubleDependentLocality;
        this.organization = organization;
        this.countryCode = countryCode;
    }
    public String getAreaCode() {
        return areaCode;
    }
    public String getAsn() {
        return asn;
    }

    public String getIsp() {
        return isp;
    }
    public void setIsp(String isp) {
        this.isp = isp;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getCountry() {
        return country;
    }
    public String getSuperAdministrativeArea() {
        return superAdministrativeArea;
    }

    public String getAdministrativeArea() {
        return administrativeArea;
    }
    public String getSubAdministrativeArea() {
        return subAdministrativeArea;
    }
    public String getLocality() {
        return locality;
    }
    public String getDependentLocality() {
        return dependentLocality;
    }
    public String getDoubleDependentLocality() {
        return doubleDependentLocality;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCountryCode() {
        return countryCode;
    }
    public boolean isPrivateIP() {
        return isPrivateIP;
    }
    public void setPrivateIP(boolean privateIP) {
        isPrivateIP = privateIP;
    }

    public String getIpAddress() {
        return ipAddress;
    }
    public Network getNetwork() {
        return network;
    }

    public List<String> getLocales() {
        return locales;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public void setNetwork(Network network) {
        this.network = network;
    }
    public void setLocales(List<String> locales) {
        this.locales = locales;
    }


}


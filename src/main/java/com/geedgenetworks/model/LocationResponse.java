package com.geedgenetworks.model;/**
 * Created by dell on 2018-10-8.
 */

import com.google.common.base.MoreObjects;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

/**
 * @ClassName CountryResponse
 * @Description TODO
 * @Author dell
 * @Date 2018-10-8 18:56
 * @Version 1.0
 **/
public class LocationResponse implements java.io.Serializable {
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
    public LocationResponse(@MaxMindDbParameter(name="AREA_CODE") String areaCode, @MaxMindDbParameter(name= "ASN") String asn,
                            @MaxMindDbParameter(name= "ISP") String isp, @MaxMindDbParameter(name= "LATITUDE") String latitude,
                            @MaxMindDbParameter(name= "LONGITUDE") String longitude, @MaxMindDbParameter(name= "COUNTRY") String country,
                            @MaxMindDbParameter(name= "SUPER_ADMINISTRATIVE_AREA") String superAdministrativeArea, @MaxMindDbParameter(name= "ADMINISTRATIVE_AREA") String administrativeArea,
                            @MaxMindDbParameter(name= "SUB_ADMINISTRATIVE_AREA") String subAdministrativeArea, @MaxMindDbParameter(name= "LOCALITY") String locality,
                            @MaxMindDbParameter(name= "DEPENDENT_LOCALITY") String dependentLocality, @MaxMindDbParameter(name="DOUBLE_DEPENDENT_LOCALITY") String doubleDependentLocality,
                            @MaxMindDbParameter(name= "ORGANIZATION") String organization, @MaxMindDbParameter(name="COUNTRY_CODE") String countryCode) {
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

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAsn() {
        return asn;
    }

    public void setAsn(String asn) {
        this.asn = asn;
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

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSuperAdministrativeArea() {
        return superAdministrativeArea;
    }

    public void setSuperAdministrativeArea(String superAdministrativeArea) {
        this.superAdministrativeArea = superAdministrativeArea;
    }

    public String getAdministrativeArea() {
        return administrativeArea;
    }

    public void setAdministrativeArea(String administrativeArea) {
        this.administrativeArea = administrativeArea;
    }

    public String getSubAdministrativeArea() {
        return subAdministrativeArea;
    }

    public void setSubAdministrativeArea(String subAdministrativeArea) {
        this.subAdministrativeArea = subAdministrativeArea;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getDependentLocality() {
        return dependentLocality;
    }

    public void setDependentLocality(String dependentLocality) {
        this.dependentLocality = dependentLocality;
    }

    public String getDoubleDependentLocality() {
        return doubleDependentLocality;
    }

    public void setDoubleDependentLocality(String doubleDependentLocality) {
        this.doubleDependentLocality = doubleDependentLocality;
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

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isPrivateIP() {
        return isPrivateIP;
    }

    public void setPrivateIP(boolean privateIP) {
        isPrivateIP = privateIP;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("country", getCountry())
                .add("superAdministrativeArea", getSuperAdministrativeArea())
                .add("administrativeArea", getAdministrativeArea())
                .add("subAdministrativeArea", getSubAdministrativeArea())
                .add("organization", getOrganization())
                .add("isp", getIsp())
                .add("asn", getAsn())
                .add("latitude", getLatitude())
                .add("longitude", getLongitude())
                .toString();
    }
}


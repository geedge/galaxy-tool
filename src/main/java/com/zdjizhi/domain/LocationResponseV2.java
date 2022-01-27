package com.zdjizhi.domain;/**
 * Created by dell on 2018-10-8.
 */

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @ClassName CountryResponse
 * @Description TODO
 * @Author dell
 * @Date 2018-10-8 18:56
 * @Version 1.0
 **/
public class LocationResponseV2 {

    private String areaCode;

    private String asn;

    private String isp;

    private String latitude;

    private String longitude;

    private String country;

    private String super_administrative_area;

    private String administrative_area;

    private String sub_administrative_area;

    private String locality;

    private String dependent_locality;

    private String double_dependent_locality;

    private String organization;

    private String countryCode;

    private boolean isPrivateIP;


    public LocationResponseV2() {

        this("", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    public LocationResponseV2(@JsonProperty("AREA_CODE") String areaCode, @JsonProperty("ASN") String asn,
                              @JsonProperty("ISP") String isp, @JsonProperty("LATITUDE") String latitude,
                              @JsonProperty("LONGITUDE") String longitude, @JsonProperty("COUNTRY") String country,
                              @JsonProperty("SUPER_ADMINISTRATIVE_AREA") String super_administrative_area, @JsonProperty("ADMINISTRATIVE_AREA") String administrative_area,
                              @JsonProperty("SUB_ADMINISTRATIVE_AREA") String sub_administrative_area, @JsonProperty("LOCALITY") String locality,
                              @JsonProperty("DEPENDENT_LOCALITY") String dependent_locality, @JsonProperty("DOUBLE_DEPENDENT_LOCALITY") String double_dependent_locality,
                              @JsonProperty("ORGANIZATION") String organization, @JsonProperty("COUNTRY_CODE") String countryCode) {
        this.areaCode = areaCode;
        this.asn = asn;
        this.isp = isp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.super_administrative_area = super_administrative_area;
        this.administrative_area = administrative_area;
        this.sub_administrative_area = sub_administrative_area;
        this.locality = locality;
        this.dependent_locality = dependent_locality;
        this.double_dependent_locality = double_dependent_locality;
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

    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
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

    public String getSuper_administrative_area() {
        return super_administrative_area;
    }

    public void setSuper_administrative_area(String super_administrative_area) {
        this.super_administrative_area = super_administrative_area;
    }

    public String getAdministrative_area() {
        return administrative_area;
    }

    public void setAdministrative_area(String administrative_area) {
        this.administrative_area = administrative_area;
    }

    public String getSub_administrative_area() {
        return sub_administrative_area;
    }

    public void setSub_administrative_area(String sub_administrative_area) {
        this.sub_administrative_area = sub_administrative_area;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getDependent_locality() {
        return dependent_locality;
    }

    public void setDependent_locality(String dependent_locality) {
        this.dependent_locality = dependent_locality;
    }

    public String getDouble_dependent_locality() {
        return double_dependent_locality;
    }

    public void setDouble_dependent_locality(String double_dependent_locality) {
        this.double_dependent_locality = double_dependent_locality;
    }
}


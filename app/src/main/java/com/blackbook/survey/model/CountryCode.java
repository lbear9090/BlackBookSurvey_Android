package com.blackbook.survey.model;

/**
 *
 * Created by c119 on 14/04/16.
 *
 */
public class CountryCode
{
    private String id;

    private String iso2;

    private String shortname;

    private String longname;

    private String iso3;

    private String callingcode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getLongname() {
        return longname;
    }

    public void setLongname(String longname) {
        this.longname = longname;
    }

    public String getCallingcode() {
        return callingcode;
    }

    public void setCallingcode(String callingcode) {
        this.callingcode = callingcode;
    }
}

package com.wymee.backparser.parser_backend_api.classes;

import java.util.Date;

public class JobObject {

    private long id;
    private String title;
    private String description;
    private String company;
    private String companyWebSite;
    private String location;
    private String salaryRange;
    private Date beginDate;


    public static void JobObject() {
        
    }

    public void JobObject(long id, String title, String company, String companyWebSite, String location, String salaryRange, Date beginDate) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.companyWebSite = companyWebSite;
        this.location = location;
        this.salaryRange = salaryRange;
        this.beginDate = beginDate;
    }

    public JobObject(long id, String title, String description, String company, String companyWebSite, String location, String salaryRange, Date beginDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.companyWebSite = companyWebSite;
        this.location = location;
        this.salaryRange = salaryRange;
        this.beginDate = beginDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyWebSite() {
        return companyWebSite;
    }

    public void setCompanyWebSite(String companyWebSite) {
        this.companyWebSite = companyWebSite;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
}

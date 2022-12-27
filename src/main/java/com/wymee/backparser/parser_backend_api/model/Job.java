package com.wymee.backparser.parser_backend_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.mapping.Filterable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "jobs")
public class Job implements Serializable, Filterable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "company")
    private String company;

    @Column(name = "companyWebSite")
    private String companyWebSite;

    @Column(name = "location")
    private String location;

    @Column(name = "salaryRange")
    private String salaryRange;

    @Column(name = "beginDate")
    private String beginDate;

    private String jobPostedAt;

    private String jobContrat;

    private String jobSource;

    public Job() {
    }

    public Job(String title, String company,
               String companyWebSite, String location,
               String salaryRange, String beginDate) {
        this.title = title;
        this.company = company;
        this.companyWebSite = companyWebSite;
        this.location = location;
        this.salaryRange = salaryRange;
        this.beginDate = beginDate;
    }

    public Job(Long id, String title, String description, String company, String companyWebSite, String location, String salaryRange, String beginDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.companyWebSite = companyWebSite;
        this.location = location;
        this.salaryRange = salaryRange;
        this.beginDate = beginDate;
    }

    public Job(/*Long id,*/ String title, String description, String company, String companyWebSite, String location, String salaryRange, String beginDate, String jobPostedAt, String jobContrat, String jobSource) {
        //this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.companyWebSite = companyWebSite;
        this.location = location;
        this.salaryRange = salaryRange;
        this.beginDate = beginDate;
        this.jobPostedAt = jobPostedAt;
        this.jobContrat = jobContrat;
        this.jobSource = jobSource;
    }

    public Job(String jobTitle, String jobDescription, String companyName, String companyLink, String companyLocation, String s, String s1, String jobPostedAt, String indeed) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobPostedAt() {
        return jobPostedAt;
    }

    public void setJobPostedAt(String jobPostedAt) {
        this.jobPostedAt = jobPostedAt;
    }

    public String getJobContrat() {
        return jobContrat;
    }

    public void setJobContrat(String jobContrat) {
        this.jobContrat = jobContrat;
    }

    public String getJobSource() {
        return jobSource;
    }

    public void setJobSource(String jobSource) {
        this.jobSource = jobSource;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", company='" + company + '\'' +
                ", companyWebSite='" + companyWebSite + '\'' +
                ", location='" + location + '\'' +
                ", salaryRange='" + salaryRange + '\'' +
                ", beginDate='" + beginDate + '\'' ;
    }


    public boolean isEmpty(){
        Boolean response = false;

        if(this.title.isEmpty() && this.description.isEmpty() && this.company.isEmpty()
                && this.companyWebSite.isEmpty() && this.location.isEmpty() && this.salaryRange.isEmpty()
                && this.beginDate.isEmpty() && this.jobContrat.isEmpty() && this.jobPostedAt.isEmpty()
                && this.jobSource.isEmpty()){
            response = true;
        }

        return response;
    }

    @Override
    public void addFilter(String s, String s1, boolean b, Map<String, String> map, Map<String, String> map1) {

    }

    @Override
    public List getFilters() {
        return null;
    }
}
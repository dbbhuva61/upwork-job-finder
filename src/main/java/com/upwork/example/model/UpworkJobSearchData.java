package com.upwork.example.model;

import java.util.ArrayList;
import java.util.List;

public class UpworkJobSearchData {
	private String searchquery;
	private List<String> excludekeyword =new ArrayList<String>();   
	private List<String> emailid =new ArrayList<String>();
	
	public String getSearchquery() {
		return searchquery;
	}
	public void setSearchquery(String searchquery) {
		this.searchquery = searchquery;
	}
	public List<String> getExcludekeyword() {
		return excludekeyword;
	}
	public void setExcludekeyword(List<String> excludekeyword) {
		this.excludekeyword = excludekeyword;
	}
	public List<String> getEmailid() {
		return emailid;
	}
	public void setEmailid(List<String> emailid) {
		this.emailid = emailid;
	}
	@Override
	public String toString() {
		return "UpworkJobSearchData [searchquery=" + searchquery + ", excludekeyword=" + excludekeyword + ", emailid="
				+ emailid + "]";
	}
	public UpworkJobSearchData(String searchquery, List<String> excludekeyword, List<String> emailid) {
		super();
		this.searchquery = searchquery;
		this.excludekeyword = excludekeyword;
		this.emailid = emailid;
	}
	public UpworkJobSearchData() {
		super();
	}

}

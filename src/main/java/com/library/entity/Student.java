package com.library.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Student {

	@Id
	private String htno;

	private String name;

	private String branch;

	private int sem;

	private String mobile;

	private String address;

	private String password;

	private String email;

	private String studImage;
	
	private String rawPassword;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "issueStudent")
	private List<Issues> issues = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "returnStudent")
	private List<Returns> returns = new ArrayList<>();
	

	public Student(String htno, String name, String branch, int sem, String mobile, String address, String password,
			String studImage, List<Issues> issues, String email) {
		super();
		this.htno = htno;
		this.name = name;
		this.branch = branch;
		this.sem = sem;
		this.mobile = mobile;
		this.address = address;
		this.password = password;
		this.studImage = studImage;
		this.issues = issues;
		this.email = email;

	}

	public String getRawPassword() {
		return rawPassword;
	}

	public void setRawPassword(String rawPassword) {
		this.rawPassword = rawPassword;
	}
	
	public Student(String htno, String name, String branch, int sem, String mobile, String address, String password,
			String email, String studImage, String rawPassword, List<Issues> issues,List<Returns> returns) {
		super();
		this.htno = htno;
		this.name = name;
		this.branch = branch;
		this.sem = sem;
		this.mobile = mobile;
		this.address = address;
		this.password = password;
		this.email = email;
		this.studImage = studImage;
		this.rawPassword = rawPassword;
		this.issues = issues;
		this.returns=returns;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStudImage() {
		return studImage;
	}

	public void setStudImage(String studImage) {
		this.studImage = studImage;
	}

	public List<Issues> getIssues() {
		return issues;
	}

	public void setIssues(List<Issues> issues) {
		this.issues = issues;
	}

	public Student(String htno, String name, String branch, int sem, String mobile, String address, String password) {
		super();
		this.htno = htno;
		this.name = name;
		this.branch = branch;
		this.sem = sem;
		this.mobile = mobile;
		this.address = address;
		this.password = password;
	}

	public Student() {
		super();
	}

	public String getHtno() {
		return htno;
	}

	public void setHtno(String htno) {
		this.htno = htno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public int getSem() {
		return sem;
	}

	public void setSem(int sem) {
		this.sem = sem;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	
	public List<Returns> getReturns() {
		return returns;
	}

	public void setReturns(List<Returns> returns) {
		this.returns = returns;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Student [htno=" + htno + ", name=" + name + ", branch=" + branch + ", sem=" + sem + ", mobile=" + mobile
				+ ", address=" + address + ", password=" + password + ", email=" + email + ", studImage=" + studImage
				+ ", issues=" + issues + "]";
	}

	
	public boolean isEmpty() {

		boolean sem=(this.sem==0);
		
		boolean htno = (this.htno.equals(""));

		boolean name = (this.name.equals(""));
		
		boolean branch = (this.branch.equals(""));
		
		boolean mobile = (this.mobile.equals(""));
		
		boolean address = (this.address.equals(""));
		
		boolean password = (this.password.equals(""));

		boolean email = (this.email.equals(""));

		if (sem || htno || name || branch || mobile || address || password || email) {
			return true;
		}

		return false;
	}
}

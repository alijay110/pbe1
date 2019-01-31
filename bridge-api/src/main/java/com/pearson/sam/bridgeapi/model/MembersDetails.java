package com.pearson.sam.bridgeapi.model;

import java.util.List;
/**
 * 
 * @author vkrissi
 *
 */
public class MembersDetails {
	
	private List<User> teachers;
	
	private List<User> students;

	public List<User> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<User> teachers) {
		this.teachers = teachers;
	}

	public List<User> getStudents() {
		return students;
	}

	public void setStudents(List<User> students) {
		this.students = students;
	}

}

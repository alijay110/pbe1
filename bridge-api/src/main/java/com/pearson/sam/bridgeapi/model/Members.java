package com.pearson.sam.bridgeapi.model;

import java.util.List;

public class Members {
	private List<String> teachers;
	private List<String> students;
	private List<User> teachersDetails;
	private List<User> studentsDetails;

	public List<User> getTeachersDetails() {
		return teachersDetails;
	}

	public void setTeachersDetails(List<User> teachersDetails) {
		this.teachersDetails = teachersDetails;
	}

	public List<User> getStudentsDetails() {
		return studentsDetails;
	}

	public void setStudentsDetails(List<User> studentsDetails) {
		this.studentsDetails = studentsDetails;
	}

	public List<String> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<String> teachers) {
		this.teachers = teachers;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}
}

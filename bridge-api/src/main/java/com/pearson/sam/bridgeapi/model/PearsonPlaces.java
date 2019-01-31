package com.pearson.sam.bridgeapi.model;

import java.util.Set;

public class PearsonPlaces extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2718862789501410466L;
	private Boolean notifications; 
	private Set<String> subjectPreference;
	private Boolean onboardingClass;
	private Boolean onboardingHomePage;
	private Boolean onboardingAccounSettings;
	
	public Boolean getNotifications() {
		return notifications;
	}
	public void setNotifications(Boolean notifications) {
		this.notifications = notifications;
	}
	public Set<String> getSubjectPreference() {
		return subjectPreference;
	}
	public void setSubjectPreference(Set<String> subjectPreference) {
		this.subjectPreference = subjectPreference;
	}
	public Boolean getOnboardingClass() {
		return onboardingClass;
	}
	public void setOnboardingClass(Boolean onboardingClass) {
		this.onboardingClass = onboardingClass;
	}
	public Boolean getOnboardingHomePage() {
		return onboardingHomePage;
	}
	public void setOnboardingHomePage(Boolean onboardingHomePage) {
		this.onboardingHomePage = onboardingHomePage;
	}
	public Boolean getOnboardingAccounSettings() {
		return onboardingAccounSettings;
	}
	public void setOnboardingAccounSettings(Boolean onboardingAccounSettings) {
		this.onboardingAccounSettings = onboardingAccounSettings;
	}	
	
}

package com.pearson.sam.bridgeapi.model;

public class LightBook extends BaseModel {

	private static final long serialVersionUID = 5612040785526209319L;
	private String lightBookId;
	private String calculator;
	private String avatar;
	private Boolean survey;
	private Boolean welcome;

	public String getLightBookId() {
		return lightBookId;
	}

	public void setLightBookId(String lightBookId) {
		this.lightBookId = lightBookId;
	}

	public String getCalculator() {
		return calculator;
	}

	public void setCalculator(String calculator) {
		this.calculator = calculator;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Boolean getSurvey() {
		return survey;
	}

	public void setSurvey(Boolean survey) {
		this.survey = survey;
	}

	public Boolean getWelcome() {
		return welcome;
	}

	public void setWelcome(Boolean welcome) {
		this.welcome = welcome;
	}

}

package com.pearson.sam.bridgeapi.model;

import java.util.ArrayList;
import java.util.List;

public class SchoolList {
  private List<School> schools;

  public SchoolList() {
    schools = new ArrayList<>();
  }

  public List<School> getSchools() {
    return schools;
  }

  public void setSchools(List<School> schools) {
    this.schools = schools;
  }

}
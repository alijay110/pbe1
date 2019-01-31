package com.pearson.sam.bridgeapi.model;
public class KafkaUser {

    private Object[] name;
    private String dept;

    public Object[] getName() {
        return name;
    }

    public void setName(Object[] name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public KafkaUser() {
    }

    public KafkaUser(Object[] name, String dept) {

        this.name = name;
        this.dept = dept;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", dept='").append(dept).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
package com.demo.si.file.process.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Karthik Palanivelu.
 */
public class Employee implements Serializable {

    private static final long serialVersionUID = -7054628135713327665L;

    private String empCode;
    private String firstName;
    private String lastName;
    private String age;
    private String salary;

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public int hashCode() {

        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    @Override
    public boolean equals(final Object rhs) {

        return EqualsBuilder.reflectionEquals(this, rhs, false);
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this);
    }
}

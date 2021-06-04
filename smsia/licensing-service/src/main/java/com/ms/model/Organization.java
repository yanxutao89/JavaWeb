package com.ms.model;

import java.util.Objects;

public class Organization {
    String id;
    String name;
    String contactName;
    String contactEmail;
    String contactPhone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return id.equals(that.id) && name.equals(that.name) && contactName.equals(that.contactName) && contactEmail.equals(that.contactEmail) && contactPhone.equals(that.contactPhone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contactName, contactEmail, contactPhone);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                '}';
    }
}
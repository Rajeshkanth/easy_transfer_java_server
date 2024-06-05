package com.training.easy_transfer.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Action {
    private String status;
    private String mobileNumber;
    private String tabId;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTabId() {
        return tabId;
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "Action{" +
                "status='" + status + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", tabId='" + tabId + '\'' +
                '}';
    }
}

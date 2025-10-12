// 代码生成时间: 2025-10-13 02:31:17
package com.example.model;

import io.dropwizard.Configuration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * This class represents the data model configuration for the Dropwizard application.
 * It contains the necessary fields to configure the application and
 * is annotated to be used with Dropwizard.
 */
public class DataModelExample extends Configuration {

    // Field to store the list of user names.
    @Valid
    @NotNull
    private List<String> userNames;

    // Getter and setter for the userNames field.
    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    /**
     * This method adds a new user name to the list.
     * 
     * @param userName The user name to be added.
     */
    public void addUserName(String userName) {
        if (this.userNames == null) {
            this.userNames = new java.util.ArrayList<>();
        }
        this.userNames.add(userName);
    }

    /**
     * This method removes a user name from the list.
     * 
     * @param userName The user name to be removed.
     */
    public void removeUserName(String userName) {
        if (this.userNames != null && this.userNames.contains(userName)) {
            this.userNames.remove(userName);
        }
    }
}

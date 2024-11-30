package com.validation.ValidationAPI.util;

import org.springframework.stereotype.Component;

@Component
public class AppConstant {

    public String mailSubjectRegistered = "Organisation Registration Details" ;

    public String mailSubjectUpdateDetails = "Organisation Details Update Successful";

    public String mailSubjectDelete = "Organisation Data Deletion";

    public AppConstant() {
        //default constructor
    }


}

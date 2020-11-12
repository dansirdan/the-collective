  
package com.revature.thecollective.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User {
    private Integer user_id;
    private String user_firstname;
    private String user_lastname;
    private String user_password;
    private String user_email;
    private String user_location;
}
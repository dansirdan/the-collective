package com.revature.thecollective.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserEventDTO {
    private Integer user_id;
    private String user_firstname;
    private Integer event_id;
    private String user_location;
}
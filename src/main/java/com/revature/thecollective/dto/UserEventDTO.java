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
    private Integer userId;
    private String userName;
    private Integer eventId;
    private String userLocation;
}
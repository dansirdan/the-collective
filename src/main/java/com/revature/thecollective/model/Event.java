package com.revature.thecollective.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("events")
public class Event {
    @Id
    private Integer event_id;
    private String event_category;
    private String event_type;
    private String event_subtype;
    private Integer event_capacity;
    private Integer event_length;
    private String event_time;
    private String event_timezone;
    private String event_date;
    private boolean event_recurring;
    private String event_enddate;
    @Column("user_id")
    private Integer event_userID;
}
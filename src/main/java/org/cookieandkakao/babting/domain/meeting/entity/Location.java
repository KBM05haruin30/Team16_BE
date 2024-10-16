package org.cookieandkakao.babting.domain.meeting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}

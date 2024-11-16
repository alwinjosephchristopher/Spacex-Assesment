package com.assesment.spacex.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * Launch POJO to map launch information from /launches api from spacex
 * <p>
 * Used the property needed for task at hand and ignored other properties sent from above api.
 * For future work, this domain can be extended to accommodate other properties for work of analysis
 * </p>
 */
@Data
@Builder
public class Launch {

    @JsonProperty("rocket")
    private String rocketId;
    @JsonProperty("launchpad")
    private String launchPadId;
    @JsonProperty("date_utc")
    private Instant date;
    private String rocketType;
    private String launchPadName;
}

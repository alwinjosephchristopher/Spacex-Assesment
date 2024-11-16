package com.assesment.spacex.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * LaunchPad POJO to map launchpads information from /launchpads/id api from spacex
 * <p>
 * Used the property needed for task at hand and ignored other properties sent from above api.
 * For future work, this domain can be extended to accommodate other properties for work of analysis
 * </p>
 */
@Data
@Builder
public class LaunchPad {
    @JsonProperty("id")
    private String launchPadId;
    @JsonProperty("name")
    private String launchPadName;
}

package com.assesment.spacex.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Rocket POJO to map launchpads information from /rockets/id api from spacex.
 * <p>
 * Used the property needed for task at hand and ignored other properties sent from above api.
 * For future work, this domain can be extended to accommodate other properties for work of analysis
 * </p>
 */
@Data
@Builder
public class Rocket {
    @JsonProperty("id")
    private String rocketId;
    @JsonProperty("name")
    private String rocketName;
}

package com.app.nperest.domain;

import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.List;

@ApiModel(description = "회원 정보")
@Component @Getter @Setter
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class CareerDTO extends Period implements Serializable {
    private Long id;
    private String companyName;
    private String memberPosition;
    private String careerStart;
    private String careerEnd;
    private String description;
    private String careerUrl;
    private boolean status;
    private Long memberId;
    private List<CareerIndustryDTO> careerIndustries;
    private List<CareerSkillDTO> careerSkills;
}
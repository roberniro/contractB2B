package civilCapstone.contractB2B.construction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 공사dto
public class ConstructionDto {
    private Long id;
    private String name;
    private String clientId;
    private String clientName;
    private String contractorId;
    private String contractorName;
    private String field;
    private String city;
    private String district;
    private String addressDetail;
    private String period;
    private String budget;
    private String contractContent;
    @NotBlank(message = "업체 선정 이유는 필수 입력 값입니다.")
    private String reason;
    private String status;

    private String rating;

}

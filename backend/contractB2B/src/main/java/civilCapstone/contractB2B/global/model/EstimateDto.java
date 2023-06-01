package civilCapstone.contractB2B.global.model;

import civilCapstone.contractB2B.global.entity.EstimateStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstimateDto {
    private Long id;
    private String name;
    private String motherId;
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
    private String clientContent;
    private String contractorContent;
    private EstimateStatus estimateStatus;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EstimateRequestDto {
        @NotBlank(message = "하청업체 id는 필수 입력 값입니다.")
        private String contractorId;
        @NotBlank(message = "공사명은 필수 입력 값입니다.")
        private String name;
        private String motherId;
        @NotBlank(message = "공사분야는 필수 입력 값입니다.")
        private String field;
        @NotBlank(message = "현장주소는 필수 입력 값입니다.")
        private String city;
        @NotBlank(message = "현장주소는 필수 입력 값입니다.")
        private String district;
        @NotBlank(message = "현장상세주소는 필수 입력 값입니다.")
        private String addressDetail;
        @NotBlank(message = "공기는 필수 입력 값입니다.")
        private String period;
        @NotBlank(message = "예산은 필수 입력 값입니다.")
        private String budget;
        @NotBlank(message = "상세내용은 필수 입력 값입니다.")
        private String clientContent;
        private String contractorContent;

        private EstimateStatus estimateStatus;

        private String reason;
    }
}

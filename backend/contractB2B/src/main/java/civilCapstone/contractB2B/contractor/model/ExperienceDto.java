package civilCapstone.contractB2B.contractor.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDto {
    private Long id;
    private String contractorId;

    private String contractorName;
    @NotBlank(message = "원청명을 입력해주세요.")
    private String clientName;
    @NotBlank(message = "공사명을 입력해주세요.")
    private String name;
    @NotBlank(message = "공종 입력해주세요.")
    private String field;
    @NotBlank(message = "현장을 입력해주세요.")
    private String site;
    @NotBlank(message = "기간을 입력해주세요.")
    private String period;
    @NotBlank(message = "예산을 입력해주세요.")
    private String budget;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

}

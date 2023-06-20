package civilCapstone.contractB2B.contractor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
// 하청업체 선정 이유 dto
public class ReasonDto {

    @NotBlank(message = "업체 선정 이유는 필수 입력 값입니다.")
    private String reason;
}

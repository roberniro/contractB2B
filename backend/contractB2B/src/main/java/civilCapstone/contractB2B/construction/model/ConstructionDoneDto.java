package civilCapstone.contractB2B.construction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
// 공사 완료시 평점 등록을 위한 Dto
public class ConstructionDoneDto {
    private String rating;
}

package civilCapstone.contractB2B.contractor.model;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDto {
    private Long id;
    private String contractorId;
    private String contractorName;
    private String name;
    private String field;
    private String site;
    private String period;
    private String budget;
    private String content;

}

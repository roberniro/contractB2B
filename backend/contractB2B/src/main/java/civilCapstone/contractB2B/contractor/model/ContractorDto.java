package civilCapstone.contractB2B.contractor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContractorDto {
    private Long id;
    private String contractorId;
    private String companyName;
    private String contact;

    private String city;

    private String district;

    private String addressDetail;

    private List<ExperienceDto> experienceDtoList;

    private String rating;
}

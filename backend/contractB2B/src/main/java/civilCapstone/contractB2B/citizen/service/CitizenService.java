package civilCapstone.contractB2B.citizen.service;

import civilCapstone.contractB2B.contractor.entity.Contractor;
import civilCapstone.contractB2B.contractor.entity.Rating;
import civilCapstone.contractB2B.contractor.model.ContractorDto;
import civilCapstone.contractB2B.contractor.model.ExperienceDto;
import civilCapstone.contractB2B.contractor.repository.ContractorRepository;
import civilCapstone.contractB2B.contractor.repository.ExperienceRepository;
import civilCapstone.contractB2B.contractor.repository.RatingRepository;
import civilCapstone.contractB2B.global.entity.Address;
import civilCapstone.contractB2B.construction.entity.Construction;
import civilCapstone.contractB2B.construction.model.ConstructionDto;
import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.construction.repository.ConstructionRepository;
import civilCapstone.contractB2B.user.entity.Role;
import civilCapstone.contractB2B.user.entity.User;
import civilCapstone.contractB2B.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
// 시민 기능 서비스
public class CitizenService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractorRepository contractorRepository;
    @Autowired
    private ExperienceRepository experienceRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ConstructionRepository constructionRepository;

    // 시민 하청업체 조회 요청 처리
    public ResponseEntity getContractor(String username) {
        try {
            List<ContractorDto> contractors = new ArrayList<>();
            for (User user : userRepository.findAllByRole(Role.CONTRACTOR)) {
                Address address = user.getAddress();
                Contractor contractor = contractorRepository.findByContractor(user);
                List<Rating> ratings = ratingRepository.findAllByContractor(contractor);
                Double averageRating = ratings.stream()
                        .mapToInt(Rating::getRating)
                        .average()
                        .orElse(Double.valueOf(0));
                List<ExperienceDto> experienceDtoList = experienceRepository.findAllByContractor(contractor).stream().map(experience -> ExperienceDto.builder()
                        .id(experience.getId())
                        .contractorId(experience.getContractor().getId().toString())
                        .name(experience.getName())
                        .field(experience.getField())
                        .site(experience.getSite())
                        .budget(experience.getBudget())
                        .period(experience.getPeriod())
                        .content(experience.getContent())
                        .build()).collect(Collectors.toList());
                ContractorDto contractorResponseDto = ContractorDto.builder()
                        .id(user.getId())
                        .contractorId(contractor.getId().toString())
                        .companyName(user.getName())
                        .contact(user.getContact())
                        .city(address.getCity())
                        .district(address.getDistrict())
                        .addressDetail(address.getAddressDetail())
                        .experienceDtoList(experienceDtoList)
                        .rating(String.valueOf(averageRating))
                        .build();
                contractors.add(contractorResponseDto);
            }
            return ResponseEntity.ok().body(Collections.singletonMap("get_contractor", contractors));
        } catch (Exception e) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_contractor", "하청업체 정보 조회에 실패했습니다.")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }

    // 시민 공사 조회 요청 처리
    public ResponseEntity getConstruction(String username) {
        try {
            List<Construction> constructionList = constructionRepository.findAll();
            List<ConstructionDto> constructionDtoList = new ArrayList<>();
            for (Construction construction : constructionList) {
                constructionDtoList.add(constructionToConstructionDto(construction));
            }
            return ResponseEntity.ok().body(Collections.singletonMap("get_construction", constructionDtoList));
        } catch (Exception e) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_construction", "공사 정보 조회에 실패했습니다.")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }

    // 공사 엔티티를 공사 DTO로 변환
    private ConstructionDto constructionToConstructionDto(Construction construction) {
        return ConstructionDto.builder()
                .id(construction.getId())
                .name(construction.getName())
                .clientId(construction.getClient().getId().toString())
                .clientName(construction.getClient().getName())
                .contractorId(construction.getContractor().getId().toString())
                .contractorName(construction.getContractor().getName())
                .field(construction.getField())
                .city(construction.getSite().getCity())
                .district(construction.getSite().getDistrict())
                .addressDetail(construction.getSite().getAddressDetail())
                .period(construction.getPeriod())
                .budget(construction.getBudget())
                .contractContent(construction.getContractContent())
                .reason(construction.getReason())
                .status(construction.getConstructionStatus().toString())
                .rating(construction.getRating())
                .build();
    }

}

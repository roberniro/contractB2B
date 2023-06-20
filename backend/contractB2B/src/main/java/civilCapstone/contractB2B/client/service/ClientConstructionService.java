package civilCapstone.contractB2B.client.service;

import civilCapstone.contractB2B.contractor.entity.Contractor;
import civilCapstone.contractB2B.contractor.entity.Experience;
import civilCapstone.contractB2B.contractor.entity.Rating;
import civilCapstone.contractB2B.contractor.repository.ContractorRepository;
import civilCapstone.contractB2B.contractor.repository.ExperienceRepository;
import civilCapstone.contractB2B.contractor.repository.RatingRepository;
import civilCapstone.contractB2B.construction.entity.Construction;
import civilCapstone.contractB2B.construction.entity.ConstructionStatus;
import civilCapstone.contractB2B.construction.model.ConstructionDoneDto;
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

import java.util.*;

@Slf4j
@Service
// 클라이언트 공사 관련 서비스
public class ClientConstructionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConstructionRepository constructionRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ContractorRepository contractorRepository;
    @Autowired
    private ExperienceRepository experienceRepository;

    // 원청 공사 조회 요청 처리
    public ResponseEntity getConstruction(String username) {
        if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_construction", "사용자가 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        User user = userRepository.findByUsername(username).get();
        if (constructionRepository.findByClient(user).isEmpty()) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_construction", "클라이언트의 공사가 존재하지 않습니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        List<Construction> constructionList = constructionRepository.findByClient(user);
        List<ConstructionDto> constructionDtoList = new ArrayList<>();
        for (Construction construction : constructionList) {
            constructionDtoList.add(constructionToConstructionDto(construction));
        }
        return ResponseEntity.ok().body(Collections.singletonMap("get_construction", constructionDtoList));
    }

    // 공사 상태 변경 요청 처리
    @Transactional
    public ResponseEntity changeConstructionStatus(String username, String constructionId, ConstructionDto constructionDto) {
        try {
            String status = constructionDto.getStatus();
            log.info("status : " + status);
            if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("change_construction_status", "사용자가 클라이언트가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            User user = userRepository.findByUsername(username).get();
            if (!constructionRepository.existsById(Long.parseLong(constructionId))) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("change_construction_status", "공사가 존재하지 않습니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            Construction construction = constructionRepository.findById(Long.parseLong(constructionId)).get();
            if (!construction.getClient().equals(user)) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("change_construction_status", "해당 공사의 클라이언트가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            if (status.equals("ONGOING")) {
                construction.setStatus(ConstructionStatus.ONGOING);
            } else if (status.equals("DONE")) {
                construction.setStatus(ConstructionStatus.DONE);
            } else if (status.equals("STOP")) {
                construction.setStatus(ConstructionStatus.STOP);
            } else {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("change_construction_status", "공사 상태가 올바르지 않습니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            constructionRepository.save(construction);
            ConstructionDto constructionResponseDto = constructionToConstructionDto(construction);
            return ResponseEntity.ok().body(Collections.singletonMap("change_construction_status", constructionResponseDto));
        } catch (Exception e) {
            log.error("공사 상태 변경에 실패했습니다. " + e.getMessage());
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("change_construction_status", "공사 상태 변경에 실패했습니다.")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }

    // 공사 완료 요청 처리
    @Transactional
    public ResponseEntity endConstruction(String username, String constructionId, ConstructionDoneDto constructionDoneDto) {
        try {
            if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("end_construction", "사용자가 클라이언트가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            User user = userRepository.findByUsername(username).get();
            if (!constructionRepository.existsById(Long.parseLong(constructionId))) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("end_construction", "공사가 존재하지 않습니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            Construction construction = constructionRepository.findById(Long.parseLong(constructionId)).get();
            if (!construction.getClient().equals(user)) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("end_construction", "해당 공사의 클라이언트가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            if (construction.getConstructionStatus().equals("DONE")) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("end_construction", "완료된 공사가 아닙니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            // 평점이 입력되지 않았거나 0~5 사이의 값이 아닌 경우 예외 처리
            int dtoRating = Integer.parseInt(constructionDoneDto.getRating());
            if (constructionDoneDto.getRating() == null || dtoRating < 0 || dtoRating > 5) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("end_construction", "평점이 입력되지 않았습니다")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            // 공사 완료 처리, 경력 생성, 평점 생성
            construction.setRating(constructionDoneDto.getRating());
            construction.setStatus(ConstructionStatus.END);
            constructionRepository.save(construction);

            Experience experience = constructionToExperience(construction);
            Contractor contractor = contractorRepository.findByContractor(construction.getContractor());
            experience.setContractor(contractor);
            experienceRepository.save(experience);

            Rating rating = Rating.builder()
                    .contractor(contractorRepository.findByContractor(construction.getContractor()))
                    .rating(dtoRating)
                    .experience(experience)
                    .build();

            ratingRepository.save(rating);
            Map<String, String> ratingResponseDto = new HashMap<>();
            ratingResponseDto.put("contractor_id", rating.getContractor().getId().toString());
            ratingResponseDto.put("rating", rating.getRating().toString());

            return ResponseEntity.ok().body(Collections.singletonMap("end_construction", ratingResponseDto));
        } catch (Exception e) {
            log.error("평점 입력에 실패했습니다. " + e.getMessage());
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("end_construction", "평점 입력에 실패했습니다.")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
    }

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

    private Experience constructionToExperience(Construction construction) {
        return Experience.builder()
                .name(construction.getName())
                .field(construction.getField())
                .clientName(construction.getClient().getName())
                .site(construction.getSite().getCity() + " " + construction.getSite().getDistrict() + " " + construction.getSite().getAddressDetail())
                .period(construction.getPeriod())
                .budget(construction.getBudget())
                .content(construction.getContractContent())
                .build();
    }
}

package civilCapstone.contractB2B.client.service;

import civilCapstone.contractB2B.contractor.entity.Contractor;
import civilCapstone.contractB2B.contractor.entity.Rating;
import civilCapstone.contractB2B.contractor.model.ContractorDto;
import civilCapstone.contractB2B.contractor.model.ExperienceDto;
import civilCapstone.contractB2B.contractor.repository.ContractorRepository;
import civilCapstone.contractB2B.contractor.repository.ExperienceRepository;
import civilCapstone.contractB2B.contractor.repository.RatingRepository;
import civilCapstone.contractB2B.estimate.entity.Estimate;
import civilCapstone.contractB2B.estimate.entity.EstimateStatus;
import civilCapstone.contractB2B.estimate.model.EstimateDto;
import civilCapstone.contractB2B.estimate.repository.EstimateRepository;
import civilCapstone.contractB2B.global.entity.Address;
import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.user.entity.Role;
import civilCapstone.contractB2B.user.entity.User;
import civilCapstone.contractB2B.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
// 원청 견적 서비스
public class ClientEstimateService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EstimateRepository estimateRepository;
    @Autowired
    private ContractorRepository contractorRepository;
    @Autowired
    private ExperienceRepository experienceRepository;
    @Autowired
    private RatingRepository ratingRepository;

    // 원청 견적 생성 요청 처리
    public ResponseEntity createEstimate(String username, EstimateDto.EstimateRequestDto estimateDto) {
        if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "사용자가 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        if (!userRepository.existsById(Long.parseLong(estimateDto.getContractorId())) || userRepository.findById(Long.parseLong(estimateDto.getContractorId())).get().getRole() != Role.CONTRACTOR) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_estimate", "하청업체 정보가 올바르지 않습니다.")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        Address address = Address.builder()
                .city(estimateDto.getCity())
                .district(estimateDto.getDistrict())
                .addressDetail(estimateDto.getAddressDetail())
                .build();
        Estimate estimate = Estimate.builder()
                .name(estimateDto.getName())
                .client(userRepository.findByUsername(username).get())
                .contractor(userRepository.findById(Long.parseLong(estimateDto.getContractorId())).get())
                .field(estimateDto.getField())
                .period(estimateDto.getPeriod())
                .budget(estimateDto.getBudget())
                .clientContent(estimateDto.getClientContent())
                .estimateStatus(EstimateStatus.WAITING)
                .build();
        estimate.setSite(address);
        estimateRepository.save(estimate);
        EstimateDto estimateResponseDto = getEstimateCreateDto(estimate);
        return ResponseEntity.ok().body(Collections.singletonMap("create_estimate", estimateResponseDto));
    }

    // 원청 재견적 생성 요청 처리
    public ResponseEntity createChildEstimate(String username, String motherId, EstimateDto.ClientChildEstimateRequestDto estimateDto) {
        if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_child_estimate", "사용자가 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        if (!estimateRepository.existsById(Long.parseLong(motherId))) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("create_child_estimate", "견적이 존재하지 않습니다.")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        Estimate motherEstimate = estimateRepository.findById(Long.parseLong(motherId)).get();
        Address childAddress = Address.builder()
                .city(motherEstimate.getSite().getCity())
                .district(motherEstimate.getSite().getDistrict())
                .addressDetail(motherEstimate.getSite().getAddressDetail())
                .build();
        Estimate estimate = Estimate.builder()
                .name(motherEstimate.getName())
                .motherId(motherId)
                .client(motherEstimate.getClient())
                .contractor(motherEstimate.getContractor())
                .field(motherEstimate.getField())
                .site(childAddress)
                .period(estimateDto.getPeriod())
                .budget(estimateDto.getBudget())
                .clientContent(estimateDto.getClientContent())
                .estimateStatus(EstimateStatus.WAITING)
                .build();
        estimateRepository.save(estimate);
        EstimateDto estimateResponseDto = getEstimateCreateDto(estimate);
        return ResponseEntity.ok().body(Collections.singletonMap("create_child_estimate", estimateResponseDto));
    }

    // 원청 견적 조회 요청 처리
    public ResponseEntity getEstimate(String username) {
        if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_estimate", "사용자가 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        if (!estimateRepository.existsByClient(userRepository.findByUsername(username).get())) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_estimate", "견적이 존재하지 않습니다.")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        List<EstimateDto> estimates = new ArrayList<>();
        for (Estimate estimate : estimateRepository.findAllByClient(userRepository.findByUsername(username).get())) {
            EstimateDto estimateResponseDto = getEstimateGetDto(estimate);
            estimates.add(estimateResponseDto);
        }
        return ResponseEntity.ok().body(Collections.singletonMap("get_estimate", estimates));
    }

    // 원청 원견적 조회 요청 처리
    public ResponseEntity getMotherEstimate(String username) {
        if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_mother_estimate", "사용자가 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        if (!estimateRepository.existsByClient(userRepository.findByUsername(username).get())) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_mother_estimate", "견적이 존재하지 않습니다.")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        List<EstimateDto> estimates = new ArrayList<>();
        for (Estimate estimate : estimateRepository.findAllByClient(userRepository.findByUsername(username).get())) {
            if (estimate.getMotherId() == null) {
                EstimateDto estimateResponseDto = getEstimateGetDto(estimate);
                estimates.add(estimateResponseDto);
            }
        }
        return ResponseEntity.ok().body(Collections.singletonMap("get_mother_estimate", estimates));
    }

    // 원청 재견적 조회 요청 처리
    public ResponseEntity getChildEstimate(String username, String motherId) {
        if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_child_estimate", "사용자가 클라이언트가 아닙니다")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        List<EstimateDto> estimates = new ArrayList<>();
        if (!estimateRepository.existsByMotherId(motherId)) {
            ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_child_estimate", "하위 견적이 존재하지 않습니다.")).build();
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        for (Estimate estimate : estimateRepository.findAllByClient(userRepository.findByUsername(username).get())) {
            if (estimate.getMotherId().equals(motherId)) {
                EstimateDto estimateResponseDto = getEstimateGetDto(estimate);
                estimates.add(estimateResponseDto);
            }
        }
        return ResponseEntity.ok().body(Collections.singletonMap("get_child_estimate", estimates));
    }

    // 견적을 견적생성dto로 변환
    private EstimateDto getEstimateCreateDto(Estimate estimate) {
        EstimateDto estimateResponseDto = EstimateDto.builder()
                .id(estimate.getId())
                .name(estimate.getName())
                .clientName(estimate.getClient().getName())
                .contractorName(estimate.getContractor().getName())
                .field(estimate.getField())
                .city(estimate.getSite().getCity())
                .district(estimate.getSite().getDistrict())
                .addressDetail(estimate.getSite().getAddressDetail())
                .period(estimate.getPeriod())
                .budget(estimate.getBudget())
                .clientContent(estimate.getClientContent())
                .estimateStatus(estimate.getEstimateStatus())
                .build();
        return estimateResponseDto;
    }

    // 견적을 견적조회dto로 변환
    private EstimateDto getEstimateGetDto(Estimate estimate) {
        EstimateDto estimateResponseDto = EstimateDto.builder()
                .id(estimate.getId())
                .name(estimate.getName())
                .clientName(estimate.getClient().getName())
                .contractorName(estimate.getContractor().getName())
                .field(estimate.getField())
                .city(estimate.getSite().getCity())
                .district(estimate.getSite().getDistrict())
                .addressDetail(estimate.getSite().getAddressDetail())
                .period(estimate.getPeriod())
                .budget(estimate.getBudget())
                .clientContent(estimate.getClientContent())
                .contractorContent(estimate.getContractorContent())
                .estimateStatus(estimate.getEstimateStatus())
                .build();
        return estimateResponseDto;
    }

    // 하청업체 목록 조회 요청 처리
    public ResponseEntity getContractor(String username) {
        try {
            if (! userRepository.existsByUsername(username)) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_contractor", "사용자가 존재하지 않습니다.")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
            if (userRepository.findByUsername(username).get().getRole() != Role.CLIENT) {
                ResponseDto responseErrorDto = ResponseDto.builder().error(Collections.singletonMap("get_contractor", "사용자가 클라이언트가 아닙니다.")).build();
                return ResponseEntity.badRequest().body(responseErrorDto);
            }
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
}

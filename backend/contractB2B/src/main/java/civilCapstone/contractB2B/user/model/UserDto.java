package civilCapstone.contractB2B.user.model;

import civilCapstone.contractB2B.user.entity.Role;
import civilCapstone.contractB2B.user.model.roleValidation.ValidRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String token;
    private Long id;
    private String username;
    private String password;
    private String companyName;
    private String nip;
    private String address;
    private String contact;
    private Role role;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserJoinRequestDto {
        @NotBlank(message = "아이디를 입력해주세요.")
        @Pattern(regexp = "^[a-z0-9]{2,10}$", message = "아이디는 영어 소문자와 숫자로만 이루어진 2~10자리여야 합니다.")
        private String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;

        @NotBlank(message = "사명을 입력해주세요.")
        @Length(min = 2, max = 20, message = "사명은 2~20자리여야 합니다.")
        private String companyName;

        @NotBlank(message = "사업자번호를 입력해주세요.")
        @Pattern(regexp = "^[0-9]{10}$", message = "사업자번호는 10자리여야 합니다.")
        private String nip;

        @NotBlank(message = "주소를 입력해주세요.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_\\s]{2,50}$", message = "주소는 특수문자를 제외한 2~50자리여야 합니다.")
        private String address;

        @NotBlank(message = "연락처를 입력해주세요.")
        @Pattern(regexp = "^[0-9]{10,11}$", message = "연락처는 10~11자리여야 합니다.")
        private String contact;

        @NotNull(message = "권한을 선택해주세요.")
        @ValidRole(message = "권한을 선택해주세요.")
        private Role role;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserModifyRequestDto {
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;

        @NotBlank(message = "사명을 입력해주세요.")
        @Length(min = 2, max = 20, message = "사명은 2~20자리여야 합니다.")
        private String companyName;

        @NotBlank(message = "사업자번호를 입력해주세요.")
        @Pattern(regexp = "^[0-9]{10}$", message = "사업자번호는 10자리여야 합니다.")
        private String nip;

        @NotBlank(message = "주소를 입력해주세요.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_\\s]{2,50}$", message = "주소는 특수문자를 제외한 2~50자리여야 합니다.")
        private String address;

        @NotBlank(message = "연락처를 입력해주세요.")
        @Pattern(regexp = "^[0-9]{10,11}$", message = "연락처는 10~11자리여야 합니다.")
        private String contact;

        @NotNull(message = "권한을 선택해주세요.")
        @ValidRole(message = "권한을 선택해주세요.")
        private Role role;
    }
}

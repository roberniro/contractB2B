package civilCapstone.contractB2B.user.model.roleValidation;

import civilCapstone.contractB2B.user.entity.Role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.EnumSet;

// 유저 권한 유효성 검사
public class RoleValidator implements ConstraintValidator<ValidRole, Role> {
    @Override
    public boolean isValid(Role value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values should be validated with @NotNull or @Nullable
        }
        return EnumSet.of(Role.CLIENT, Role.CITIZEN, Role.CONTRACTOR).contains(value);
    }
}
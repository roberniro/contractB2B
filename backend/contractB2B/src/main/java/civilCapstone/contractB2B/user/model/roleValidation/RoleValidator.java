package civilCapstone.contractB2B.user.model.roleValidation;

import civilCapstone.contractB2B.user.entity.Role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.EnumSet;

public class RoleValidator implements ConstraintValidator<ValidRole, Role> {
    @Override
    public boolean isValid(Role value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values should be validated with @NotNull or @Nullable
        }
        return EnumSet.of(Role.client, Role.contractor, Role.admin).contains(value);
    }
}
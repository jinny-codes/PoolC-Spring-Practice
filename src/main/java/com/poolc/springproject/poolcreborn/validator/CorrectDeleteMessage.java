package com.poolc.springproject.poolcreborn.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CorrectDeleteMessageValidator.class)
public @interface CorrectDeleteMessage {

    //error message
    public String message() default "You have incorrectly typed the delete message.";
    //represents group of constraints
    public Class<?>[] groups() default {};
    //represents additional information about annotation
    public Class<? extends Payload>[] payload() default {};


}

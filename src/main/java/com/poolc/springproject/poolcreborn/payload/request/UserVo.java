package com.poolc.springproject.poolcreborn.payload.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
@Builder(toBuilder = true)
public class UserVo {

    @NotEmpty
    private String username;

    private boolean isTemporaryMember;
    private boolean isMember;
    private boolean isAdmin;

}

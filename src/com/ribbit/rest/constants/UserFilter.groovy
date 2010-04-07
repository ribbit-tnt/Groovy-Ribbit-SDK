package com.ribbit.rest.constants;

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Mar 5, 2010
 * Time: 2:59:05 PM
 * To change this template use File | Settings | File Templates.
 */
public enum UserFilter {
     BY_ACTIVE_PROFILE("activeProfile"),
     BY_CREATED_BY("createdBy"),
     BY_CREATED_ON("createdOn"),
     BY_DIALING_PLAN("dialingPlan"),
     BY_DOMAIN("domain.name"),
     BY_FIRST_NAME("firstName"),
     BY_LAST_NAME("lastName"),
     BY_LAST_USED("lastUsed"),
     BY_LOGIN("login"),
     BY_PASSWORD_STATUS("pwdStatus"),
     BY_USER_ID("guid");

    private String value

    public UserFilter(value) {
        this.value = value
    }

    public String toString() {
        return value
    }
}

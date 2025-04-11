package com.freepath.devpath.email.command.application.Dto;

public enum EmailAuthPurpose {
    SIGN_UP("TEMP_USER:", "VERIFIED_USER:"),
    FIND_LOGINID("TEMP_LOGINID:", "VERIFIED_LOGINID:"),
    RESET_PASSWORD("TEMP_R_PASSWORD:", "VERIFIED_R_PASSWORD:"),
    CHANGE_PASSWORD("TEMP_C_PASSWORD:", "VERIFIED_C_PASSWORD:"),
    CHANGE_EMAIL("TEMP_EMAIL:", "VERIFIED_EMAIL:"),
    DELETE_USER("TEMP_DELETE:", "VERIFIED_DELETE:");

    private final String tempPrefix;
    private final String verifiedPrefix;

    EmailAuthPurpose(String tempPrefix, String verifiedPrefix) {
        this.tempPrefix = tempPrefix;
        this.verifiedPrefix = verifiedPrefix;
    }

    public String getTempKey(String email) {
        return tempPrefix + email;
    }

    public String getVerifiedKey(String email) {
        return verifiedPrefix + email;
    }
}
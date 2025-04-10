package com.freepath.devpath.email.command.application.Dto;

public enum EmailAuthPurpose {
    SIGN_UP("TEMP_USER:", "VERIFIED_USER:"),
    FIND_LOGINID("TEMP_LOGINID:", "VERIFIED_LOGINID:"),
    UPDATE_PASSWORD("TEMP_PASSWORD:", "VERIFIED_PASSWORD:"),
    UPDATE_EMAIL("TEMP_EMAIL:", "VERIFIED_EMAIL:");

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
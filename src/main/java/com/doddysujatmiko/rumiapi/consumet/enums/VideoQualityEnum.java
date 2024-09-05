package com.doddysujatmiko.rumiapi.consumet.enums;

public enum VideoQualityEnum {
    NHD,
    FWVGA,
    HD,
    FHD;

    public static VideoQualityEnum fromString(String quality) {
        return switch (quality.toLowerCase()) {
            case "360p" -> NHD;
            case "480p" -> FWVGA;
            case "720p" -> HD;
            case "1080p" -> FHD;
            default -> FHD;
        };
    }
}

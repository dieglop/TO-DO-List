package com.example.practice.enums;

import java.util.Objects;

public enum ProfileEnum {
    
    ADMIN(1,"ROLE_ADMIN"),
    USER(2, "ROLE_USER");


    private final Integer code;
    private final String description;

    private ProfileEnum(Integer code, String description){
        this.code = code;
        this.description = description;
    }

    public static ProfileEnum toEnum(Integer code){

        if(Objects.isNull(code) )
            return null;

        for(ProfileEnum pe : ProfileEnum.values()){
            if(code.equals(pe.getCode())){
                return pe;
            }
        }

        throw new IllegalArgumentException("Invalid code: " + code);

    }

    public Integer getCode(){
        return this.code;
    }

    public String getDescription() {
        return description;
    }

    
}

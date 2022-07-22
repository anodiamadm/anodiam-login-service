package com.anodiam.StudentSignup.model.common;

public enum ResponseCode {
    SUCCESS(0, "SUCCESS: Microsvc001: Student Signup: Student registration successful: "),
    FAILURE(1, "ERR: Microsvc001: Student Signup: Failure with exception message: "),
    EMAIL_FORMAT_INVALID(2, "ERR: Microsvc001: Student Signup: Invalid Username / Email " +
            "format used for Sign Up: "),
    USER_ALREADY_EXISTS(3, "ERR: Microsvc001: Student Signup: Username / Email used for " +
            "Sign Up is already registered: "),
    USER_NOT_REGISTERED(4, "Microsvc001: Student Signup: Username / Email is NOT already " +
            "registered: "),
    PASSWORD_SHORT(5, "ERR: Microsvc001: Student Signup: Password cannot be < 6 " +
            "characters: "),
    PASSWORD_INVALID(6, "ERR: Microsvc001: Student Signup: Password must be 6 to 100 " +
            "characters long and contain capital alphabet, number and special character: "),
    ROLE_NAME_EXISTS(100, "Microsvc001: Student Signup: Role name exists: "),
    ROLE_NAME_INVALID(101, "ERR: Microsvc001: Student Signup: Role name INVALID: "),
    PERMISSION_NAME_EXISTS(200, "Microsvc001: Student Signup: Permission name exists: "),
    PERMISSION_NAME_INVALID(201, "ERR: Microsvc001: Student Signup: Permission name INVALID: ");

    private Integer id;
    private String message;

    ResponseCode(Integer id, String message){
        this.id = id;
        this.message=message;
    }

    public Integer getID(){
        return id;
    }
    public String getMessage() {return message;}
}
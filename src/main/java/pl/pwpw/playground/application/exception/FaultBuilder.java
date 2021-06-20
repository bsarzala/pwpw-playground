package pl.pwpw.playground.application.exception;

public enum FaultBuilder {
    NO_SUCH_APPLICATION(404, "There is no such application with provided application_number!"),
    NO_SUCH_EMAIL(404, "There is no application with provided email!"),
    WRONG_FILE_TYPE(400, "File has wrong type! Provide .pdf or .jpg only."),
    CANNOT_TRANSFORM_FILE(500, "Corrupted file!");

    private final int errorCode;
    private final String errorMessage;

    FaultBuilder(final int errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    public int getErrorCode(){
        return errorCode;
    }
    public String getErrorMessage(){
        return errorMessage;
    }
}
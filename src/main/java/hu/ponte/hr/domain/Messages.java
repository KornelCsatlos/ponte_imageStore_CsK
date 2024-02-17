package hu.ponte.hr.domain;

public enum Messages {
    EMPTY_OR_NON_EXISTING("The given file is empty or doesn't exist"),
    MORE_THAN_2MB("The file is more than 2MB"),
    WRONG_FILE_TYPE("The file is not an image"),
    EXCEPTION_WHILE_READING_IMAGE("Reading the picture caused an exception"),
    OK("OK");

    private final String message;
    Messages(String message) {
        this.message = message;
    }

    public String value(){
        return message;
    }
}

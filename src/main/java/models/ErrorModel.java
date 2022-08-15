package models;

public class ErrorModel {
    private String field;
    private String message;


    public ErrorModel(String field, String message){
        this.field=field;
        this.message=message;

    }
    public ErrorModel(){
        this(null,null);
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }


}

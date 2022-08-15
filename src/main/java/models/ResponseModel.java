package models;

import java.util.List;

public class ResponseModel {
    public UserDataModel success;
    public List<ErrorModel> error;

    private ResponseModel(UserDataModel successResult, List<ErrorModel> errorResult) {
        success = successResult;
        error = errorResult;
    }

    public static ResponseModel createSuccess(UserDataModel successResult) {
        return new ResponseModel(successResult, null);
    }

    public static ResponseModel createError(List<ErrorModel> errorResult) {
        return new ResponseModel(null, errorResult);
    }
}

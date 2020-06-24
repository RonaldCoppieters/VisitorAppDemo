package be.pxl.VisitorsApplication.controller.resource;

public class BadRequestResource {

    private final String error = "Bad Request";
    private final int status = 400;

    private String code;
    private String message = "No message available.";
    private String path;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStatus() {
        return status;
    }
}

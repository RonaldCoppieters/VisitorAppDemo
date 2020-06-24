package be.pxl.VisitorsApplication.util.exception;

import be.pxl.VisitorsApplication.controller.resource.BadRequestResource;

public class BadRequestException extends Exception {

    private final BadRequestResource badRequestResource;

    public BadRequestException(String code, String message, String path) {
        BadRequestResource badRequestResource = new BadRequestResource();
        badRequestResource.setCode(code);
        badRequestResource.setMessage(message);
        badRequestResource.setPath(path);
        this.badRequestResource = badRequestResource;
    }

    public BadRequestResource getBadRequestResource() {
        return badRequestResource;
    }

}

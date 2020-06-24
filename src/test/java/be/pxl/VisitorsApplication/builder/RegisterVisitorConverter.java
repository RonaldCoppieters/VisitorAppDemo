package be.pxl.VisitorsApplication.builder;

import be.pxl.VisitorsApplication.controller.resource.RegisterVisitorResource;
import be.pxl.VisitorsApplication.model.Visitor;

public class RegisterVisitorConverter {
    public static RegisterVisitorResource Convert(Visitor visitor) {
        RegisterVisitorResource resource = new RegisterVisitorResource();
        resource.setAppointment(visitor.getAppointment());
        resource.setFirstName(visitor.getFirstName());
        resource.setLastName(visitor.getLastName());
        resource.setPhoneNumber(visitor.getPhoneNumber());
        if (visitor.getPatient() != null)
            resource.setPatientCode(visitor.getPatient().getCode());
        return resource;
    }
}

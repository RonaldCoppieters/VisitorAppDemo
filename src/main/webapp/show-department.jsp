<%@ page import="be.pxl.VisitorsApplication.model.Visitor" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css" />
    <title>Show Department</title>
</head>

<body>
<table class="table table-primary table-striped">
    <thead>
    <tr style="background-color: cornflowerblue; color: black; font-weight: bold">
        <td>Appointment</td>
        <td>Patient</td>
        <td>Visitor</td>
        <td>Phone Number</td>
    </tr>
    </thead>
    <tbody>
    <%
        for (Visitor visitor : (List<Visitor>) request.getAttribute("visitors")) {
            out.print(String.format("<tr>" +
                    "<td>%s</td>" +
                    "<td>%s</td>" +
                    "<td>%s</td>" +
                    "<td>%s</td>" +
                    "</td>",
                    visitor.getAppointment().format(DateTimeFormatter.ofPattern("HH:mm")),
                    visitor.getPatient().getCode(),
                    visitor.getFirstName() + " " + visitor.getLastName(),
                    visitor.getPhoneNumber()));
        }
    %>
    </tbody>
</table>
</body>

</html>
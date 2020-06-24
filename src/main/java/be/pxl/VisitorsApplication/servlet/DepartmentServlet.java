package be.pxl.VisitorsApplication.servlet;

import be.pxl.VisitorsApplication.model.Visitor;
import be.pxl.VisitorsApplication.service.VisitorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("department")
public class DepartmentServlet {

    private final VisitorService visitorService;

    public DepartmentServlet(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("index")
    public ModelAndView getAll() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("department");
        return mv;
    }

    @GetMapping
    public ModelAndView show(String code) {
        List<Visitor> visitors = visitorService.getVisitorsForDepartment(code);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("show-department");
        mv.addObject("visitors", visitors);
        return mv;
    }
}


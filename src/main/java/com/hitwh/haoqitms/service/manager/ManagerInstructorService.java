package com.hitwh.haoqitms.service.manager;

import org.springframework.web.bind.annotation.GetMapping;

public interface ManagerInstructorService {
    @GetMapping("/total")
    Integer countTotalInstructor();

}

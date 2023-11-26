package com.hitwh.haoqitms.mapper;

import com.hitwh.haoqitms.entity.CourseList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CourseMapperTest {
    private final CourseMapper courseMapper;

    @Autowired
    public CourseMapperTest(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Test
    void getReportByCourseId() {
        System.out.println(courseMapper.getReportByCourseId(4));
    }

    @Test
    void updateReportByCourseId() {
        System.out.println(courseMapper.updateReportByCourseId(4, "test"));
    }

    @Test
    void getCourses() {
        CourseList courseList = new CourseList();
        courseList.setName("%董%");
//        courseList.setInstructorName("%周%");
//        courseList.setCompanyName("%信%");
//        courseList.setStartDate(new Date(Timestamp.valueOf("2020-01-01 00:00:00").getTime()));
        courseList.setStartDate("2022-01-01");
        System.out.println(courseList.getStartDate());
        List<CourseList> courses = courseMapper.getCourses(courseList, 10, 0);
        for (CourseList course : courses) {
            System.out.println(course);
        }
    }

    @Test
    void getCoursesCount() {
    }
}
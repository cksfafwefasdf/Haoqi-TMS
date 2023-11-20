package com.hitwh.haoqitms.mapper;

import com.hitwh.haoqitms.entity.StudentCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StudentCourseViewMapper {
    /**
     * 根据课程id获取学生信息, 用于现场工作人员完成学生身份核实、考勤和缴费
     * @param courseId 课程id
     * @return 选修该课程的学生信息列表
     */
    @Select("select * from student_course_view where course_id = #{courseId}")
    List<StudentCourse> getStudentCourseByCourseId(Integer courseId);

    /**
     * 根据学生id和课程id更新学生缴费状态
     * @param studentId 学生id
     * @param courseId 课程id
     * @param paid 缴费状态
     * @return 是否更新成功
     */
    @Update("update student_course_view set paid = #{paid} " +
            "where student_id = #{studentId} and course_id = #{courseId}")
    Boolean updateStudentPayStatus(Integer studentId, Integer courseId, Boolean paid);

    /**
     * 根据学生id和课程id更新学生考勤状态
     * @param studentId 学生id
     * @param courseId 课程id
     * @param attendance 考勤状态
     * @return 是否更新成功
     */
    @Update("update student_course_view set attendance = #{attendance} " +
            "where student_id = #{studentId} and course_id = #{courseId}")
    Boolean updateStudentAttendanceStatus(Integer studentId, Integer courseId, Boolean attendance);


}

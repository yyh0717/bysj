package cn.edu.sdjzu.xg.bysj.controller.basic.teacher;

import cn.edu.sdjzu.xg.bysj.domain.Degree;
import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.service.DegreeService;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import cn.edu.sdjzu.xg.bysj.service.ProfTitleService;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
//远程url:47.98.244.111:8080/bysj/teacher.ctl
@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String teacher_json = JSONUtil.getJSON(request);
        Teacher teacherToAdd = JSON.parseObject(teacher_json,Teacher.class);
        JSONObject resp = new JSONObject();
        try {
           boolean teacher =TeacherService.getInstance().add(teacherToAdd);
            //加入数据信息
            if (teacher){
            resp.put("message", "添加成功");}
            else {
                resp.put("message", "添加失败");}
            }
         catch (SQLException e) {
            e.printStackTrace();
            resp.put("message", "数据库异常");
        }catch (Exception e){
            e.printStackTrace();
            resp.put("message", "网络异常");
        }
        //响应
        response.getWriter().println(resp);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        if(id_str == null){
            try {
                responseTeachers(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseTeacher(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String teacher_id = request.getParameter("id");
        int id = Integer.parseInt(teacher_id);
        JSONObject resp = new JSONObject();
        try {
            TeacherService.getInstance().delete(id);
                //加入数据信息
                resp.put("message", "删除成功");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.put("message", "数据库异常");
        }catch (Exception e){
            e.printStackTrace();
            resp.put("message", "网络异常");
        }
        //响应
        response.getWriter().println(resp);
    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Teacher teacherToUpdate = JSON.parseObject(teacher_json, Teacher.class);
        JSONObject resp = new JSONObject();

        //响应
        //增加加Degree对象
        try {
            boolean teacher = TeacherService.getInstance().update(teacherToUpdate);
            if (teacher){
                //加入数据信息
                resp.put("message", "修改成功");}
            else {
                resp.put("message", "修改失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.put("message", "数据库异常");
        }catch (Exception e){
            e.printStackTrace();
            resp.put("message", "网络异常");
        }
        response.getWriter().println(resp);
    }
    private void responseTeacher(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Teacher teacher = TeacherService.getInstance().find(id);
        String teacher_json = JSON.toJSONString(teacher);
        //响应message到前端
        response.getWriter().println(teacher_json);
    }
    //响应所有学位对象
    private void responseTeachers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<Teacher> teacherss = TeacherService.getInstance().findAll();
        String teachers_json = JSON.toJSONString(teacherss);
        response.getWriter().println(teachers_json);
    }
}

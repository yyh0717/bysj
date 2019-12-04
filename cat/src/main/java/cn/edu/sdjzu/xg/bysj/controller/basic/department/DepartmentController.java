package cn.edu.sdjzu.xg.bysj.controller.basic.department;
import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.School;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
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

//远程url:47.98.244.111:8080/bysj/department.ctl
@WebServlet("/department.ctl")
/**
 * 方法-功能
 * put 修改
 * post 添加
 * delete 删除
 * get 查找
 */
public class DepartmentController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        //根据request对象，获得代表参数的JSON字串
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json,Department.class);
        JSONObject resp = new JSONObject();
        try {
            boolean department = DepartmentService.getInstance().add(departmentToAdd);
            //加入数据信息
            if (department){
                resp.put("message", "添加成功");}
            else {
                resp.put("message", "添加失败，请求已被处理");
            }
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


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id_str = request.getParameter("id");
        System.out.println(id_str);
        String paraType = request.getParameter("paraType");
        System.out.println(paraType);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null&&paraType ==null){
                responseDepartments(response);
            }else if (id_str != null&&paraType==null){
                int id = Integer.parseInt(id_str);
                responseDepartment(id,response);
            }else if (id_str !=null &&paraType.equals("school")){
                int schoolId = Integer.parseInt(id_str);
                responseDepartmentBySchoolId(schoolId,response);
            }else {
                response.getWriter().println("兄弟有个地方你打错了");
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            e.printStackTrace();
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }

    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id,类型为str类型
        String departmentId_str = request.getParameter("id");
        //解析字符串，将id类型返回为整数
        int id = Integer.parseInt(departmentId_str);
        JSONObject resp = new JSONObject();

        try {
            boolean department = DepartmentService.getInstance().delete(id);
            //加入数据信息
            if (department){
                resp.put("message", "删除成功");}
            else {
                resp.put("message", "删除失败，请求已被处理或无法删除");
            }
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
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        JSONObject resp = new JSONObject();

        //增加加Degree对象
        try {
            boolean department = DepartmentService.getInstance().update(departmentToAdd);
            if (department){
                resp.put("message", "修改成功");}
            else {
                resp.put("message", "修改失败，请求已被处理");
            }
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
    private void responseDepartment(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);
        response.getWriter().println(department_json);
    }
    //响应所有学位对象
    private void responseDepartments(HttpServletResponse response)
            throws  IOException, SQLException {
        //获得所有学院
        Collection<Department> departments = DepartmentService.getInstance().getAll();
        String departments_json = JSON.toJSONString(departments);
        response.getWriter().println(departments_json);
    }
    private void responseDepartmentBySchoolId(int schoolId,HttpServletResponse response)
            throws IOException,SQLException{
        Collection<Department> departments = DepartmentService.getInstance().findAllBySchool(schoolId);
        String department_json = JSON.toJSONString(departments);
        response.getWriter().println(department_json);
    }
}

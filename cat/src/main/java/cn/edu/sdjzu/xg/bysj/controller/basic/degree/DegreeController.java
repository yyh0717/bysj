package cn.edu.sdjzu.xg.bysj.controller.basic.degree;


import cn.edu.sdjzu.xg.bysj.domain.Degree;
import cn.edu.sdjzu.xg.bysj.service.DegreeService;
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
/**
 * 将所有方法组织在一个Controller(Servlet)中
 */
//远程url:47.98.244.111:8080/bysj/degree.ctl
@WebServlet("/degree.ctl")
public class DegreeController extends HttpServlet {

    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */

    /**
     * 增加一个学位对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String degree_json = JSONUtil.getJSON(request);
        Degree degreeToAdd = JSON.parseObject(degree_json,Degree.class);
        JSONObject resp = new JSONObject();
        //加入数据信息

        try{
            DegreeService.getInstance().add(degreeToAdd);
            resp.put("message", "添加成功");
        }
        catch (SQLException e){
            e.printStackTrace();
            resp.put("message", "添加失败");
        }
        //响应
        response.getWriter().println(resp);
    }

    /**
     * 删除一个学位对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        JSONObject resp = new JSONObject();
        //加入数据信息
        try{
            DegreeService.getInstance().delete(id);
            resp.put("message", "删除成功");
        }
        catch (SQLException e){
            e.printStackTrace();
            resp.put("message", "删除失败");
        }
        //响应
        response.getWriter().println(resp);
    }


    /**
     * 修改一个学位对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        JSONObject resp = new JSONObject();

        //增加加Degree对象
        try {
            DegreeService.getInstance().update(degreeToAdd);
            //加入数据信息
            resp.put("message", "修改成功");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.put("message", "修改失败");
        }
        //响应
        response.getWriter().println(resp);
    }
    /**
     * 响应一个或所有学位对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        //获得所有学院
//        Collection<Degree> degrees = null;
//        try {
//            degrees = DegreeService.getInstance().findAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        String degrees_str = JSON.toJSONString(degrees);
////        JSON.to
//        //向客户端响应数据
//        response.getWriter().println(degrees_str);
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        if(id_str == null){
            try {
                responseDegrees(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseDegree(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void responseDegree(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Degree degree = DegreeService.getInstance().find(id);
        String degree_json = JSON.toJSONString(degree);
        response.getWriter().println(degree_json);
    }
    //响应所有学位对象
    private void responseDegrees(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees);

        response.getWriter().println(degrees_json);
    }

}
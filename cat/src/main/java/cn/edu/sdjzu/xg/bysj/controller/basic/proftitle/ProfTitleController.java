package cn.edu.sdjzu.xg.bysj.controller.basic.proftitle;
import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import cn.edu.sdjzu.xg.bysj.service.ProfTitleService;
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
//远程url:47.98.244.111:8080/bysj/profTitle.ctl
@WebServlet("/profTitle.ctl")
public class ProfTitleController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String profTitle_json = JSONUtil.getJSON(request);
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json,ProfTitle.class);
        JSONObject resp = new JSONObject();
        try {
            ProfTitleService.getInstance().add(profTitleToAdd);
            //加入数据信息
            resp.put("message", "添加成功");
        } catch (SQLException e) {
            e.printStackTrace();
            //加入数据信息
            resp.put("message", "添加失败");
        }
        //响应
        response.getWriter().println(resp);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try {
//            Collection<ProfTitle> profTitles = ProfTitleService.getInstance().getAll();
//            String profTitle_str = JSON.toJSONString(profTitles);
//            response.getWriter().println(profTitle_str);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        if(id_str == null){
            try {
                responseProfTitles(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseProfTitle(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        JSONObject resp = new JSONObject();
        try {
            ProfTitleService.getInstance().delete(id);
            //加入数据信息
            resp.put("message", "删除成功");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.put("message", "删除失败");
        }
        //响应
        response.getWriter().println(resp);
    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        ProfTitle profTitleToUpdate = JSON.parseObject(profTitle_json, ProfTitle.class);
        JSONObject resp = new JSONObject();

        //增加加Degree对象
        try {
            ProfTitleService.getInstance().update(profTitleToUpdate);
            //加入数据信息
            resp.put("message", "更新成功");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.put("message", "更新失败");
        }
        //响应
        response.getWriter().println(resp);
    }
    private void responseProfTitle(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String profTitle_json = JSON.toJSONString(profTitle);
        //响应
        //创建JSON对象message，以便往前端响应信息
        //响应message到前端
        response.getWriter().println(profTitle_json);
    }
    //响应所有学位对象
    private void responseProfTitles(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().getAll();
        String profTitle_json = JSON.toJSONString(profTitles);
        //响应message到前端
        response.getWriter().println(profTitle_json);
    }
}

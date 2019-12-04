package test;

import cn.edu.sdjzu.xg.bysj.dao.SchoolDao;
import cn.edu.sdjzu.xg.bysj.domain.School;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
import java.sql.SQLException;
public class SchoolDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //获得school对象，以便为schoolToAdd的school属性赋值
        School school = SchoolService.getInstance().find(2);
        School schoolToAdd = new School("管理工程","02","最好的学院");
        SchoolDao schoolDao = new SchoolDao();
        //执行Dao对象的方法
        School addedSchool = schoolDao.addWithSP(schoolToAdd);
        //打印添加后返回的对象
        System.out.println(addedSchool);
        System.out.println("添加School成功");
    }
}

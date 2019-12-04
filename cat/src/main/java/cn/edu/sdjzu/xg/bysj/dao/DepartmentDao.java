package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class DepartmentDao {
	private static Collection<Department> departments;

	private static DepartmentDao departmentDao=new DepartmentDao();
	private DepartmentDao(){}

	public static DepartmentDao getInstance(){
		return departmentDao;
	}
	public Collection<Department> findAll()throws SQLException{
		departments = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from department");
		while (resultSet.next()){
			departments.add(new Department(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),
					resultSet.getString("remarks"),SchoolDao.getInstance().find(resultSet.getInt("school_id"))));
		}
		JdbcHelper.close(statement,connection);
		return departments;
	}
	public Collection<Department> findAllBySchool(int schoolId) throws SQLException{
		Collection<Department> departmentsBySchoolId = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("SELECT * from department");
		while (resultSet.next()){
			if (resultSet.getInt("school_id")==schoolId){
				departmentsBySchoolId.add(new Department(
						resultSet.getInt("id"),
						resultSet.getString("description"),
						resultSet.getString("no"),
						resultSet.getString("remarks"),
						SchoolService.getInstance().find(resultSet.getInt("school_id"))));
			}
		}
		return departmentsBySchoolId;
	}
	public Department find(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		String findDepartment_sql = "SELECT * FROM department where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(findDepartment_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();
		Department department= new Department(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),
				resultSet.getString("remarks"),SchoolDao.getInstance().find(resultSet.getInt("school_id")));
		return department;
	}

	public boolean update(Department department) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		String updateDepartment_sql = "UPDATE department SET description = ?,no =?,remarks=? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
		pstmt.setString(1,department.getDescription());
		pstmt.setString(2,department.getNo());
		pstmt.setString(3,department.getRemarks());
		pstmt.setInt(4,department.getId());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("本次改动了"+affectedRowNum+"行");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}
	public boolean add(Department department) throws SQLException {
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO department "+ "(description, no,remarks,school_id)" +" VALUES (?,?,?,?)");
        preparedStatement.setString(1,department.getDescription());
        preparedStatement.setString(2,department.getNo());
        preparedStatement.setString(3,department.getRemarks());
        preparedStatement.setInt(4,department.getSchool().getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println(affectedRowNum);
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
	}
	/**public boolean delete(Integer id){
		Department department = this.find(id);
		return this.delete(department);
	}*/
	public boolean delete(Department department)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("Delete from department WHERE id =?");
		preparedStatement.setInt(1,department.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}
    public static void main(String[] args) throws SQLException{
		Department departmentToAdd = DepartmentService.getInstance().find(5);
		departmentToAdd.setDescription("艺术管理");
		departmentDao.update(departmentToAdd);
		Department department1 = DepartmentService.getInstance().find(5);
		System.out.println("Description = " + department1.getDescription());
    }
}


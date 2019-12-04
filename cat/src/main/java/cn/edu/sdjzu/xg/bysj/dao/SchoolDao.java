package cn.edu.sdjzu.xg.bysj.dao;
import cn.edu.sdjzu.xg.bysj.domain.Degree;
import cn.edu.sdjzu.xg.bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();
	private static Collection<School> schools;
	/**static{
		schools = new TreeSet<School>();
		School school = new School(1,"土木工程","01","");
		schools.add(school);
		schools.add(new School(2,"管理工程","02","最好的学院"));
		schools.add(new School(3,"市政工程","03",""));
		schools.add(new School(4,"艺术","04",""));
	}
	 */
	public SchoolDao(){}
	
	public static SchoolDao getInstance(){
		return schoolDao;
	}

	public Collection<School> findAll() throws SQLException {
		schools = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from school");
		//从数据库中取出数据
		while (resultSet.next()){
			//System.out.println(resultSet.getString("description"));
			schools.add(new School(resultSet.getInt("id"), resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks")));
		}
		JdbcHelper.close(stmt,connection);
		return schools;
	}
	
	public School find(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		String findSchool_sql = "SELECT * FROM school where id = ?";
		//创建预编译语句盒子
		PreparedStatement pstmt = connection.prepareStatement(findSchool_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		//游标下移一行，返回下一行是否有有效记录
		resultSet.next();
		School school= new School(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),
				resultSet.getString("remarks"));
		JdbcHelper.close(pstmt,connection);
		return school;
	}
	
	public boolean update(School school) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateSchool_sql = "UPDATE school SET description =?,no =?,remarks =? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateSchool_sql);
		pstmt.setString(1,school.getDescription());
		pstmt.setString(2,school.getNo());
		pstmt.setString(3,school.getRemarks());
		pstmt.setInt(4,school.getId());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("本次更新了了"+affectedRowNum+"行");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}
	
	public boolean add(School school) throws SQLException {
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO school "+ "(no,description,remarks)" +" VALUES (?,?,?)");
		preparedStatement.setString(1,school.getNo());
		preparedStatement.setString(2,school.getDescription());
		preparedStatement.setString(3,school.getRemarks());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean delete(School school) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("Delete from school WHERE id =?");
		preparedStatement.setInt(1,school.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}
	//存储过程
	public School addWithSP(School school) throws SQLException, ClassNotFoundException {
		//加载驱动程序
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备可调用语句对象，sp_addSchool为存贮过程名称，后面为4个参数
		CallableStatement callableStatement
				= connection.prepareCall("{CALL sp_addSchool(?,?,?,?)}");
		//将第4个参数设置为输出参数，类型为长整型（数据库的数据类型）
		callableStatement.registerOutParameter(4, Types.BIGINT);
		callableStatement.setString(1,school.getDescription());
		callableStatement.setString(2,school.getNo());
		callableStatement.setString(3,school.getRemarks());
		//执行可调用语句callableStatement
		callableStatement.execute();
		//获得第5个参数的值：数据库为该记录自动生成的id
		int id = callableStatement.getInt(4);
		//为参数school的id字段赋值
		school.setId(id);
		JdbcHelper.close(callableStatement,connection);
		return school;
	}
}

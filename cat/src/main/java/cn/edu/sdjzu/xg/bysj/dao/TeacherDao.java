package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	private static Collection<Teacher> teachers;

	public Collection<Teacher> findAll()throws SQLException {
		teachers = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from teacher");
		while (resultSet.next()){
			teachers.add(new Teacher(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("no"),ProfTitleDao.getInstance().find(resultSet.getInt("proftitle_id")),
					DegreeDao.getInstance().find(resultSet.getInt("degree_id")),DepartmentDao.getInstance().find(resultSet.getInt("department_id"))));
		}
		JdbcHelper.close(statement,connection);
		return teachers;
	}
	
	public Teacher find(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM teacher where id = ?");
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();
		Teacher teacher= new Teacher((resultSet.getInt("id")),resultSet.getString("name"),resultSet.getString("no"),ProfTitleDao.getInstance().find(resultSet.getInt("proftitle_id")),
				DegreeDao.getInstance().find(resultSet.getInt("degree_id")),DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
		return teacher;
	}
	
	public boolean update(Teacher teacher) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement pstmt = connection.prepareStatement("UPDATE teacher SET name=?,proftitle_id=?,degree_id=?,department_id=? where id = ?");
		pstmt.setString(1,teacher.getName());
		pstmt.setInt(2,teacher.getTitle().getId());
		pstmt.setInt(3,teacher.getDegree().getId());
		pstmt.setInt(4,teacher.getDepartment().getId());
		pstmt.setInt(5,teacher.getId());
		//返回改动的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("本次改动了"+affectedRowNum+"行");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}
	
	public boolean add(Teacher teacher) throws SQLException{
			Connection connection = null;
			PreparedStatement pstmt = null;
			int affectedRowNum =0;
			try {
				connection = JdbcHelper.getConn();
				connection.setAutoCommit(false);
				String addTeacher_sql = "INSERT INTO teacher(name,no,proftitle_id,degree_id,department_id) VALUES" +
						" (?,?,?,?,?)";
				pstmt = connection.prepareStatement(addTeacher_sql,Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, teacher.getName());
				pstmt.setString(2, teacher.getNo());
				pstmt.setInt(3,teacher.getTitle().getId());
				pstmt.setInt(4,teacher.getDegree().getId());
				pstmt.setInt(5,teacher.getDepartment().getId());
				affectedRowNum = pstmt.executeUpdate();
				System.out.println("添加了 " + affectedRowNum +" 行记录");
				ResultSet resultSet = pstmt.getGeneratedKeys();
				resultSet.next();
				int teacherId = resultSet.getInt(1);
                teacher.setId(teacherId);
				java.util.Date date_util = new java.util.Date();
				Long date_long = date_util.getTime();
				Date date_sql = new Date(date_long);
                UserService.getInstance().add(connection,
                        new User(
                                teacher.getNo(),
                                teacher.getNo(),
                                date_sql,
                                teacher
                        )
                );
			} catch (SQLException e) {
				System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
				try {
					if (connection != null){
						connection.rollback();
					}
				} catch (SQLException e1){
					e.printStackTrace();
				}

			} finally {
				try {
					if (connection != null){
						//恢复自动提交
						connection.setAutoCommit(true);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				//关闭资源
				JdbcHelper.close(pstmt,connection);

			}
            return affectedRowNum>0;
		}
//		Connection connection = JdbcHelper.getConn();
//		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO teacher (name, proftitle_id,degree_id,department_id) VALUES (?,?,?,?)");
//		preparedStatement.setString(1,teacher.getName());
//		preparedStatement.setInt(2,teacher.getTitle().getId());
//		preparedStatement.setInt(3,teacher.getDegree().getId());
//		preparedStatement.setInt(4,teacher.getDepartment().getId());
//		int affectedRowNum = preparedStatement.executeUpdate();
//		System.out.println("本次更新了" + affectedRowNum + "行");
//		JdbcHelper.close(preparedStatement,connection);
//		return affectedRowNum>0;


	/**public boolean delete(Integer id){
		Teacher teacher = this.find(id);
		return this.delete(teacher);
	}
	*/
	public void delete(Teacher teacher) throws SQLException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {

			connection = JdbcHelper.getConn();
			String deleteUser_sql = "DELETE FROM user WHERE teacher_id = ?";
			pstmt = connection.prepareStatement(deleteUser_sql);
			pstmt.setInt(1,teacher.getId());
			int affectedRowNum = pstmt.executeUpdate();
			System.out.println("删除了 " + affectedRowNum +" 行记录");

			String deleteTeacher_sql = "DELETE FROM teacher WHERE id = ?";
			pstmt = connection.prepareStatement(deleteTeacher_sql);
			pstmt.setInt(1,teacher.getId());
			int affectedRowNum1 = pstmt.executeUpdate();
			System.out.println("删除了 " + affectedRowNum1 +" 行记录");


		} catch (SQLException e) {
			System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
			try {
				if (connection != null){
					connection.rollback();
				}
			} catch (SQLException e1){
				e.printStackTrace();
			}

		} finally {
			try {
				if (connection != null){
					//恢复自动提交
					connection.setAutoCommit(true);

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(pstmt,connection);
		}
	}
//		Connection connection = JdbcHelper.getConn();
//		PreparedStatement preparedStatement = connection.prepareStatement("Delete from teacher WHERE id =?");
//		preparedStatement.setInt(1,teacher.getId());
//		int affectedRowNum = preparedStatement.executeUpdate();
//		System.out.println(affectedRowNum);
//		JdbcHelper.close(preparedStatement,connection);
//		return affectedRowNum>0;
//	}
}

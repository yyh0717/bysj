package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;


public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}
	
	private static Collection<User> users;
//	static{
//		TeacherDao teacherDao = TeacherDao.getInstance();
//		users = new TreeSet<User>();
//		User user = new User(1,"st","st",new Date(),teacherDao.find(1));
//		users.add(user);
//		users.add(new User(2,"lx","lx",new Date(),teacherDao.find(2)));
//		users.add(new User(3,"wx","wx",new Date(),teacherDao.find(3)));
//		users.add(new User(4,"lf","lf",new Date(),teacherDao.find(4)));
//	}
public Collection<User> findAll() throws SQLException {
    Collection<User> users = new TreeSet<User>();
    Connection connection = JdbcHelper.getConn();
    Statement stmt = connection.createStatement();
    ResultSet resultSet = stmt.executeQuery("select * from user");
    while(resultSet.next()){
        User user = new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getDate("loginTime"),
                TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
        );

        users.add(user);
    }
    return users;
}

    public User find(Integer id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String findUser_sql = "SELECT * FROM user where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(findUser_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        User user = new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getDate("password"),
                TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
        );
        return user;
    }


    public User login(String username,String password) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        String login_sql = "SELECT * FROM user where username = ?,password = ?";
        PreparedStatement pstmt = connection.prepareStatement(login_sql);
        pstmt.setString(1,username);
        pstmt.setString(2,password);
        ResultSet resultSet = pstmt.executeQuery();
        user = UserService.getInstance().find(resultSet.getInt("id"));

        return user;
    }


	public boolean update(User user){
		users.remove(user);
		return users.add(user);		
	}

    public boolean add(User user) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String addUser_sql = "INSERT INTO user(username,password,loginTime,teacher_id) VALUES" +
                " (?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(addUser_sql);
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2,user.getPassword());
        pstmt.setDate(3, (java.sql.Date) user.getLoginTime());
        pstmt.setInt(4,user.getTeacher().getId());
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("添加了 " + affectedRowNum +" 行记录");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum>0;
}
    public boolean add(Connection connection,User user) throws SQLException {
        String addUser_sql = "INSERT INTO user(username,password,loginTime,teacher_id) VALUES" +
                " (?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(addUser_sql);
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2,user.getPassword());
        pstmt.setDate(3, (java.sql.Date) user.getLoginTime());
        pstmt.setInt(4,user.getTeacher().getId());
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("添加了 " + affectedRowNum +" 行记录");
        pstmt.close();
        return affectedRowNum>0;
    }
    public boolean delete(User user) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String deleteUser_sql = "DELETE FROM user WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(deleteUser_sql);
        pstmt.setInt(1,user.getId());
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了 " + affectedRowNum +" 行记录");

        JdbcHelper.close(pstmt,connection);
        return affectedRowNum>0;
    }
    public User findByUsername(String username) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String findUser_sql = "SELECT * FROM user WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(findUser_sql);
        pstmt.setString(1,username);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        User user = new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getDate("password"),
                TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
        );
        return user;
    }
	//修改密码
	public boolean changePassword(User user,String newPassword) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user set password=? where id =?");
		preparedStatement.setString(1,newPassword);
		preparedStatement.setInt(2,user.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("本次更新了"+affectedRowNum+"条密码");
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

//	public static void main(String[] args){
//		UserDao dao = new UserDao();
//		Collection<User> users = dao.findAll();
//		display(users);
//	}
//	private static void display(Collection<User> users) {
//		for (User user : users) {
//			System.out.println(user);
//		}
//	}
}

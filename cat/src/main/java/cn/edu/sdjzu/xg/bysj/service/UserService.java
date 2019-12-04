package cn.edu.sdjzu.xg.bysj.service;


import cn.edu.sdjzu.xg.bysj.dao.UserDao;
import cn.edu.sdjzu.xg.bysj.domain.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();
	
	public UserService() {
	}
	
	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> getUsers() throws SQLException {
		return userDao.findAll();
	}
	
	public User getUser(Integer id) throws SQLException {
		return userDao.find(id);
	}
	
	public boolean updateUser(User user) throws SQLException {
		userDao.delete(user);
		return userDao.add(user);
	}
	
	public boolean addUser(User user) throws SQLException {
		return userDao.add(user);
	}
	public boolean add(Connection connection, User user) throws SQLException {
		return UserDao.getInstance().add(connection,user);
	}
	public boolean deleteUser(Integer id) throws SQLException {
		User user = this.getUser(id);
		return this.deleteUser(user);
	}
	
	public boolean deleteUser(User user) throws SQLException {
		return userDao.delete(user);
	}
	public boolean changePassword(User user,String newPassword) throws SQLException {
		return UserDao.getInstance().changePassword(user,newPassword);
	}
	public User find(Integer id) throws SQLException {
		return UserDao.getInstance().find(id);
	}
	public User login(String username, String password) throws SQLException {
		Collection<User> users = this.getUsers();
		User desiredUser = null;
		for(User user:users){
			if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
				desiredUser = user;
			}
		}
		return desiredUser;
	}	
}

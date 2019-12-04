package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}
	private static Collection<ProfTitle> profTitles;
	public Collection<ProfTitle> findAll()throws SQLException{
		profTitles = new HashSet<>();
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from proftitle");
		while (resultSet.next()){
			profTitles.add(new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"), resultSet.getString("remarks")));
		}
		return ProfTitleDao.profTitles;
	}
	public ProfTitle find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM proftitle where id =?");
		preparedStatement.setInt(1,id);
		//创建结果集对象，执行预编译语句并返回结果集
		ResultSet resultSet = preparedStatement.executeQuery();
		ProfTitle profTitle = null;

		if (resultSet.next()){
		profTitle = new ProfTitle(
				resultSet.getInt("id"),
				resultSet.getString("description"),
				resultSet.getString("no"),
				resultSet.getString("remarks"));}
		JdbcHelper.close(preparedStatement,connection);
		return profTitle;
	}

	public boolean update(ProfTitle profTitle) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement pstmt = connection.prepareStatement("UPDATE proftitle SET description =?,no =?,remarks =? where id = ?");
		pstmt.setString(1,profTitle.getDescription());
		pstmt.setString(2,profTitle.getNo());
		pstmt.setString(3,profTitle.getRemarks());
		pstmt.setInt(4,profTitle.getId());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("本次更新了了"+affectedRowNum+"行");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}

	public boolean add(ProfTitle profTitle) throws SQLException {
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO proftitle "+ "(no,description,remarks)" +" VALUES (?,?,?)");
		preparedStatement.setString(1,profTitle.getNo());
		preparedStatement.setString(2,profTitle.getDescription());
		preparedStatement.setString(3, profTitle.getRemarks());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	/**public boolean delete(Integer id){
		ProfTitle profTitle = this.find(id);
		return this.delete(profTitle);
	}*/

	public boolean delete(ProfTitle profTitle)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("Delete from proftitle WHERE id =?");
		preparedStatement.setInt(1,profTitle.getId());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}
}


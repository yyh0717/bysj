package cn.edu.sdjzu.xg.bysj.dao;
import cn.edu.sdjzu.xg.bysj.domain.Degree;
import cn.edu.sdjzu.xg.bysj.domain.Department;
import util.JdbcHelper;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;
public final class DegreeDao {
	private static DegreeDao degreeDao=
			new DegreeDao();
	private DegreeDao(){}
	public static DegreeDao getInstance(){
		return degreeDao;
	}
	private static Collection<Degree> degrees;
	public Collection<Degree> findAll()throws SQLException{
        degrees = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from Degree");
        //从数据库中取出数据
        while (resultSet.next()){
            //System.out.println(resultSet.getString("description"));
            degrees.add(new Degree(resultSet.getInt("id"), resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks")));
        }
        JdbcHelper.close(stmt,connection);
        return degrees;
	}
	public Degree find(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		String findDegree_sql = "SELECT * FROM degree where id = ?";
		//创建预编译语句盒子
		PreparedStatement pstmt = connection.prepareStatement(findDegree_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		//游标下移一行，返回下一行是否有有效记录
		resultSet.next();
		Degree degree= new Degree(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),
				resultSet.getString("remarks"));
		JdbcHelper.close(pstmt,connection);
		return degree;
	}
	public boolean update(Degree degree) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "UPDATE degree SET description =?,no =?,remarks =? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
		pstmt.setString(1,degree.getDescription());
		pstmt.setString(2,degree.getNo());
		pstmt.setString(3,degree.getRemarks());
		pstmt.setInt(4,degree.getId());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("本次更新了了"+affectedRowNum+"行");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}
	public boolean add(Degree degree) throws SQLException {
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO degree "+ "(no,description)" +" VALUES (?,?)");
		preparedStatement.setString(1,degree.getNo());
		preparedStatement.setString(2,degree.getDescription());
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println(affectedRowNum);
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}
//	public boolean delete(Integer id) throws SQLException {
//		Degree degree = this.find(id);
//		return this.delete(degree);
//	}
	public boolean delete(Degree degree)throws SQLException{
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("Delete from degree WHERE id =?");
        preparedStatement.setInt(1,degree.getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println(affectedRowNum);
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
	}
}


package cn.yuanye.DynamicProxy;

public class OracleConnectionPool implements DatabaseConnectionPool {

	@Override
	public int getConnection() {
		System.out.println("OracleConnectionPool:get connection");
		return 0;
	}

	@Override
	public void releaseConnection() {
		System.out.println("OracleConnectionPool:release connection");
	}

	@Override
	public void initPool() {
		System.out.println("OracleConnectionPool:pool inited");
	}

	@Override
	public void releasePool() {
		System.out.println("OracleConnectionPool:pool released");
	}

}

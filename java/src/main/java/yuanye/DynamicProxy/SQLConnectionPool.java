package yuanye.DynamicProxy;

public class SQLConnectionPool implements DatabaseConnectionPool {

	@Override
	public int getConnection() {
		System.out.println("SQLConnectionPool:getConnection");
		return 1;
	}

	@Override
	public void releaseConnection() {
		System.out.println("SQLConnectionPool:releaseConnection");
	}

	@Override
	public void initPool() {
		System.out.println("SQLConnectionPool:pool inited");
	}

	@Override
	public void releasePool() {
		System.out.println("SQLConnectionPool:pool released");
	}

}

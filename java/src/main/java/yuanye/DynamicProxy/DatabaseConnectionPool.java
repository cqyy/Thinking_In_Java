package yuanye.DynamicProxy;

//interface for database connection
public interface DatabaseConnectionPool {
	int getConnection();
	void releaseConnection();
	void initPool();
	void releasePool();
}

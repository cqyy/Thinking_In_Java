package yuanye.DynamicProxy;

import java.lang.reflect.Proxy;

public class Client {

	public static void main(String[] args) {
		SQLConnectionPool pool = new SQLConnectionPool();
		DatabaseConnectionPool proxyPool = (DatabaseConnectionPool) Proxy
				.newProxyInstance(
						DatabaseConnectionPool.class.getClassLoader(),
						new Class[] { DatabaseConnectionPool.class },
						new ConnectionPoolProxyHandler(pool));

		for (int i = 0; i < 5; i++) {
			proxyPool.getConnection();
		}

		for (int i = 0; i < 5; i++) {
			proxyPool.releaseConnection();
		}

		OracleConnectionPool pool2 = new OracleConnectionPool();
		DatabaseConnectionPool proxyPool2 = 
				(DatabaseConnectionPool) Proxy.newProxyInstance(
						DatabaseConnectionPool.class.getClassLoader(),
						new Class[] { DatabaseConnectionPool.class },
						new ConnectionPoolProxyHandler(pool2));

		for (int i = 0; i < 5; i++) {
			proxyPool2.getConnection();
		}

		for (int i = 0; i < 5; i++) {
			proxyPool2.releaseConnection();
		}

	}
}

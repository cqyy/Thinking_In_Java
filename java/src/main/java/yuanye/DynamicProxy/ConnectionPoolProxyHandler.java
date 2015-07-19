package yuanye.DynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <p>mock database connectionPool</p>
 * @author YuanYe
 *
 */
public class ConnectionPoolProxyHandler implements InvocationHandler {
	//connected clients count
	private int clients = 0;
	private DatabaseConnectionPool pool ; 

	public ConnectionPoolProxyHandler(DatabaseConnectionPool pool){
		this.pool = pool;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result=null;
		//get connection;if the pool is empty,initialize the pool
		if(method.getName() == "getConnection"){
			if(clients++ == 0){
				pool.initPool();
			}
			result = method.invoke(pool, args);
		}
		//release connection;if no clients connected,release the pool
		else if(method.getName() == "releaseConnection"){
			result = method.invoke(pool, args);
			if(--clients <= 0){
				pool.releasePool();
			}
		}else{
			result = method.invoke(pool, args);
		}
		return result;
	}
}

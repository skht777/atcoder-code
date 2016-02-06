/**
 * 
 */
package com.skht777.atcoder;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author skht777
 *
 */
public class HttpClientAdapter implements Closeable {

	public static ResponseHandler<URL> REDIRECT;
	
	static {
		REDIRECT = 	res -> res.containsHeader("Location") ? new URL(res.getFirstHeader("Location").getValue()) : null;
	}
	
	private CloseableHttpClient client;
	
	public class RequestAdapter {
		private HttpUriRequest request;
		
		private RequestAdapter(HttpUriRequest request) {
			this.request = request;
		}
		
		public <T> T handle(ResponseHandler<T> res) throws IOException {
			return client.execute(request, res);
		}
		
		public <T> T execute(Function<String, T> parser) throws IOException {
			return parser.apply(handle(new BasicResponseHandler()));
		}
		
		public void execute(Consumer<String> parser) throws IOException {
			parser.accept(handle(new BasicResponseHandler()));
		}
		
		public void execute() throws IOException {
			handle(new BasicResponseHandler());
		}
		
	}
	
	/**
	 * 
	 */
	public HttpClientAdapter(CloseableHttpClient client) {
		this.client = client;
	}
	
	public RequestAdapter request(URL url) throws IOException {
		return new RequestAdapter(new HttpGet(url.toExternalForm()));
	}
	
	public RequestAdapter request(URL url, List<BasicNameValuePair> param) throws IOException {
		HttpPost post = new HttpPost(url.toExternalForm());
		post.setEntity(new UrlEncodedFormEntity(param));
		return new RequestAdapter(post);
	}
	
	@Override
	public void close() throws IOException  {
		client.close();
	}

}

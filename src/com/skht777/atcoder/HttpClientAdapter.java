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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author skht777
 *
 */
public class HttpClientAdapter implements Closeable {
	
	private CloseableHttpClient client;
	
	public class RequestAdapter {
		private HttpUriRequest request;
		
		private RequestAdapter(HttpUriRequest request) {
			this.request = request;
		}
		
		public <T> T handle(ResponseHandler<T> res) throws IOException {
			return client.execute(request, res);
		}
		
		public String execute() throws IOException {
			return handle(res->EntityUtils.toString(res.getEntity(), "UTF-8"));
		}
		
		public <T> T execute(Function<String, T> parser) throws IOException {
			return parser.apply(execute());
		}
		
		public void consume(Consumer<String> parser) throws IOException {
			parser.accept(execute());
		}
		
	}
	
	/**
	 * 
	 */
	public HttpClientAdapter(CloseableHttpClient client) {
		this.client = client;
	}
	
	public HttpClientAdapter() {
		client = HttpClients.createDefault();
	}
	
	public RequestAdapter request(String url) throws IOException {
		return request(new URL(url));
	}
	
	public RequestAdapter request(URL url) throws IOException {
		return new RequestAdapter(new HttpGet(url.toExternalForm()));
	}
	
	public RequestAdapter request(String url, List<BasicNameValuePair> param) throws IOException {
		return request(new URL(url), param);
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

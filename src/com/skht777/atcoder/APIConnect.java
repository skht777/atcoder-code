/**
 * 
 */
package com.skht777.atcoder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


/**
 * @author skht777
 *
 */
public class APIConnect {

	private HttpClientAdapter adapter;
	private Map<String, String> contests;
	private Map<String, String> problems;
	private Map<String, List<Submission>> submissions;
	
	/**
	 * @throws IOException
	 * 
	 */
	public APIConnect() throws IOException {
		adapter = new HttpClientAdapter(HttpClients.createDefault());
		contests = adapter.request(new URL("http://kenkoooo.com/atcoder-api/contests")).handle(res->{
			JSONObject obj = new JSONObject(EntityUtils.toString(res.getEntity(), "UTF-8"));
			return toMap(obj.keySet(), key->key, v->obj.getJSONObject(v).getString("name"));
		});
	}
	
	private Map<String, String> toMap (Set<String> keys, Function<String, String> keyMapper, Function<String, String> valueMapper) {
		return keys.stream().collect(Collectors.toMap(keyMapper, valueMapper, (a,b) -> a, TreeMap::new));
	}
	
	private URL getURL(String api, String id, String contest) {
		try {
			return new URL("http://skht777.webcrow.jp/atcoder-api/submissions/" + api + ".php?id=" + id + "&contest=" + contest);
		}catch(MalformedURLException e) {}
		return null;
	}
	
	public void getInfo(String userId, String contest) throws IOException {
		JSONObject res = adapter.request(getURL("list", userId, contest)).execute(s->{return new JSONObject(s).getJSONObject(contest);});
		JSONObject p = res.getJSONObject("problems");
		problems = toMap(p.keySet(), key->key, v->v + " " + p.getJSONObject(v).getString("title"));
		JSONObject s = res.getJSONObject("submissions");
		submissions = s.keySet().stream().map(v->new Submission(s.getJSONObject(v))).collect(Collectors.groupingBy(sub->sub.getNumber()));
	}
	
	public String getCode(String id, String contest) throws IOException {
		return adapter.request(getURL("answer", id, contest)).execute(s->s);
	}

	public Map<String, String> getContests() {
		return contests;
	}

	public Map<String, String> getProblems() {
		return problems;
	}

	public Map<String, List<Submission>> getSubmissions() {
		return submissions;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		APIConnect api = new APIConnect();
		api.contests.entrySet().stream().sorted((e1,e2)->e1.getKey().compareTo(e2.getKey())).forEach(System.out::println);
		//api.getInfo("abc032");
		//api.submissions.entrySet().forEach(System.out::println);
	}

}

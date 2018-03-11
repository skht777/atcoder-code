/**
 * 
 */
package com.skht777.atcoder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * @author skht777
 *
 */
public class APIConnect {

	private HttpClientAdapter adapter;

	/**
	 * 
	 */
	public APIConnect() {
		adapter = new HttpClientAdapter();
	}

	private String getURL(String api, String id, String contest) {
		return String.format("http://skht777.webcrow.jp/atcoder-api/submissions/%s.php?id=%s&contest=%s", api, id, contest);
	}
	
	public List<Pair<String>> getContests() throws IOException {
		JSONArray obj = adapter.request("http://kenkoooo.com/atcoder/atcoder-api/info/contests").execute(JSONArray::new);
		return IntStream.range(0, obj.length()).mapToObj(i->obj.getJSONObject(i))
				.map(o->new Pair<>(o.getString("title"), o.getString("id"))).collect(Collectors.toList());
	}
	
	public boolean isUserValid(String user) {
		try {
			return adapter.request("http://kenkoooo.com/atcoder/atcoder-api/results?user=" + user).execute(JSONArray::new).length() > 1;
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Pair<List<Submission>>> getInfo(String userId, String contest) throws IOException {
		JSONObject res = adapter.request(getURL("list", userId, contest)).execute(JSONObject::new).getJSONObject(contest);
		Map<String, List<Submission>> submission = Stream.of(res.getJSONObject("submissions"))
				.flatMap(o->o.keySet().stream().map(k->o.getJSONObject(k)).map(Submission::new)).collect(Collectors.groupingBy(Submission::getNumber));
		return Stream.of(res.getJSONObject("problems")).flatMap(o->o.keySet().stream().map(k->o.getJSONObject(k))).map(o->new Pair<>(o.getString("number"), o.getString("title")))
				.map(p->new Pair<>(p.getKey() + " " + p.getValue(), submission.getOrDefault(p.getKey(), new ArrayList<>()))).collect(Collectors.toList());
	}

	public String getCode(String id, String contest) throws IOException {
		return adapter.request(getURL("answer", id, contest)).execute();
	}

}

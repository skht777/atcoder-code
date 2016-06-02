/**
 * 
 */
package com.skht777.atcoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.util.Pair;


/**
 * @author skht777
 *
 */
public class APIConnect {

	private HttpClientAdapter adapter;

	/**
	 * @throws IOException
	 * 
	 */
	public APIConnect() throws IOException {
		adapter = new HttpClientAdapter();
	}

	private String getURL(String api, String id, String contest) throws IOException {
		return String.format("http://skht777.webcrow.jp/atcoder-api/submissions/%s.php?id=%s&contest=%s", api, id, contest);
	}
	
	private Stream<JSONObject> toList(JSONObject target) {
		return target.keySet().stream().map(s->target.getJSONObject(s));
	}
	
	public List<Pair<String, String>> getContests() throws IOException {
		JSONArray obj = adapter.request("http://kenkoooo.com/atcoder/json/contests.json").execute(JSONArray::new);
		return IntStream.range(0, obj.length()).mapToObj(i->obj.getJSONObject(i)).map(o->new Pair<>(o.getString("name"), o.getString("id"))).collect(Collectors.toList());
	}

	public List<Pair<String, List<Submission>>> getInfo(String userId, String contest) throws IOException {
		JSONObject res = adapter.request(getURL("list", userId, contest)).execute(JSONObject::new).getJSONObject(contest);
		Map<String, List<Submission>> submission = toList(res.getJSONObject("submissions")).map(Submission::new)
				.collect(Collectors.groupingBy(Submission::getNumber));
		return toList(res.getJSONObject("problems")).map(o->new Pair<>(o.getString("number"), o.getString("title")))
				.map(p->new Pair<>(p.getKey() + " " + p.getValue(), submission.getOrDefault(p.getKey(), new ArrayList<>()))).collect(Collectors.toList());
	}

	public String getCode(String id, String contest) throws IOException {
		return adapter.request(getURL("answer", id, contest)).execute();
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		APIConnect api = new APIConnect();
		api.getContests().stream().sorted((e1,e2)->e1.getKey().compareTo(e2.getKey())).forEach(System.out::println);
	}

}

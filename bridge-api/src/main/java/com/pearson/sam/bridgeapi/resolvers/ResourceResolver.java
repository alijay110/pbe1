package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CREATE_RESOURCE;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GET_PAGINATED_RESOURCE_TABLE_DATA;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GET_RESOURCE_DATA_BY_RESOURCE_ID;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.UPDATE_RESOURCE;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.ibridgeservice.IResourceBridgeService;
import com.pearson.sam.bridgeapi.model.Content;
import com.pearson.sam.bridgeapi.model.FirstLevel;
import com.pearson.sam.bridgeapi.model.Resource;
import com.pearson.sam.bridgeapi.model.ResourceData;
import com.pearson.sam.bridgeapi.model.SecondLevel;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.samservices.ProductServices;
import com.pearson.sam.bridgeapi.util.Utils;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.plaf.synth.SynthSeparatorUI;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
public class ResourceResolver {

	private static final Logger logger = LoggerFactory.getLogger(ResourceResolver.class);

	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();

	@Autowired
	IResourceBridgeService resourceBridgeService;

	@Autowired
	ProductServices productServices;

	@Value("${SPEC_FILE}")
	private String specJsonFile;

	@Value("${RESOURCE_PATH}")
	private String resourcePath;

	@GraphQLMutation(name = CREATE_RESOURCE)
	public Resource createResource(@GraphQLArgument(name = "data") Resource data) {
		//logger.info("Creating a Resource in resolver {}");
		return resourceBridgeService.create(data);
	}

	@GraphQLMutation(name = UPDATE_RESOURCE)
	public Resource updateResource(@GraphQLArgument(name = "resourceId") String resourceId,
			@GraphQLArgument(name = "data") Resource data) {
		//logger.info("Updating a Resource in resolver {}");
		// data.setResourceId(resourceId);
		// return resourceBridgeService.update(data);
		return null;
	}

	@SuppressWarnings("unchecked")
	@GraphQLQuery(name = GET_RESOURCE_DATA_BY_RESOURCE_ID)
	public Resource getResourceDataByResourceId(@GraphQLArgument(name = "resourceId") String resourceId) {
		//logger.info("Fetching a Resource in resolver {}");
		/*
		 * Resource resource = new Resource();
		 * resource.setResourceId(resourceId); return
		 * resourceBridgeService.fetchOne(resource);
		 */
		return null;
	}

	@GraphQLQuery(name = GET_PAGINATED_RESOURCE_TABLE_DATA)
	public Page<Resource> getPaginatedResourceTableData(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") Resource filter,
			@GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"resourceId\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLArgument(name = "productId") String productId,
			@GraphQLRootContext AuthContext context) {
		
		
		Pageable pageable = PageRequest.of(pageNumber, pageLimit, Direction.valueOf(sort.getOrder().toString()),
				sort.getField());

		return resourceBridgeService.pageIt(pageable, Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)), productId);

	}

	private Map<String, List<JSONObject>> sortBasedOnChapter(Map<String, List<JSONObject>> sectionMap) {
		Map<String, List<JSONObject>>  sortedMap = new LinkedHashMap<>();
		Map<Integer, String>  sortedChapterMap = new TreeMap<>();
		
		sectionMap.keySet().forEach(s ->{
			List<JSONObject> objList =  sortedList(sectionMap.get(s), "sectionOrder");
			JSONObject obj = objList.get(0);
			sortedChapterMap.put(Integer.valueOf( (String) obj.get("sectionOrder")), s);
		});
		
		sortedChapterMap.keySet().forEach(s ->{
			sortedMap.put(sortedChapterMap.get(s),sectionMap.get(sortedChapterMap.get(s)));
		});
		return sortedMap;
	}

	private Map<String, List<JSONObject>> groupWithChapter(JSONArray jsonObject) {
		Map<String, List<JSONObject>> sectionMap = new HashMap<>();
		jsonObject.forEach(s -> {
			JSONObject obj = (JSONObject) s;
			String section = (String) obj.get("section");
			if (!section.isEmpty() && sectionMap.containsKey(section)) {
				List<JSONObject> obj1 = sectionMap.get(section);
				obj1.add(obj);
			} else {
				if (!section.isEmpty()) {
					List<JSONObject> list = new ArrayList<>();
					list.add(obj);
					sectionMap.put(section, list);
				}

			}
		});
		return sectionMap;
	}

	private Map<String, List<JSONObject>> buildSubTopic(List<JSONObject> topics, List<JSONObject> slrdList) {

		topics.forEach(t -> {
			JSONObject obj = t;
			if (obj.containsKey("sub-topic")) {

			} else {
				JSONObject resource = new JSONObject();
				getResourceObjectTopic(resource, t);
				slrdList.add(resource);
			}
		});

		 System.out.println("-------"+slrdList);
		return null;
	}

	private Map<String, List<JSONObject>> getChildNode(List<JSONObject> list, String key, String order, String next,
			List flrdList) {
		Map<String, List<JSONObject>> map = new LinkedHashMap<>();
		//Sort Resources based on order
		List<JSONObject> sortedTopicList = sortedList(list, "order");
		sortedTopicList.forEach(l -> {
			JSONObject obj = (JSONObject) l;
			// String section = (String) obj.get(key);
			System.out.println("Chapter : " + obj.get(key) + ", Chapter order: " + obj.get("sectionOrder") + ", Topic: " + obj.get(next));
			JSONObject obj2 = null;
			String name = null;
			if (obj.containsKey("topic")) {
				obj2 = (JSONObject) obj.get(next);
				addResource(obj2, obj);
				name = (String) obj2.get("name");
			} else {
				//Add Resources to chapter if it does't contain topic
				JSONObject resource = new JSONObject();
				getResourceObjectChapter(resource, obj);
				flrdList.add(resource);
			}

			if (obj2 != null) {

				if (!name.isEmpty() && map.containsKey(name)) {
					List<JSONObject> obj1 = map.get(name);
					obj1.add(obj2);
				} else {
					if (!name.isEmpty()) {
						List<JSONObject> tempList = new ArrayList<>();
						tempList.add(obj2);
						map.put(name, tempList);
					}

				}

			}
		});

		return map;
	}

	private JSONObject addResource(JSONObject toObj, JSONObject fromObj) {

		JSONObject resObj = new JSONObject();
		if (fromObj != null && toObj != null && fromObj.containsKey("url")) {
			resObj.put("url", fromObj.get("url"));
			resObj.put("name", fromObj.get("name"));
			resObj.put("limitByRoles", fromObj.get("limitByRoles"));
			resObj.put("fileType", fromObj.get("fileType"));
			resObj.put("contentTypeValue", fromObj.get("contentTypeValue"));
			resObj.put("isDownload", fromObj.get("isDownload"));
			resObj.put("description", fromObj.get("description"));
			resObj.put("order", fromObj.get("order"));
			resObj.put("sectionOrder", fromObj.get("sectionOrder"));
			
			toObj.put("resource", resObj);
		}

		return toObj;
	}

	private JSONObject getResourceObjectChapter(JSONObject toObj, JSONObject fromObj) {

		if (fromObj != null && toObj != null && fromObj.containsKey("url")) {
			toObj.put("resourceUrl", fromObj.get("url"));
			toObj.put("resourceTitle", fromObj.get("name"));
			// toObj.put("roleAccess", fromObj.get("limitByRoles"));
			toObj.put("fileType", fromObj.get("fileType").toString());
			toObj.put("contentTypeValue", fromObj.get("contentTypeValue"));
			toObj.put("isDownload", fromObj.get("isDownloadable"));
			toObj.put("order", fromObj.get("order"));
			toObj.put("sectionOrder", fromObj.get("sectionOrder"));
			// toObj.put("description", fromObj.get("description").toString());

		}

		return toObj;
	}
	
	private JSONObject getResourceObjectTopic(JSONObject toObj, JSONObject fromObj) {

		if (fromObj != null && toObj != null && fromObj.containsKey("resource")) {
			JSONObject resObj = (JSONObject) fromObj.get("resource");
			if(fromObj != null && toObj != null && fromObj.containsKey("resource")){
				toObj.put("resourceUrl", resObj.get("url"));
				toObj.put("resourceTitle", resObj.get("name"));
				// toObj.put("roleAccess", fromObj.get("limitByRoles"));
				toObj.put("fileType", resObj.get("fileType"));
				toObj.put("contentTypeValue", resObj.get("contentTypeValue"));
				toObj.put("isDownload", resObj.get("isDownloadable"));
				toObj.put("order", resObj.get("order"));
				toObj.put("sectionOrder", resObj.get("sectionOrder"));
				// toObj.put("description", fromObj.get("description").toString());
			}
			

		}

		return toObj;
	}

	private List<JSONObject> sortedList(List<JSONObject> unsortedList, String key) {
		Map<Integer, JSONObject> map = new TreeMap<>();
		List<JSONObject> sortedList = new LinkedList<>();
		unsortedList.forEach(l -> {

			Integer name = (Integer) Integer.valueOf((String) l.get(key));
			if (Optional.ofNullable(name).isPresent()) {
				map.put(name, l);
			}

		});

		map.keySet().forEach(s -> {
			sortedList.add(map.get(s));
		});
		return sortedList;

	}


	private List<ResourceData> convertToResourceData(List<JSONObject> list) {
		List<ResourceData> convertedList = new LinkedList<>();

		list.forEach(l -> {
			ResourceData data = gson.fromJson(l.toJSONString(), ResourceData.class);
			convertedList.add(data);
		});
		return convertedList;
	}
}
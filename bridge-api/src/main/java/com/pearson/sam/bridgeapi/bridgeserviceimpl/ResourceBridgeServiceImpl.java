package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pearson.sam.bridgeapi.ibridgeservice.IResourceBridgeService;
import com.pearson.sam.bridgeapi.iservice.IProductService;
import com.pearson.sam.bridgeapi.iservice.IResourceService;
import com.pearson.sam.bridgeapi.model.Content;
import com.pearson.sam.bridgeapi.model.FirstLevel;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Product;
import com.pearson.sam.bridgeapi.model.Resource;
import com.pearson.sam.bridgeapi.model.ResourceData;
import com.pearson.sam.bridgeapi.model.SecondLevel;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ResourceBridgeServiceImpl implements IResourceBridgeService {

	private static final Logger logger = LoggerFactory.getLogger(ResourceBridgeServiceImpl.class);

	private final GsonBuilder gsonBuilder = new GsonBuilder();
	private final Gson gson = gsonBuilder.setPrettyPrinting().disableHtmlEscaping().create();

	IResourceService resourceService;
	
	@Autowired
	IProductService productService;
	
	@Autowired
	private ISessionFacade sessionFacade;

	@Override
	public Resource create(Resource resource) {
		//logger.info("Creating Resource {}");
		return resourceService.create(resource);
	}

	@Override
	public Resource update(Resource resource) {
		return resourceService.update(resource);
	}

	@Override
	public Resource fetchOne(Resource resource) {
		//logger.info("Getting Resource {}");
		return resourceService.fetchOne(resource);
	}

	@Override
	public List<Resource> fetchMultiple(KeyType key, List<String> ids) {
		return resourceService.fetchMultiple(key, ids);
	}

	@Override
	public Resource delete(Resource resource) {
		return resourceService.delete(resource);
	}

	@Override
	public Page<Resource> pageIt(Pageable pageable, Example<Resource> e, String productId) {
		//logger.info("Getting paginated Resource {}");
   
		
		List<Resource> resourceList = getResourceList(productId) ;
		Page<Resource> pages = new PageImpl<Resource>(resourceList, pageable, resourceList.size());

		return pages;
	}

	private List<Resource> getResourceList(String productId) {
		List<Resource> resourceList = new ArrayList<>();
		Product product = new Product();
		product.setProductId(productId);
	    product =productService.fetchOne(product);
		List<String> resList = product.getResourceCourseIds();
		resList.remove(product.getProductCode());
	    product.getResourceCourseIds().forEach(r->{
	    	
	    	Resource resource = getResource(r);
	    	resource.setProductId(productId);
	    	resourceList.add(resource);
	    	});
		
		return resourceList;
	}

	private Resource getResource(String courseId) {
		Resource resource = new Resource();
		try {
			User user = getLoggedInUser();
			List<String> schoolIds = user.getSchool();
			String orgId = schoolIds.get(0);
			JSONParser jsonParser = new JSONParser();
			String assetResponseString = gson
					.toJson(productService.getLearningMaterial(orgId, courseId, user.getUid()));
			JSONObject assetResponse = (JSONObject) new JSONParser().parse(assetResponseString);
			JSONArray assetsArray = (JSONArray) assetResponse.get("assetResponse");
			JSONArray resourceArray = getResourceArray(assetsArray);
			//JSONArray resourceArray = (JSONArray) assetResponse.get("assetResponse");

			// Group them based on section

			Map<String, List<JSONObject>> sectionMap = groupWithChapter(resourceArray);

			// Sort Based on Chapter using first element of sorted resources

			Map<String, List<JSONObject>> sortedMap = sortBasedOnChapter(sectionMap);

			// --------end of sorting of chapter

			Content content = new Content();
			List<FirstLevel> firstLevelList = new ArrayList<>();
			sortedMap.keySet().forEach(v -> {

				// SORT with SelectionOrder
				// sortedMap.put(v, sortedList(sortedMap.get(v),
				// "sectionOrder"));
				// GOT sectionMap with each value sorted.

				FirstLevel firtLevel = new FirstLevel();
				List<JSONObject> flrdList = new LinkedList<>();
				firtLevel.setTitle(v);
				// System.out.println("**********" + sectionMap.get(v));

				Map<String, List<JSONObject>> topics = getChildNode(sortedMap.get(v), "section", "order", "topic",
						flrdList);

				topics.keySet().forEach(t -> {
					List<SecondLevel> secLList = new LinkedList<>();
					SecondLevel secL = new SecondLevel();
					secL.setTitle(t);
					List<JSONObject> slrdList = new LinkedList<>();
					List topicList = topics.get(t);

					buildSubTopic(topicList, slrdList);

					secL.setResourceData(convertToResourceData(slrdList));
					secLList.add(secL);

					firtLevel.setSecondLevel(secLList);

				});
				firtLevel.setResourceData(convertToResourceData(flrdList));
				// TOPIC level ENd
				firstLevelList.add(firtLevel);

			});
			content.setFirstLevel(firstLevelList);
			// resource.setFirstLevel(firstLevelList);
			System.out.println("jsonObject***" + sortedMap.size());
			resource.setContent(content);
		} catch (Exception e) {
			//logger.info(e.getMessage());
		}
		return resource;
	}

	private JSONArray getResourceArray(JSONArray assetResArray) {
		JSONArray resources = new JSONArray();
		assetResArray.forEach(assetRes-> {
			JSONObject assetResObj = (JSONObject) assetRes;
			JSONArray assets = (JSONArray) assetResObj.get("assets");
			assets.forEach(asset ->{
				JSONObject assetObj = (JSONObject) asset;
				resources.add(assetObj.get("metadata"));
			});
			
		});
		return resources;
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

	private Map<String, List<JSONObject>> sortBasedOnChapter(Map<String, List<JSONObject>> sectionMap) {
		Map<String, List<JSONObject>> sortedMap = new LinkedHashMap<>();
		Map<Integer, String> sortedChapterMap = new TreeMap<>();

		sectionMap.keySet().forEach(s -> {
			List<JSONObject> objList = sortedList(sectionMap.get(s), "sectionOrder");
			JSONObject obj = objList.get(0);
			sortedChapterMap.put(Integer.valueOf((String) obj.get("sectionOrder")), s);
		});

		sortedChapterMap.keySet().forEach(s -> {
			sortedMap.put(sortedChapterMap.get(s), sectionMap.get(sortedChapterMap.get(s)));
		});
		return sortedMap;
	}

	private Map<String, List<JSONObject>> getChildNode(List<JSONObject> list, String key, String order, String next,
			List flrdList) {
		Map<String, List<JSONObject>> map = new LinkedHashMap<>();
		// Sort Resources based on order
		List<JSONObject> sortedTopicList = sortedList(list, "order");
		sortedTopicList.forEach(l -> {
			JSONObject obj = (JSONObject) l;
			// String section = (String) obj.get(key);
			System.out.println("Chapter : " + obj.get(key) + ", Chapter order: " + obj.get("sectionOrder") + ", Topic: "
					+ obj.get(next));
			if(obj.get("sectionOrder").equals(24)){
				System.out.println();
			}
			JSONObject obj2 = null;
			String name = null;
			if (obj.containsKey("topic")) {
				obj2 = (JSONObject) obj.get(next);
				addResource(obj2, obj);
				name = (String) obj2.get("name");
			} else {
				// Add Resources to chapter if it does't contain topic
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

		System.out.println("-------" + slrdList);
		return null;
	}

	private List<ResourceData> convertToResourceData(List<JSONObject> list) {
		List<ResourceData> convertedList = new LinkedList<>();

		list.forEach(l -> {
			ResourceData data = gson.fromJson(l.toJSONString(), ResourceData.class);
			convertedList.add(data);
		});
		return convertedList;
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

	private JSONObject getResourceObjectTopic(JSONObject toObj, JSONObject fromObj) {

		if (fromObj != null && toObj != null && fromObj.containsKey("resource")) {
			JSONObject resObj = (JSONObject) fromObj.get("resource");
			if (fromObj != null && toObj != null && fromObj.containsKey("resource")) {
				toObj.put("resourceUrl", resObj.get("url"));
				toObj.put("resourceTitle", resObj.get("name"));
				toObj.put("roleAccess", resObj.get("limitByRoles").toString());
				toObj.put("fileType", resObj.get("fileType"));
				toObj.put("contentTypeValue", resObj.get("contentTypeValue"));
				toObj.put("isDownload", resObj.get("isDownloadable"));
				toObj.put("order", resObj.get("order"));
				toObj.put("sectionOrder", resObj.get("sectionOrder"));
			    toObj.put("description",	Optional.ofNullable(resObj.get("description")).get().toString());
				/*JSONObject obj=null;
				try {
					obj = (JSONObject) new JSONParser().parse((String) resObj.get("description"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject newObj = new JSONObject();
				newObj.put("author", obj.get("author"));
				newObj.put("audience", obj.get("audience"));
				newObj.put("freeResource", obj.get("free\\paid").equals("Paid")?false:true);
				toObj.put("description", newObj);*/
			}

		}

		return toObj;
	}

	private JSONObject getResourceObjectChapter(JSONObject toObj, JSONObject fromObj) {

		if (fromObj != null && toObj != null && fromObj.containsKey("url")) {
			toObj.put("resourceUrl", fromObj.get("url"));
			toObj.put("resourceTitle", fromObj.get("name"));
		    toObj.put("roleAccess", fromObj.get("limitByRoles").toString());
			toObj.put("fileType", fromObj.get("fileType").toString());
			toObj.put("contentTypeValue", fromObj.get("contentTypeValue"));
			toObj.put("isDownload", fromObj.get("isDownloadable"));
			toObj.put("order", fromObj.get("order"));
			toObj.put("sectionOrder", fromObj.get("sectionOrder"));
			toObj.put("description",	Optional.ofNullable(fromObj.get("description")).get().toString());
			//toObj.put("description", gson.toJson(fromObj.get("description")));
			//toObj.put("description",fromObj.get("description").toString());
			/*JSONObject obj=null;
			try {
				obj = (JSONObject) new JSONParser().parse((String) fromObj.get("description"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject newObj = new JSONObject();
			newObj.put("author", obj.get("author"));
			newObj.put("audience", obj.get("audience"));
			newObj.put("freeResource", Optional.ofNullable(obj.get("free\\paid")).equals("Paid")?false:true);
			toObj.put("description", newObj);*/
		}
		return toObj;
	}
	
	private User getLoggedInUser() {
		return sessionFacade.getLoggedInUser(true);
	}
}
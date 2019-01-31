package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PAGINATION_GRAPHQL_QUERY_GET_APP_NOTIFICATION;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.ibridgeservice.IAppNotificationBridgeService;
import com.pearson.sam.bridgeapi.model.AppNotification;
import com.pearson.sam.bridgeapi.model.Sort;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
public class AppNotificationResolver {

	private static final Logger logger = LoggerFactory.getLogger(AppNotificationResolver.class);
	
	@Autowired
	IAppNotificationBridgeService appNotificationBridgeService;
	
	/**
	 * getAppNotifications.
	 * 
	 * @return
	 */
	@GraphQLQuery(name = PAGINATION_GRAPHQL_QUERY_GET_APP_NOTIFICATION)
	public Page<AppNotification> getAppNotifications(
			@GraphQLArgument(name = "offset", defaultValue = "0") Integer pageNumber,
			@GraphQLArgument(name = "limit", defaultValue = "15") Integer pageLimit,
			@GraphQLArgument(name = "filter", defaultValue = "{}") AppNotification filter,
			@GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
			@GraphQLArgument(name = "sort", defaultValue = "{\"field\":\"notificationId\",\"order\":\"ASC\"}") Sort sort,
			@GraphQLRootContext AuthContext context) {
		Pageable pageable = PageRequest.of(pageNumber, pageLimit, Direction.valueOf(sort.getOrder().toString()),
				sort.getField());
	    return appNotificationBridgeService.pageIt(pageable,
				Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
	}
	
	@GraphQLMutation
	public AppNotification createAppNotification(@GraphQLArgument(name = "data") AppNotification data) {
		//logger.info("Creating a AppNotification in resolver {}");
		return appNotificationBridgeService.create(data);
	}
	
	@GraphQLMutation
	public AppNotification updateAppNotification(@GraphQLArgument(name = "id") String id, @GraphQLArgument(name = "data") AppNotification data) {
		//logger.info("Update a AppNotification in resolver {}");
		data.setNotificationId(id);
		return appNotificationBridgeService.update(data);
	}

}
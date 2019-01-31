/*package com.pearson.sam.bridgeapi.resolvers;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_ADD_PANEL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_REMOVE_PANEL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_MUTATION_UPDATE_PANEL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_PANEL;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GRAPHQL_QUERY_PANELS;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.PANEL_REPO;

import com.pearson.sam.bridgeapi.config.AuthContext;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.MethodType;
import com.pearson.sam.bridgeapi.model.Panel;
import com.pearson.sam.bridgeapi.model.Sort;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.serviceImpl.AbstractService;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLRootContext;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
public class PanelResolver extends AbstractResolver<Panel> {

  @Autowired
  private ISessionFacade sessionFacade;

  @Autowired
  @Qualifier(PANEL_REPO)
  AbstractService<Panel> abstractService;

  private static final Logger logger = LoggerFactory.getLogger(PanelResolver.class);

  @GraphQLMutation(name = GRAPHQL_MUTATION_ADD_PANEL)
  public Panel addPanel(@GraphQLArgument(name = "data") Panel data,
      @GraphQLRootContext AuthContext context) {
    return callExecute(MethodType.CREATE, data, abstractService);
  }

  @GraphQLMutation(name = GRAPHQL_MUTATION_UPDATE_PANEL)
  public Panel updatePanel(@GraphQLArgument(name = "panelId") String panelId,
      @GraphQLArgument(name = "data") Panel panel) {
    panel.setPanelId(panelId);
    return callExecute(MethodType.UPDATE, panel, abstractService);
  }

  *//**
   * removePanel.
   * 
   * @param data
   *          desc
   * @return
   *//*
  @GraphQLMutation(name = GRAPHQL_MUTATION_REMOVE_PANEL)
  public Panel removePanel(@GraphQLArgument(name = "panelId") String data) {
    Panel panel = new Panel();
    panel.setPanelId(data);
    return callExecute(MethodType.DELETE, panel, abstractService);
  }

  *//**
   * panel.
   * 
   * @param data
   *          desc
   * @return
   *//*
  @GraphQLQuery(name = GRAPHQL_QUERY_PANEL)
  public Panel panel(@GraphQLArgument(name = "panelId") String data) {
    //logger.info("logged in user {} ", sessionFacade.getLoggedInUser(false));
    Panel panel = new Panel();
    panel.setPanelId(data);
    return callExecute(MethodType.FETCH_ONE, panel, abstractService);
  }

  @GraphQLQuery(name = GRAPHQL_QUERY_PANELS)
  public List<Panel> panels(@GraphQLRootContext AuthContext context) {
    return callExecute(abstractService, KeyType.USER_ID, null);
  }

  *//**
   * pageablePanels.
   *//*
  @GraphQLQuery(name = "pageablePanels")
  public Page<Panel> pageablePanels(
      @GraphQLArgument(name = "offset", defaultValue = "0") Integer offset,
      @GraphQLArgument(name = "limit", defaultValue = "15") Integer limit,
      @GraphQLArgument(name = "filter", defaultValue = "{}") Panel filter,
      @GraphQLArgument(name = "filterPattern", defaultValue = "5") StringMatcher sm,
      @GraphQLArgument(name = "sort", 
        defaultValue = "{\"field\":\"panelId\",\"order\":\"ASC\"}") Sort sort,
      @GraphQLRootContext AuthContext context) {
    Pageable pageable = PageRequest.of(offset, limit, Direction.valueOf(sort.getOrder().toString()),
        sort.getField());
    return pageIt(abstractService, pageable,
        Example.of(filter, ExampleMatcher.matching().withStringMatcher(sm)));
  }
}
*/
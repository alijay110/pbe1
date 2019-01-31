package com.pearson.sam.bridgeapi.config;

import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.CURRENT_QUERY;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.GUEST_OPERATIONS;
import static com.pearson.sam.bridgeapi.constants.BridegeApiConstants.REQUEST_ID;
import static com.pearson.sam.bridgeapi.constants.SecurityConstants.ACCESS_TOKEN;
import static com.pearson.sam.bridgeapi.constants.SecurityConstants.HEADER_STRING;
import static com.pearson.sam.bridgeapi.constants.SecurityConstants.SESSION_EMAIL;
import static com.pearson.sam.bridgeapi.constants.SecurityConstants.SESSION_USERID;
import static com.pearson.sam.bridgeapi.constants.SecurityConstants.TOKEN_PREFIX;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.repository.UserRepository;
import com.pearson.sam.bridgeapi.security.GraphqlBridgeApiAuthorizationFilter;
import com.pearson.sam.bridgeapi.util.Utils;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLException;
import graphql.language.Document;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.language.Selection;
import graphql.parser.Parser;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.servlet.ExecutionStrategyProvider;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@SuppressWarnings("serial")
@WebServlet(value = "/graphql")
public class GraphqlEndpoint extends SimpleGraphQLServlet {

  private static final Logger logger = LoggerFactory.getLogger(GraphqlEndpoint.class);

  private final Set<String> guestQueries;

  private final UserRepository userRepo;

  private final GraphqlBridgeApiAuthorizationFilter graphqlAuth;

  /**
   * GraphqlEndpoint constructor.
   * 
   * @param schema
   *          schema
   * @param map
   *          autowired
   */
  public GraphqlEndpoint(GraphQLSchema schema,
      @Autowired @Qualifier(GUEST_OPERATIONS) Set<String> guestQueriesInfo,
      @Autowired UserRepository userRepo,
      @Autowired GraphqlBridgeApiAuthorizationFilter graphqlAuth) {
    super(new Builder(schema).withGraphQLErrorHandler(new BridgeGraphQLErrorHandler()));
    guestQueries = guestQueriesInfo;
    this.userRepo = userRepo;
    this.graphqlAuth = graphqlAuth;
    printAllQueriesAndMutations(schema);
  }

  /*
   * (non-Javadoc)
   * 
   * @see graphql.servlet.SimpleGraphQLServlet#createContext(java.util.Optional, java.util.Optional)
   */
  @Override
  protected GraphQLContext createContext(Optional<HttpServletRequest> request,
      Optional<HttpServletResponse> response) {

    if (request.isPresent()) {
      HttpSession session = request.get().getSession();
      String userId = (String) session.getAttribute(SESSION_USERID);
      String email = (String) session.getAttribute(SESSION_EMAIL);
      String token = (String) session.getAttribute(ACCESS_TOKEN);
      Set<String> permissions = Utils.convert(session.getAttribute("permissions"),
          new TypeReference<Set<String>>() {
          });
      SessionUser sessionUser = new SessionUser(userId, email, null, token, permissions);
      return new AuthContext(sessionUser, request, response);
    }
    return null;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      final GraphQLContext context = createContext(Optional.of(req), Optional.of(resp));
      final Object rootObject = createRootObject(Optional.of(req), Optional.of(resp));
      InputStream inputStream = req.getInputStream();
      if (!inputStream.markSupported()) {
        inputStream = new BufferedInputStream(inputStream);
      }

      GraphQLRequest greq = getGraphqLRequestMapper().readValue(inputStream);
      GraphQLSchema schema = getSchemaProvider().getSchema(req);
      String queryName = StringUtils.EMPTY;
      try {
        queryName = getQueryName(greq.getQuery());
      } catch (GraphQLException ge) {
        log.error(ge.getMessage());
        callSendError(resp, 400, ge.getMessage());
      }
      req.setAttribute(CURRENT_QUERY, queryName);

     // if (isUserAllowedToProceed(greq, req, resp)) {
        doQuery(resp, context, rootObject, greq, schema);
     // } else {
     //   resp.sendError(HttpStatus.SC_UNAUTHORIZED,
     //       "User is not authorised to perform this Operation!!");
        return;
      //}
    } catch (Exception e) {
      log.error("error caught in do post");
      log.error("Error executing GraphQL request!", e);
      callSendError(resp, 500, e.getMessage());
    }
  }

  private SessionUser getLoggedInUser(boolean needFullDetails) {
    Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    SessionUser sessionUser = null;
    if (obj instanceof String) {
      sessionUser = new SessionUser((String) obj, null, Collections.emptySet(), StringUtils.EMPTY,
          Collections.emptySet());
    } else {
      sessionUser = (SessionUser) SecurityContextHolder.getContext().getAuthentication()
          .getPrincipal();
    }

    if (needFullDetails) {
      User user = userRepo.findByIdentifier(sessionUser.getUserId());
      return new SessionUser(sessionUser, user);
    }
    return sessionUser;
  }

  private void printAllQueriesAndMutations(GraphQLSchema schema) {
    logger.info("Mutations: {}",
        ((GraphQLObjectType) schema.getTypeMap().get("Mutation")).getFieldDefinitions().stream()
            .map(GraphQLFieldDefinition::getName).sorted().collect(Collectors.toList()));
    logger.info("Quries: {}",
        ((GraphQLObjectType) schema.getTypeMap().get("Query")).getFieldDefinitions().stream()
            .map(GraphQLFieldDefinition::getName).sorted().collect(Collectors.toList()));
  }

  private void doQuery(HttpServletResponse resp, final GraphQLContext context,
      final Object rootObject, GraphQLRequest greq, GraphQLSchema schema) {
    ExecutionStrategyProvider executionStrategyProvider = getExecutionStrategyProvider();

    final ExecutionResult executionResult = GraphQL.newGraphQL(schema)
        .queryExecutionStrategy(executionStrategyProvider.getQueryExecutionStrategy())
        .mutationExecutionStrategy(executionStrategyProvider.getMutationExecutionStrategy())
        .subscriptionExecutionStrategy(executionStrategyProvider.getSubscriptionExecutionStrategy())
        .instrumentation(getInstrumentation())
        .preparsedDocumentProvider(getPreparsedDocumentProvider()).build()
        .execute(new ExecutionInput(greq.getQuery(), greq.getOperationName(), context, rootObject,
            greq.getVariables()));

    final Map<String, Object> result = new LinkedHashMap<>();
    result.put("data", executionResult.getData());

    if (getGraphQLErrorHandler().errorsPresent(executionResult.getErrors())) {
      result.put("errors", getGraphQLErrorHandler().processErrors(executionResult.getErrors()));
    }

    if (executionResult.getExtensions() != null) {
      result.put("extensions", executionResult.getExtensions());
    }

    resp.setContentType(APPLICATION_JSON_UTF8);
    resp.setStatus(STATUS_OK);
    try {
      resp.getWriter().write(getMapper().writeValueAsString(result));
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  /**
   * Creates an {@link ObjectReader} for deserializing {@link GraphQLRequest}.
   */
  private ObjectReader getGraphqLRequestMapper() {
    InjectableValues.Std injectableValues = new InjectableValues.Std();
    injectableValues.addValue(ObjectMapper.class, getMapper());

    return getMapper().reader(injectableValues).forType(GraphQLRequest.class);
  }

  private boolean skipAuth(String query) {
    logger.info("query {}", query);
    logger.info("guestQueries : {}", guestQueries);
    if (guestQueries.contains(query)) {
      logger.info("NO NEED AUTHENTICATION");
      return true;
    }

    logger.info("NEED AUTHENTICATION");
    return false;
  }

  private String getQueryName(String query) {
    try {
      Document doc = new Parser().parseDocument(query);
      OperationDefinition od = (OperationDefinition) doc.getDefinitions().get(0);
      Selection<?> s = od.getSelectionSet().getSelections().get(0);
      Field f = (Field) s;
      return f.getName();
    } catch (ParseCancellationException pce) {
      log.error("Unable to parse the query, please verify the query:{}", query);
      throw new GraphQLException("Unable to parse the query, please verify the query:" + query);
    }
  }

  /**
   * authenticateIfRequired.
   * 
   * @param graphqlRequest
   *          greq
   * @param httpReq
   *          req
   * @param httpRes
   *          res
   * @throws Exception
   *           e
   */
  private boolean isUserAllowedToProceed(GraphQLRequest graphqlRequest, HttpServletRequest httpReq,
      HttpServletResponse httpRes) {

    addHeaders(httpRes);

    String authHeader = httpReq.getHeader(HEADER_STRING);
    String hack = httpReq.getHeader("usrId");
    boolean authenticated = false;

    if (StringUtils.isNotBlank(authHeader) || StringUtils.isNotBlank(hack)) {
      try {
        authenticated = callAuthentication(httpReq, httpRes, authHeader, hack);
      } catch (IOException e) {
        callSendError(httpRes, 403, "Authentication Error!!!");
      }
    }
    return canExecuteThisQuery(graphqlRequest, httpRes, authenticated);
  }

  private boolean canExecuteThisQuery(GraphQLRequest graphqlRequest, HttpServletResponse httpRes,
      boolean authenticated) {
    boolean needAuth = true;
    boolean authorized = false;
    try {
      String query = getQueryName(graphqlRequest.getQuery());
      needAuth = !skipAuth(query);
      if (needAuth && authenticated) {
        authorized = checkAuthorization(query);
        if (authorized) {
          return authorized;
        } else {
          callSendError(httpRes, HttpStatus.SC_UNAUTHORIZED,
              "User is not authorised to perform this Operation!!");
        }
      } else if (needAuth) {
        callSendError(httpRes, 403, "User need to be authenticated!!!");
      } else {
        return !needAuth;
      }
    } catch (ParseCancellationException e) {
      callSendError(httpRes, 400, "Parsing Error!!!");
    }
    return false;
  }

  /**
   * checkAuthorization.
   * 
   * @return
   */
  private boolean checkAuthorization(String query) {
    SessionUser user = getLoggedInUser(true);
    log.info("user {} \n query {} \nPermissions {}", user, query, user.getPermissions());
    return user.getPermissions().contains(query);
  }

  private boolean callAuthentication(HttpServletRequest httpReq, HttpServletResponse httpRes,
      String authHeader, String hack) throws IOException {
    UsernamePasswordAuthenticationToken authentication = null;

    if (StringUtils.isNotBlank(authHeader)) {
      if (!authHeader.startsWith(TOKEN_PREFIX)) {
        logger.info("Error");
        callSendError(httpRes, 403, "Need Token");
      }
      authentication = graphqlAuth.getAuthentication(httpReq, httpRes, userRepo);
    } else if (StringUtils.isNotBlank(hack)) {
      authentication = graphqlAuth.getMockAuthentication(httpReq);
    }

    if (null != authentication) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
      return true;
    }

    return false;
  }

  /**
   * addHeaders.
   * 
   */
  private void addHeaders(HttpServletResponse res) {
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.setHeader("Access-Control-Allow-Credentials", "true");
    res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    res.setHeader("Access-Control-Max-Age", "3600");
    res.setHeader("Access-Control-Allow-Headers",
        "Content-Type, Accept, X-Requested-With, remember-me, authorization");
  }

  private void callSendError(HttpServletResponse httpRes, int status, String msg) {
    try {
      httpRes.sendError(status, msg);
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    try {
      super.doOptions(req, res);
      addHeaders(res);
    } catch (Exception e) {
      log.error("error caught in do options");
      log.error("Error executing GraphQL request!", e);
      callSendError(res, 500, e.getMessage());
    }

  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    super.doGet(req, resp);
    addHeaders(resp);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setAttribute(REQUEST_ID, System.currentTimeMillis());
    super.service(req, resp);
  }

}

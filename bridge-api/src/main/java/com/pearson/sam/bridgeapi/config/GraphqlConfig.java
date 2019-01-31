package com.pearson.sam.bridgeapi.config;

import com.pearson.sam.bridgeapi.resolvers.AccessCodeHistoryResolver;
import com.pearson.sam.bridgeapi.resolvers.AccessCodeResolver;
import com.pearson.sam.bridgeapi.resolvers.AppNotificationResolver;
import com.pearson.sam.bridgeapi.resolvers.ClassRoomResolver;
import com.pearson.sam.bridgeapi.resolvers.LicenceHistoryResolver;
import com.pearson.sam.bridgeapi.resolvers.LicenceHolderHistoryResolver;
import com.pearson.sam.bridgeapi.resolvers.LicenceHolderResolver;
import com.pearson.sam.bridgeapi.resolvers.LicenceResolver;
import com.pearson.sam.bridgeapi.resolvers.LogoutResolver;
import com.pearson.sam.bridgeapi.resolvers.OrganisationResolver;
//import com.pearson.sam.bridgeapi.resolvers.PanelResolver;
import com.pearson.sam.bridgeapi.resolvers.ProductResolver;
import com.pearson.sam.bridgeapi.resolvers.ResourceResolver;
import com.pearson.sam.bridgeapi.resolvers.SchoolResolver;
import com.pearson.sam.bridgeapi.resolvers.SubscriptionResolver;
import com.pearson.sam.bridgeapi.resolvers.UserResolver;
import com.pearson.sam.bridgeapi.resolvers.ViewerResolver;
import com.pearson.sam.bridgeapi.resolvers.VoucherHistoryResolver;
import com.pearson.sam.bridgeapi.resolvers.VoucherResolver;

import graphql.schema.GraphQLSchema;

import io.leangen.graphql.GraphQLSchemaGenerator;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = { "Products/${product}/Env/${env}/sam.properties","SamEndPoints.properties" })
public class GraphqlConfig {

	@Autowired
	private LogoutResolver logoutResolver;

	@Autowired
	private UserResolver userResolver;

	@Autowired
	private SchoolResolver schoolResolver;

	@Autowired
	private ClassRoomResolver classRoomResolver;

	/*@Autowired
	private PanelResolver panelResolver;*/

	@Autowired
	private ProductResolver productResolver;

	@Autowired
	private ViewerResolver viewerResolver;

	@Autowired
	private LicenceResolver licenceResolver;

	@Autowired
	private AccessCodeResolver accessCodeResolver;

	@Autowired
	private VoucherResolver voucherResolver;

	@Autowired
	private LicenceHolderHistoryResolver licenceHolderHistoryResolver;

	@Autowired
	private LicenceHolderResolver licenceHolderResolver;

	@Autowired
	private SubscriptionResolver subscriptionResolver;

	@Autowired
	private AccessCodeHistoryResolver accessCodeHistoryResolver;
	
	@Autowired
	private VoucherHistoryResolver voucherHistoryResolver;
	
	@Autowired
	private OrganisationResolver organisationResolver;
	
	@Autowired
	private AppNotificationResolver appNotificationResolver;
	
	@Autowired
	private ResourceResolver resourceResolver;
	
	@Autowired
	private LicenceHistoryResolver licenceHistoryResolver;

	/**
	 * Schema generation logic.
	 * 
	 * @returns new {@link GraphQLSchema}
	 * @throws IOException
	 * 
	 */
	@Bean
	public GraphQLSchema schema() {
		return new GraphQLSchemaGenerator()
				.withBasePackages("com.pearson.sam.bridgeapi.model", "org.springframework.data.domain")
				.withOperationsFromSingletons(userResolver, schoolResolver, voucherResolver, logoutResolver,
						classRoomResolver, /*panelResolver,*/ productResolver, viewerResolver, licenceResolver,
						accessCodeResolver, licenceHolderHistoryResolver, licenceHolderResolver, subscriptionResolver,
						accessCodeHistoryResolver,voucherHistoryResolver,organisationResolver,appNotificationResolver,
						resourceResolver,licenceHistoryResolver)
				.generate();
	}

}

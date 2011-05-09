package org.broadleafcommerce.gwt.client.datasource.catalog.product.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.broadleafcommerce.gwt.client.Main;
import org.broadleafcommerce.gwt.client.datasource.dynamic.module.BasicEntityModule;
import org.broadleafcommerce.gwt.client.datasource.dynamic.operation.EntityOperationType;
import org.broadleafcommerce.gwt.client.datasource.dynamic.operation.EntityServiceAsyncCallback;
import org.broadleafcommerce.gwt.client.datasource.relations.PersistencePerspective;
import org.broadleafcommerce.gwt.client.datasource.results.ClassMetadata;
import org.broadleafcommerce.gwt.client.datasource.results.DynamicResultSet;
import org.broadleafcommerce.gwt.client.datasource.results.Entity;
import org.broadleafcommerce.gwt.client.datasource.results.MergedPropertyType;
import org.broadleafcommerce.gwt.client.datasource.results.PolymorphicEntity;
import org.broadleafcommerce.gwt.client.datasource.results.Property;
import org.broadleafcommerce.gwt.client.service.AbstractCallback;
import org.broadleafcommerce.gwt.client.service.AppServices;
import org.broadleafcommerce.gwt.client.service.DynamicEntityServiceAsync;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ProductSkuBasicEntityModule extends BasicEntityModule {

	/**
	 * @param ceilingEntityFullyQualifiedClassname
	 * @param persistencePerspective
	 * @param service
	 */
	public ProductSkuBasicEntityModule(String ceilingEntityFullyQualifiedClassname, PersistencePerspective persistencePerspective, DynamicEntityServiceAsync service) {
		super(ceilingEntityFullyQualifiedClassname, persistencePerspective, service);
	}

	@Override
	public void executeAdd(final String requestId, DSRequest request, final DSResponse response, String[] customCriteria, final AsyncCallback<DataSource> cb) {
		Main.NON_MODAL_PROGRESS.startProgress();
		JavaScriptObject data = request.getData();
        TreeNode record = new TreeNode(data);
        Entity entity = buildEntity(record);
        
        List<Property> newPropList = new ArrayList<Property>();
        {
	        Property myProp = entity.findProperty("name");
	        if (myProp != null) {
	        	Property temp = new Property();
	        	temp.setName("sku.name");
	        	temp.setValue(myProp.getValue());
	        	newPropList.add(temp);
	        }
        }
        {
	        Property myProp = entity.findProperty("activeStartDate");
	        if (myProp != null) {
	        	Property temp = new Property();
	        	temp.setName("sku.activeStartDate");
	        	temp.setValue(myProp.getValue());
	        	newPropList.add(temp);
	        }
        }
        {
	        Property myProp = entity.findProperty("activeEndDate");
	        if (myProp != null) {
	        	Property temp = new Property();
	        	temp.setName("sku.activeEndDate");
	        	temp.setValue(myProp.getValue());
	        	newPropList.add(temp);
	        }
        }
        {
	        Property myProp = entity.findProperty("description");
	        if (myProp != null) {
	        	Property temp = new Property();
	        	temp.setName("sku.description");
	        	temp.setValue(myProp.getValue());
	        	newPropList.add(temp);
	        }
        }
        {
	        Property myProp = entity.findProperty("longDescription");
	        if (myProp != null) {
	        	Property temp = new Property();
	        	temp.setName("sku.longDescription");
	        	temp.setValue(myProp.getValue());
	        	newPropList.add(temp);
	        }
        }
        newPropList.addAll(Arrays.asList(entity.getProperties()));
        Property[] newProps = new Property[newPropList.size()];
        newProps = newPropList.toArray(newProps);
        entity.setProperties(newProps);
        
        service.add(ceilingEntityFullyQualifiedClassname, entity, persistencePerspective, customCriteria, new EntityServiceAsyncCallback<Entity>(EntityOperationType.ADD, requestId, request, response, dataSource) {
			public void onSuccess(Entity result) {
				super.onSuccess(result);
				TreeNode record = (TreeNode) buildRecord(result, false);
				TreeNode[] recordList = new TreeNode[]{record};
				response.setData(recordList);
				if (cb != null) {
					cb.onSuccess(dataSource);
				}
				dataSource.processResponse(requestId, response);
			}
		});
	}

	@Override
	public void buildFields(String[] customCriteria, final AsyncCallback<DataSource> cb) {
		AppServices.DYNAMIC_ENTITY.inspect(ceilingEntityFullyQualifiedClassname, persistencePerspective, customCriteria, metadataOverrides, new AbstractCallback<DynamicResultSet>() {
			public void onSuccess(DynamicResultSet result) {
				super.onSuccess(result);
				ClassMetadata metadata = result.getClassMetaData();
				filterProperties(metadata, new MergedPropertyType[]{MergedPropertyType.PRIMARY, MergedPropertyType.JOINSTRUCTURE});
				
				//Add a hidden field to store the polymorphic type for this entity
				DataSourceField typeField = new DataSourceTextField("type");
				typeField.setCanEdit(false);
				typeField.setHidden(true);
				typeField.setAttribute("permanentlyHidden", true);
				dataSource.addField(typeField);
				
				dataSource.getField("sku.name").setHidden(true);
				dataSource.getField("sku.name").setAttribute("permanentlyHidden", true);
				dataSource.getField("sku.activeStartDate").setHidden(true);
				dataSource.getField("sku.activeStartDate").setAttribute("permanentlyHidden", true);
				dataSource.getField("sku.activeEndDate").setHidden(true);
				dataSource.getField("sku.activeEndDate").setAttribute("permanentlyHidden", true);
				dataSource.getField("sku.description").setHidden(true);
				dataSource.getField("sku.description").setAttribute("permanentlyHidden", true);
				dataSource.getField("sku.longDescription").setHidden(true);
				dataSource.getField("sku.longDescription").setAttribute("permanentlyHidden", true);
				
				for (PolymorphicEntity polymorphicEntity : metadata.getPolymorphicEntities()){
					String name = polymorphicEntity.getName();
					String type = polymorphicEntity.getType();
					dataSource.getPolymorphicEntities().put(type, name);
				}
				dataSource.setDefaultNewEntityFullyQualifiedClassname(dataSource.getPolymorphicEntities().keySet().iterator().next());
				
				cb.onSuccess(dataSource);
			}
			
		});
	}

	
}

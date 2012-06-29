package de.benjaminborbe.authorization.permission;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.inject.Provider;

import de.benjaminborbe.authorization.api.PermissionIdentifier;

public class PermissionBeanMapperUnitTest {

	@Test
	public void testMapToObject() throws Exception {
		final Provider<PermissionBean> permissionBeanProvider = new Provider<PermissionBean>() {

			@Override
			public PermissionBean get() {
				return new PermissionBean();
			}
		};
		final PermissionBeanMapper mapper = new PermissionBeanMapper(permissionBeanProvider);
		final String permissionName = "myPermission";
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put("id", permissionName);
			final PermissionBean permissionBean = mapper.map(data);
			assertEquals(permissionName, permissionBean.getId().getId());
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			final PermissionBean permissionBean = mapper.map(data);
			assertNull(permissionBean.getId());
		}
	}

	@Test
	public void testObjectToMap() throws Exception {
		final Provider<PermissionBean> permissionBeanProvider = new Provider<PermissionBean>() {

			@Override
			public PermissionBean get() {
				return new PermissionBean();
			}
		};
		final PermissionBeanMapper mapper = new PermissionBeanMapper(permissionBeanProvider);
		final String permissionName = "myPermission";
		{
			final PermissionBean permissionBean = permissionBeanProvider.get();
			permissionBean.setId(new PermissionIdentifier(permissionName));
			final Map<String, String> data = mapper.map(permissionBean);
			assertEquals(permissionName, data.get("id"));
		}
		{
			final PermissionBean permissionBean = permissionBeanProvider.get();
			final Map<String, String> data = mapper.map(permissionBean);
			assertNull(data.get("id"));
		}
	}
}

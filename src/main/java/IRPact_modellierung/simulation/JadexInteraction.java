package IRPact_modellierung.simulation;

import jadex.commons.future.IFuture;
import jadex.application.IEnvironmentService;
import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.ITuple2Future;
import jadex.extension.envsupport.environment.space2d.ContinuousSpace2D;

import java.util.Map;

public class JadexInteraction {

	private IComponentManagementService componentManagementService;
	private IExternalAccess platform;
	private static ContinuousSpace2D jadexSpace;
	private CreationInfo creationParent;

	public JadexInteraction() {
		platform = startPlatform();
		componentManagementService = startCMS(platform);
		creationParent = setupSpaceModel(componentManagementService, platform);
	}

	public IComponentManagementService getComponentManagementService() {
		return componentManagementService;
	}

	public IExternalAccess getPlatform() {
		return platform;
	}

	public static ContinuousSpace2D getJadexSpace() {
		return jadexSpace;
	}

	public CreationInfo getCreationParent() {
		return creationParent;
	}

	/**
	 * Starts the platform the jadex components run on
	 *
	 * @return An access for the platform
	 */
	private static IExternalAccess startPlatform(){
		final String[] defargs = new String[] { "-gui", "false", "-welcome", "false", "-cli", "false", "-printpass", "false" }; // return
		final IFuture<IExternalAccess> platfut = Starter.createPlatform(defargs);
		return platfut.get();
	}

	private static IComponentManagementService startCMS(IExternalAccess platform) {
		return SServiceProvider.getService(platform, IComponentManagementService.class, RequiredServiceInfo.SCOPE_PLATFORM).get();
	}

	private CreationInfo setupSpaceModel(IComponentManagementService cms, IExternalAccess platform) {
		//assign the information of the parent element (platform)
		final CreationInfo creationParent = new CreationInfo(platform.getComponentIdentifier());
		final ITuple2Future<IComponentIdentifier, Map<String, Object>> createComponent = cms.createComponent("Irpact_modellierung/innovationsdiffusion/Diffusion.application.xml", creationParent);
		createComponent.getFirstResult(); // Warte auf Space-Erstellung
		final IEnvironmentService spaceService = SServiceProvider.getService(platform, IEnvironmentService.class, RequiredServiceInfo.SCOPE_PLATFORM).get();
		jadexSpace = (ContinuousSpace2D) spaceService.getSpace("gc2dspace").get();
		return creationParent;
	}

}
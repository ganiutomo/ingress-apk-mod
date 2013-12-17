package broot.ingress.mod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import broot.ingress.mod.BuildConfig.UiVariant;
import broot.ingress.mod.util.Config;
import broot.ingress.mod.util.Config.ChatTimeFormat;
import broot.ingress.mod.util.Config.GpsLockTime;
import broot.ingress.mod.util.Config.Pref;
import broot.ingress.mod.util.InventoryUtils;
import broot.ingress.mod.util.MenuUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.SnapshotArray;
import com.esotericsoftware.tablelayout.Cell;
import com.nianticproject.ingress.NemesisActivity;
import com.nianticproject.ingress.NemesisApplication;
import com.nianticproject.ingress.common.ComponentManager;
import com.nianticproject.ingress.common.app.NemesisMemoryCache;
import com.nianticproject.ingress.common.app.NemesisMemoryCacheFactory;
import com.nianticproject.ingress.common.app.NemesisWorld;
import com.nianticproject.ingress.common.assets.AssetFinder;
import com.nianticproject.ingress.common.inventory.MenuControllerImpl;
import com.nianticproject.ingress.common.scanner.ScannerActivity;
import com.nianticproject.ingress.common.scanner.ScannerStateManager;
import com.nianticproject.ingress.common.ui.BaseSubActivity;
import com.nianticproject.ingress.common.ui.FormatUtils;
import com.nianticproject.ingress.common.ui.elements.PortalInfoDialog;
import com.nianticproject.ingress.common.ui.elements.AvatarPlayerStatusBar;
import com.nianticproject.ingress.common.ui.widget.MenuTabId;
import com.nianticproject.ingress.gameentity.components.LocationE6;
import com.nianticproject.ingress.shared.ClientType;
import com.nianticproject.ingress.shared.location.LocationUtils;

public class Entry {

	private static Label portalInfoDistLabel;

	static {
		Mod.init();
	}

	public static FileHandle AssetFinder_onGetAssetPath(final String in) {
		if (!in.startsWith("{data:")) {
			return null;
		}
		final int pos1 = in.indexOf("/data/", 6);
		final int pos2 = in.indexOf(",", pos1 + 6);
		final String pre = in.substring(6, pos1) + "/";
		final String post = "/" + in.substring(pos1 + 6, pos2);

		UiVariant variant = Mod.currUiVariant;
		final FileHandle file = Gdx.files.internal(pre + variant.getName() + post);
		if (file.exists()) {
			return file;
		}
		variant = UiVariant.valueOf(variant.getParent());
		return null;
	}

	public static void AssetFinder_onInit(final AssetFinder assetFinder) {
		Mod.assetFinder = assetFinder;
		Mod.updateCurrUiVariant();
	}

	public static SimpleDateFormat CommsAdapter_getDateFormat() {
		final ChatTimeFormat f = Config.getEnumValue(Pref.ChatTimeFormat);
		return f.getFormat();
	}

	public static ClientType getClientType() {
		return ClientType.DEVELOPMENT;
	}

	public static ClientType getClientTypeForJackson() {
		return ClientType.PRODUCTION;
	}

	public static long GpsSensor_lockTimeout() {
		final GpsLockTime t = Config.getEnumValue(Pref.GpsLockTime);
		return t.getLockTime();
	}

	public static float HackAnimationStage_getTotalTime(final float orig) {
		return Config.getBoolean(Pref.HackAnimEnabled) ? orig : 0;
	}

	public static boolean HackController_shouldShowAnimation() {
		return Config.getBoolean(Pref.HackAnimEnabled);
	}

	public static boolean InventoryItemRenderer_shouldRotate() {
		return Config.getBoolean(Pref.RotateInventoryItemsEnabled);
	}

	public static boolean InventoryItemRenderer_simplifyItems() {
		return Config.getBoolean(Pref.SimplifyInventoryItems);
	}

	public static boolean isInviteNagBlockEnabled() {
		return Config.getBoolean(Pref.NeedInviteNagBlock);
	}

	public static boolean isPrivacyEnabled() {
		return Config.getBoolean(Pref.IsPrivacyOn);
	}

	public static boolean ItemActionHandler_recycleAnimationsEnabled() {
		return Config.getBoolean(Pref.RecycleAnimationsEnabled);
	}

	public static void MenuController_onInit(final MenuControllerImpl menuController) {
		Mod.menuController = menuController;
	}

	public static void MenuControllerImpl_onSelectTab(MenuControllerImpl controller, final MenuTabId tabId) {
		controller.subActivityManager.replaceForegroundActivity(MenuUtils.getActivityClassForMenuTabId(tabId));
	}

	public static Class<?> MenuShowBtn_onClick() {
		return MenuUtils.getActivityClassForMenuTabId(MenuUtils.getTabs()[0]);
	}

	public static String MenuTabId_onToString(final MenuTabId tab) {
		switch (tab) {
		case MOD_ABOUT:
			return "[MOD]";
		case MOD_ITEMS:
			return "[ITEMS]";
		}
		return null;
	}

	public static MenuTabId[] MenuTopWidget_getTabs() {
		return MenuUtils.getTabs();
	}

	public static boolean Mod_ShowAgentTab() {
		return Config.getBoolean(Pref.ShowAgentTab);
	}

	public static void NemesisActivity_onOnCreate(final NemesisActivity activity) {
		PowerManager pm;
		Mod.nemesisActivity = activity;
		Mod.updateFullscreenMode();
		pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
		Mod.ksoWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "Ingress - Keep Screen ON");
		Mod.updateKeepScreenOn();
	}

	public static void NemesisActivity_onOnPause(final NemesisActivity activity) {
		if (Config.getBoolean(Pref.KeepScreenOn)) {
			if (Mod.ksoWakeLock.isHeld()) {
				Mod.ksoWakeLock.release();
			}
		}
    }

    public static void ScannerActivity_onUpdateState(ScannerActivity activity) {
//        if((System.currentTimeMillis() - Mod.lastTap > 1000*5) && Mod.statusBarIsVisible) {
//            Mod.avatarPlayerStatusBar.stage.getRoot().clear();
//            Mod.statusBarIsVisible = false;
//        }
    }

    public static void AvatarPlayerStatusBar_onStageAddActor(Actor actor) {
    }

    public static void AvatarPlayerStatusBar_onCreateUi(AvatarPlayerStatusBar avatarPlayerStatusBar) {
        Mod.avatarPlayerStatusBar = avatarPlayerStatusBar;
        Mod.lastTap = System.currentTimeMillis();
    }

    public static void AvatarPlayerStatusBar_onCreatedUi(AvatarPlayerStatusBar avatarPlayerStatusBar) {
    }

    public static void ScannerTouchHandler_onTouchDown(float x, float y, int z) {
        if(!Mod.statusBarIsVisible) {
            Mod.avatarPlayerStatusBar.createUi(Mod.avatarPlayerStatusBar.skin, Mod.avatarPlayerStatusBar.stage);
            Mod.lastTap = System.currentTimeMillis();
            Mod.statusBarIsVisible = true;
        }
	}

	public static void NemesisActivity_onOnResume(final NemesisActivity activity) {
		if (Config.getBoolean(Pref.KeepScreenOn)) {
			if (!Mod.ksoWakeLock.isHeld()) {
				Mod.ksoWakeLock.acquire();
			}
		}
	}

	public static void NemesisApp_onOnCreate(final Application app) {
		Mod.app = app;
		Config.load();
		Mod.displayMetrics = new DisplayMetrics();
		((WindowManager) app.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
		        Mod.displayMetrics);
	}

	public static void NemesisWorld_onInit(final NemesisWorld world) {
		Mod.world = world;
	}

	public static float ParticleEnergyGlobVisuals_getTimeSec(final float orig) {
		return Config.getBoolean(Pref.XmFlowEnabled) ? orig : 0;
	}

	public static float PortalInfoDialog_getOpenDelay(final float orig) {
		return Config.getBoolean(Pref.ScannerZoomInAnimEnabled) ? orig : 0;
	}

	public static void PortalInfoDialog_onPlayerLocationChanged() {
		if (!Config.getBoolean(Pref.ChangePortalInfoDialog)) {
			return;
		}
		final double dist = LocationUtils.calculateDistance(ComponentManager.getPlayerModel().getPlayerLocation().getLatLng(),
		        ((LocationE6) Mod.portalInfoDialog.portalComponent.getEntity().getComponent(LocationE6.class))
		                .getLatLng());
		portalInfoDistLabel.setText(FormatUtils.formatDistance((float) dist));
	}

	public static void PortalInfoDialog_onStatsTableCreated(final PortalInfoDialog dialog, final Table t) {
		Mod.portalInfoDialog = dialog;

		if (!Config.getBoolean(Pref.ChangePortalInfoDialog)) {
			return;
		}

		final Label.LabelStyle style = Mod.skin.get("portal-stats", Label.LabelStyle.class);
		final Label.LabelStyle keyExistsStyle = Mod.skin.get("ops-title", Label.LabelStyle.class);

		final List<Cell> cells = new ArrayList<Cell>(t.getCells());
		t.clear();
		t.add((Actor) cells.get(0).getWidget()).left();
		t.add((Actor) cells.get(1).getWidget()).left().expandX();
		t.row();
		t.add((Actor) cells.get(3).getWidget()).left();
		t.add((Actor) cells.get(4).getWidget()).left().expandX();
		t.row();
		final int keys = InventoryUtils.getNumberOfPortalKeys(dialog.portalComponent);
		t.add(new Label("Keys:", keys > 0 ? keyExistsStyle : style)).left();
		t.add(new Label(String.valueOf(keys), keys > 0 ? keyExistsStyle : style)).left().expandX();
		t.row();
		t.add(new Label("Dist.:", style)).left();
		t.add(portalInfoDistLabel = new Label("", style)).left().expandX();
	}

	public static void PowerCubeDetailsUiCreator_onActionButtonsTableCreated(final Table t) {
		if (Config.getBoolean(Pref.EnablePowerCubesRecycle)) {
			return;
		}

		final List<Cell> cells = new ArrayList<Cell>(t.getCells());
		t.clear();
		t.add((Actor) cells.get(0).getWidget())
		        .left()
		        .size(com.esotericsoftware.tablelayout.Value.percentWidth(0.29F),
		                com.esotericsoftware.tablelayout.Value.percentWidth(0.12F));
		t.add((Actor) cells.get(1).getWidget())
		        .left()
		        .size(com.esotericsoftware.tablelayout.Value.percentWidth(0.29F),
		                com.esotericsoftware.tablelayout.Value.percentWidth(0.12F));
		t.row();
	}

	public static void ScannerStateManager_onInit(ScannerStateManager instance) {
		Mod.scannerStateManager = instance;
	}

	public static boolean ShouldShowPortalVectors() {
		return Config.getBoolean(Pref.ShowPortalVectors);
	}

	public static boolean ScannerTouchHandler_shouldSwapTouchMenuButtons() {
		return Config.getBoolean(Pref.SwapTouchMenuButtons);
	}

	public static ShaderProgram ShaderUtils_compileShader(final String vertex, final String frag, final String name) {
		return new ShaderProgram(vertex, frag);
	}

	public static float ShieldShader_getRampTargetInvWidthX(final float orig) {
		return Config.getBoolean(Pref.ShieldAnimEnabled) ? orig : 0;
	}

	public static float ShieldShader_getRampTargetInvWidthY(final float orig) {
		return Config.getBoolean(Pref.ShieldAnimEnabled) ? orig : 1;
	}

	public static boolean shouldDrawScannerObject() {
		return Config.getBoolean(Pref.ScannerObjectsEnabled);
	}

	public static boolean shouldSkipIntro() {
		return false;
	}

	// At this point most stuff should be already initialized
	public static void SubActivityApplicationLisener_onCreated() {
		Mod.cache = (NemesisMemoryCache) NemesisMemoryCacheFactory.getCache();
		Mod.skin = ComponentManager.getSubActivityManager().skin;
	}

	public static void SubActivityManager_onInit(final List<BaseSubActivity> activities) {
		activities.add(new AboutModActivity());
		activities.add(new ModItemsActivity());
	}

	public static boolean vibrationEnabled() {
		return Config.getBoolean(Pref.Vibration);
	}

	public static boolean ZoomInMode_shouldZoomIn() {
		return Config.getBoolean(Pref.ScannerZoomInAnimEnabled);
	}
}

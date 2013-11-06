package broot.ingress.mod.util;

import java.util.ArrayList;
import java.util.List;

import broot.ingress.mod.AboutModActivity;
import broot.ingress.mod.ModItemsActivity;
import broot.ingress.mod.util.Config.ItemsTab;
import broot.ingress.mod.util.Config.Pref;

import com.nianticproject.ingress.common.device.DeviceActivity;
import com.nianticproject.ingress.common.intel.IntelActivity;
import com.nianticproject.ingress.common.inventory.ItemsActivity;
import com.nianticproject.ingress.common.mission.MissionListActivity;
import com.nianticproject.ingress.common.passcode.PasscodeTabActivity;
import com.nianticproject.ingress.common.playerprofile.MyProfileActivity;
import com.nianticproject.ingress.common.recruit.RecruitActivity;
import com.nianticproject.ingress.common.ui.widget.MenuTabId;

public class MenuUtils {

	public static Class<?> getActivityClassForMenuTabId(final MenuTabId tab) {
		switch (tab) {
		case MOD_ITEMS:
			return ModItemsActivity.class;
		case INVENTORY:
			return ItemsActivity.class;
		case AGENT:
			return MyProfileActivity.class;
		case MISSIONS:
			return MissionListActivity.class;
		case INTEL:
			return IntelActivity.class;
		case RECRUIT:
			return RecruitActivity.class;
		case PASSCODE:
			return PasscodeTabActivity.class;
		case DEVICE:
			return DeviceActivity.class;
		case MOD_ABOUT:
			return AboutModActivity.class;
		default:
			throw new RuntimeException();
		}
	}

	public static MenuTabId[] getTabs() {
		final List<MenuTabId> tabs = new ArrayList<MenuTabId>();
		final ItemsTab tab = Config.getEnumValue(Pref.ItemsTab);
		if (tab == Config.ItemsTab.AT_START) {
			tabs.add(MenuTabId.MOD_ITEMS);
		}
		if (Config.getBoolean(Pref.ShowOrigItemsTab)) {
			tabs.add(MenuTabId.INVENTORY);
		}
		if (Config.getBoolean(Pref.ShowAgentTab)) {
			tabs.add(MenuTabId.AGENT);
		}
		if (Config.getBoolean(Pref.ShowMissionTab)) {
			tabs.add(MenuTabId.MISSIONS);
		}
		if (Config.getBoolean(Pref.ShowIntelTab)) {
			tabs.add(MenuTabId.INTEL);
		}
		if (Config.getBoolean(Pref.ShowRecruitTab)) {
			tabs.add(MenuTabId.RECRUIT);
		}
		if (Config.getBoolean(Pref.ShowPasscodeTab)) {
			tabs.add(MenuTabId.PASSCODE);
		}
		if (Config.getBoolean(Pref.ShowDeviceTab)) {
			tabs.add(MenuTabId.DEVICE);
		}
		if (tab == Config.ItemsTab.AT_END) {
			tabs.add(MenuTabId.MOD_ITEMS);
		}
		tabs.add(MenuTabId.MOD_ABOUT);
		return tabs.toArray(new MenuTabId[tabs.size()]);
	}
}

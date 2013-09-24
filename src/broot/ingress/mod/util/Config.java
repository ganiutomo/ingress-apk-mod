package broot.ingress.mod.util;

import android.content.SharedPreferences;
import broot.ingress.mod.Mod;

import java.util.List;

public class Config {

    public static boolean swapTouchMenuButtons;

    public static ItemsTab itemsTab;
    public static boolean showOrigItemsTab;
    public static boolean showAgentTab;
    public static boolean showIntelTab;
    public static boolean showMissionTab;
    public static boolean showRecruitTab;
    public static boolean showPasscodeTab;
    public static boolean showDeviceTab;

    public static boolean skipIntro;
    public static boolean scannerZoomInAnimEnabled;
    public static boolean hackAnimEnabled;
    public static boolean rotateInventoryItemsEnabled;
    public static boolean recycleAnimationsEnabled;
    public static boolean xmFlowEnabled;
    public static boolean shieldAnimEnabled;

    public static boolean fullscreen;
    public static boolean showPortalVectors;
    public static boolean portalParticlesEnabled;
    public static boolean xmGlobsEnabled;
    public static boolean scannerObjectsEnabled;
    public static boolean simplifyInventoryItems;
    public static int chatTimeFormat;
    public static int gpsLockTime;
    public static boolean vibration;
    public static boolean keepScreenOn;
    public static boolean changePortalInfoDialog;

    public static UiVariant uiVariant;

    public static void load() {
        SharedPreferences prefs = Mod.app.getSharedPreferences("mod", 0);

        swapTouchMenuButtons = prefs.getBoolean("swapTouchMenuButtons", false);

        itemsTab = ItemsTab.valueOf(prefs.getString("itemsTab", "HIDDEN"));
        showOrigItemsTab = prefs.getBoolean("showOrigItemsTab", true);
        showAgentTab = prefs.getBoolean("showAgentTab", true);
        showIntelTab = prefs.getBoolean("showIntelTab", true);
        showMissionTab = prefs.getBoolean("showMissionTab", true);
        showRecruitTab = prefs.getBoolean("showRecruitTab", true);
        showPasscodeTab = prefs.getBoolean("showPasscodeTab", true);
        showDeviceTab = prefs.getBoolean("showDeviceTab", true);

        skipIntro = prefs.getBoolean("skipIntro", false);
        scannerZoomInAnimEnabled = prefs.getBoolean("scannerZoomInAnimEnabled", true);
        hackAnimEnabled = prefs.getBoolean("hackAnimEnabled", true);
        rotateInventoryItemsEnabled = prefs.getBoolean("rotateInventoryItemsEnabled", true);
        recycleAnimationsEnabled = prefs.getBoolean("recycleAnimationsEnabled", true);
        xmFlowEnabled = prefs.getBoolean("xmFlowEnabled", true);
        shieldAnimEnabled = prefs.getBoolean("shieldAnimEnabled", true);

        fullscreen = prefs.getBoolean("fullscreen", false);
        showPortalVectors = prefs.getBoolean("showPortalVectors", true);
        portalParticlesEnabled = prefs.getBoolean("portalParticlesEnabled", true);
        xmGlobsEnabled = prefs.getBoolean("xmGlobsEnabled", true);
        scannerObjectsEnabled = prefs.getBoolean("scannerObjectsEnabled", true);
        simplifyInventoryItems = prefs.getBoolean("simplifyInventoryItems", false);
        gpsLockTime = prefs.getInt("gpsLockTime", 120000);
        chatTimeFormat = prefs.getInt("chatTimeFormat", 0);
        vibration = prefs.getBoolean("vibration", true);
        keepScreenOn = prefs.getBoolean("keepScreenOn", false);
        changePortalInfoDialog = prefs.getBoolean("changePortalInfoDialog", true);

        uiVariant = UiVariant.byName.get(prefs.getString("uiVariant", "auto"));
        if (uiVariant == null) {
            uiVariant = UiVariant.AUTO;
        }

        Mod.onConfigLoaded();
    }

    public static void save() {
        SharedPreferences.Editor e = Mod.app.getSharedPreferences("mod", 0).edit();

        e.putBoolean("swapTouchMenuButtons", swapTouchMenuButtons);

        e.putString("itemsTab", itemsTab.toString());
        e.putBoolean("showOrigItemsTab", showOrigItemsTab);
        e.putBoolean("showAgentTab", showAgentTab);
        e.putBoolean("showIntelTab", showIntelTab);
        e.putBoolean("showMissionTab", showMissionTab);
        e.putBoolean("showRecruitTab", showRecruitTab);
        e.putBoolean("showPasscodeTab", showPasscodeTab);
        e.putBoolean("showDeviceTab", showDeviceTab);

        e.putBoolean("skipIntro", skipIntro);
        e.putBoolean("hackAnimEnabled", hackAnimEnabled);
        e.putBoolean("scannerZoomInAnimEnabled", scannerZoomInAnimEnabled);
        e.putBoolean("rotateInventoryItemsEnabled", rotateInventoryItemsEnabled);
        e.putBoolean("recycleAnimationsEnabled", recycleAnimationsEnabled);
        e.putBoolean("xmFlowEnabled", xmFlowEnabled);
        e.putBoolean("shieldAnimEnabled", shieldAnimEnabled);

        e.putBoolean("fullscreen", fullscreen);
        e.putBoolean("showPortalVectors", showPortalVectors);
        e.putBoolean("portalParticlesEnabled", portalParticlesEnabled);
        e.putBoolean("xmGlobsEnabled", xmGlobsEnabled);
        e.putBoolean("scannerObjectsEnabled", scannerObjectsEnabled);
        e.putBoolean("simplifyInventoryItems", simplifyInventoryItems);
        e.putInt("gpsLockTime", gpsLockTime);
        e.putInt("chatTimeFormat", chatTimeFormat);
        e.putBoolean("vibration", vibration);
        e.putBoolean("keepScreenOn", keepScreenOn);
        e.putBoolean("changePortalInfoDialog", changePortalInfoDialog);

        e.putString("uiVariant", uiVariant.name);

        e.commit();
    }

    public static void nextItemsTab() {
        itemsTab = ItemsTab.values()[(itemsTab.ordinal() + 1) % ItemsTab.values().length];
        save();
    }
    
    public static void nextUiVariant() {
        List<UiVariant> variants = UiVariant.variants;
        uiVariant = variants.get((variants.indexOf(uiVariant) + 1) % variants.size());
        save();
    }

    public static void nextGpsLockTime() {
        switch (gpsLockTime) {
            case 0: gpsLockTime = 30000; break;
            case 30000: gpsLockTime = 60000; break;
            case 60000: gpsLockTime = 120000; break;
            case 120000: gpsLockTime = 300000; break;
            case 300000: gpsLockTime = 600000; break;
            case 600000: gpsLockTime = 900000; break;
            case 900000: gpsLockTime = 0;
        }
    }

    public static enum ItemsTab {
        HIDDEN("Hide"),
        AT_END("Last"),
        AT_START("First"),
        ;

        public final String desc;

        private ItemsTab(String desc) {
            this.desc = desc;
        }
    }
}

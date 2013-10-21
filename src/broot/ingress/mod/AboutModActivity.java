package broot.ingress.mod;

import java.util.ArrayList;
import java.util.List;

import broot.ingress.mod.util.Config;
import broot.ingress.mod.util.UiVariant;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.Value;
import com.nianticproject.ingress.common.scanner.visuals.PortalParticleRender;
import com.nianticproject.ingress.common.ui.BaseSubActivity;
import com.nianticproject.ingress.common.ui.UiLayer;
import com.nianticproject.ingress.common.ui.widget.MenuTabId;
import com.nianticproject.ingress.common.ui.widget.MenuTopWidget;

public class AboutModActivity extends BaseSubActivity {

	public static class ListItem extends Table {
		public Label                   nameLabel;
		public Label                   descLabel;
		public final List<TextButton>  buttons = new ArrayList<TextButton>();

		private final Skin             skin;
		private final Label.LabelStyle smallWhite;
		private Table                  buttonsTable;

		public ListItem(final Skin skin, final String name, final String desc) {
			this.skin = skin;
			smallWhite = skin.get("small-white", Label.LabelStyle.class);

			setBackground(skin.getDrawable("nav-button-clear"));

			final Table t1 = new Table();
			t1.setBackground(skin.getDrawable("nav-button"));
			t1.add(nameLabel = new Label(name, smallWhite)).pad(0, 8, 0, 8);
			add(t1).left().top().pad(8, -8, 8, 8);

			add(descLabel = new Label(desc, smallWhite)).expandX().left();
			descLabel.setWrap(true);

			row();
		}

		public ListItem(final Skin skin, final String name, final String desc, final String value,
		        final EventListener listener) {
			this(skin, name, desc);
			addButton(null, value, listener);
		}

		public TextButton addButton(final String name, final String value, final EventListener listener) {
			if (buttonsTable == null) {
				add(buttonsTable = new Table()).colspan(2).expandX().fillX().padBottom(8);
			}

			buttonsTable.row();
			if (name == null) {
				buttonsTable.add().expandX();
			} else {
				buttonsTable.add(new Label(name, smallWhite)).expandX().right().padRight(12);
			}

			final TextButton button = new TextButton(value, skin);
			if (listener != null) {
				button.addListener(listener);
			}
			buttonsTable.add(button).width(Value.percentWidth(0.5F)).right().padRight(8);
			buttons.add(button);

			return button;
		}
	}

	private MenuTopWidget topWidget;

	private Table         menuItemsTable;
	private ListItem      gameplayTweaksItem;
	private ListItem      tabsItem;
	private ListItem      animsItem;
	private ListItem      uiTweaksItem;
	private ListItem      uiVariantItem;

	private ListItem      restartItem;

	public AboutModActivity() {
		super(AboutModActivity.class.getName());

		getRenderer().addUiLayer(new UiLayer() {
			private void addItem(final ListItem item) {
				menuItemsTable.add(item).expandX().fillX().padBottom(-2);
				menuItemsTable.row();
			}

			@Override
			public void createUi(final Skin skin, final Stage stage) {
				menuItemsTable = new Table();
				menuItemsTable.top().pad(10);

				gameplayTweaksItem = new ListItem(skin, "Gameplay tweaks", null);
				gameplayTweaksItem.addButton("TARGET and FIRE", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.swapTouchMenuButtons = !Config.swapTouchMenuButtons;
						updateGameplayTweaksValues(true);
					}
				});
				addItem(gameplayTweaksItem);

				tabsItem = new ListItem(skin, "Menu tabs", null);
				tabsItem.addButton("[ITEMS]", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.nextItemsTab();
						updateTabsValues(false);
					}
				});
				tabsItem.addButton("INVENTORY", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.showOrigItemsTab = !Config.showOrigItemsTab;
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("AGENT", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.showAgentTab = !Config.showAgentTab;
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("MISSIONS", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.showMissionTab = !Config.showMissionTab;
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("INTEL", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.showIntelTab = !Config.showIntelTab;
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("RECRUIT", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.showRecruitTab = !Config.showRecruitTab;
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("PASSCODE", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.showPasscodeTab = !Config.showPasscodeTab;
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("DEVICE", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.showDeviceTab = !Config.showDeviceTab;
						updateTabsValues(true);
					}
				});
				addItem(tabsItem);

				animsItem = new ListItem(skin, "Animations", null);
				// animsItem.addButton("Globe intro", "", new ClickListener() {
				// @Override
				// public void clicked(InputEvent event, float x, float y) {
				// Config.skipIntro = !Config.skipIntro;
				// updateAnimsValues(true);
				// }
				// });
				animsItem.addButton("Scanner zoom in", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.scannerZoomInAnimEnabled = !Config.scannerZoomInAnimEnabled;
						updateAnimsValues(true);
					}
				});
				animsItem.addButton("Hacking", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.hackAnimEnabled = !Config.hackAnimEnabled;
						updateAnimsValues(true);
					}
				});
				animsItem.addButton("Item rotation", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.rotateInventoryItemsEnabled = !Config.rotateInventoryItemsEnabled;
						updateAnimsValues(true);
					}
				});
				animsItem.addButton("Recycle animation", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.recycleAnimationsEnabled = !Config.recycleAnimationsEnabled;
						updateAnimsValues(true);
					}
				});
				animsItem.addButton("XM flow", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.xmFlowEnabled = !Config.xmFlowEnabled;
						updateAnimsValues(true);
					}
				});
				animsItem.addButton("Shield Animation", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.shieldAnimEnabled = !Config.shieldAnimEnabled;
						updateAnimsValues(true);
					}
				});
				addItem(animsItem);

				uiTweaksItem = new ListItem(skin, "UI tweaks", null);
				uiTweaksItem.addButton("Fullscreen", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.fullscreen = !Config.fullscreen;
						updateUiTweaksValues(true);
						Mod.updateFullscreenMode();
						restartItem.descLabel.setText("Restart is recommended");
					}
				});
				uiTweaksItem.addButton("Portal vectors", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.showPortalVectors = !Config.showPortalVectors;
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Portal particles", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.portalParticlesEnabled = !Config.portalParticlesEnabled;
						updateUiTweaksValues(true);
						PortalParticleRender.enabled = Config.portalParticlesEnabled;
					}
				});
				uiTweaksItem.addButton("Scanner objects", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.scannerObjectsEnabled = !Config.scannerObjectsEnabled;
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Simplify Items", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.simplifyInventoryItems = !Config.simplifyInventoryItems;
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Chat time format", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.chatTimeFormat = (Config.chatTimeFormat + 1) % 3;
						updateUiTweaksValues(true);
						restartItem.descLabel.setText("Restart is recommended");
					}
				});
				uiTweaksItem.addButton("Vibrate", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.vibration = !Config.vibration;
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Keep screen on", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.keepScreenOn = !Config.keepScreenOn;
						updateUiTweaksValues(true);
						Mod.updateKeepScreenOn();
					}
				});
				uiTweaksItem.addButton("Keep GPS on", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.nextGpsLockTime();
						updateUiTweaksValues(true);
						Mod.updateKeepScreenOn();
					}
				});
				uiTweaksItem.addButton("Modify portal info", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.changePortalInfoDialog = !Config.changePortalInfoDialog;
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Allow Cubes recyling", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.enablePowerCubesRecycle = !Config.enablePowerCubesRecycle;
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Privacy", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.isPrivacyOn = !Config.isPrivacyOn;
						updateUiTweaksValues(true);
						restartItem.descLabel.setText("Restart is recommended");
					}
				});
				uiTweaksItem.addButton("Block invite nag", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.needInviteNagBlock = !Config.needInviteNagBlock;
						updateUiTweaksValues(true);
					}
				});
				addItem(uiTweaksItem);

				addItem(uiVariantItem = new ListItem(skin, "UI variant", "", "Toggle", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.nextUiVariant();
						Mod.updateCurrUiVariant();
						updateUiVariantValue();
						restartItem.descLabel.setText("Restart is recommended");
					}
				}));

				addItem(restartItem = new ListItem(skin, "Restart", "", "Restart app", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Mod.restartApp();
					}
				}));

				addItem(new ListItem(skin, "Mod version", Mod.getFullVersion()));

				final Table root = new Table();
				root.setFillParent(true);
				topWidget = new MenuTopWidget(skin, (int) stage.getWidth(), Mod.menuController, MenuTabId.MOD_ABOUT);
				topWidget.createTabs();
				root.add(topWidget);
				root.row();
				root.add(new ScrollPane(menuItemsTable)).expand().fill().pad(2);

				stage.addActor(root);
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean dontDispose(final float f1) {
				return true;
			}
		});
	}

	@Override
	protected String getName() {
		return AboutModActivity.class.getName();
	}

	@Override
	protected void onResume() {
		updateGameplayTweaksValues(false);
		updateTabsValues(false);
		updateAnimsValues(false);
		updateUiTweaksValues(false);
		updateUiVariantValue();
	}

	private void updateAnimsValues(final boolean save) {
		if (save) {
			Config.save();
		}
		// animsItem.buttons.get(0).setText(!Config.skipIntro ? "ON" : "OFF");
		animsItem.buttons.get(0).setText(Config.scannerZoomInAnimEnabled ? "ON" : "OFF");
		animsItem.buttons.get(1).setText(Config.hackAnimEnabled ? "ON" : "OFF");
		animsItem.buttons.get(2).setText(Config.rotateInventoryItemsEnabled ? "ON" : "OFF");
		animsItem.buttons.get(3).setText(Config.recycleAnimationsEnabled ? "ON" : "OFF");
		animsItem.buttons.get(4).setText(Config.xmFlowEnabled ? "ON" : "OFF");
		animsItem.buttons.get(5).setText(Config.shieldAnimEnabled ? "ON" : "OFF");
	}

	private void updateGameplayTweaksValues(final boolean save) {
		if (save) {
			Config.save();
		}
		gameplayTweaksItem.buttons.get(0).setText(Config.swapTouchMenuButtons ? "Swap" : "Leave");
	}

	private void updateTabsValues(final boolean save) {
		if (save) {
			Config.save();
		}
		tabsItem.buttons.get(0).setText(Config.itemsTab.desc);
		tabsItem.buttons.get(1).setText(Config.showOrigItemsTab ? "Show" : "Hide");
		tabsItem.buttons.get(2).setText(Config.showAgentTab ? "Show" : "Hide");
		tabsItem.buttons.get(3).setText(Config.showMissionTab ? "Show" : "Hide");
		tabsItem.buttons.get(4).setText(Config.showIntelTab ? "Show" : "Hide");
		tabsItem.buttons.get(5).setText(Config.showRecruitTab ? "Show" : "Hide");
		tabsItem.buttons.get(6).setText(Config.showPasscodeTab ? "Show" : "Hide");
		tabsItem.buttons.get(7).setText(Config.showDeviceTab ? "Show" : "Hide");
		topWidget.createTabs();
	}

	private void updateUiTweaksValues(final boolean save) {
		String timeFormatLabel, gpsLockLabel;
		if (save) {
			Config.save();
		}
		uiTweaksItem.buttons.get(0).setText(Config.fullscreen ? "ON" : "OFF");
		uiTweaksItem.buttons.get(1).setText(Config.showPortalVectors ? "ON" : "OFF");
		uiTweaksItem.buttons.get(2).setText(Config.portalParticlesEnabled ? "ON" : "OFF");
		uiTweaksItem.buttons.get(3).setText(Config.scannerObjectsEnabled ? "ON" : "OFF");
		uiTweaksItem.buttons.get(4).setText(Config.simplifyInventoryItems ? "ON" : "OFF");
		switch (Config.chatTimeFormat) {
		case 0:
			timeFormatLabel = "12:00 AM";
			break;
		case 1:
			timeFormatLabel = "00:00:00";
			break;
		default:
			timeFormatLabel = "00:00";
			break;
		}
		uiTweaksItem.buttons.get(5).setText(timeFormatLabel);
		uiTweaksItem.buttons.get(6).setText(Config.vibration ? "ON" : "OFF");
		uiTweaksItem.buttons.get(7).setText(Config.keepScreenOn ? "ON" : "OFF");
		switch (Config.gpsLockTime) {
		case 0:
			gpsLockLabel = "Disabled";
			break;
		case 30000:
			gpsLockLabel = "30sec";
			break;
		case 60000:
			gpsLockLabel = "1min";
			break;
		case 120000:
			gpsLockLabel = "2min";
			break;
		case 300000:
			gpsLockLabel = "5min";
			break;
		case 600000:
			gpsLockLabel = "10min";
			break;
		case 900000:
			gpsLockLabel = "15min";
			break;
		default:
			gpsLockLabel = "Unknown";
		}
		uiTweaksItem.buttons.get(8).setText(gpsLockLabel);
		uiTweaksItem.buttons.get(9).setText(Config.changePortalInfoDialog ? "ON" : "OFF");
		uiTweaksItem.buttons.get(10).setText(Config.enablePowerCubesRecycle ? "ON" : "OFF");
		uiTweaksItem.buttons.get(11).setText(Config.isPrivacyOn ? "ON" : "OFF");
		uiTweaksItem.buttons.get(12).setText(Config.needInviteNagBlock ? "ON" : "OFF");
	}

	private void updateUiVariantValue() {
		String text = Config.uiVariant.desc;
		if (Config.uiVariant == UiVariant.AUTO) {
			text += " (" + Mod.currUiVariant.desc + ")";
		}
		uiVariantItem.descLabel.setText(text);
	}
}

package broot.ingress.mod;

import java.util.ArrayList;
import java.util.List;

import broot.ingress.mod.util.Config;
import broot.ingress.mod.util.Config.ChatTimeFormat;
import broot.ingress.mod.util.Config.GpsLockTime;
import broot.ingress.mod.util.Config.Pref;

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
						Config.invertBooleanPreference(Pref.SwapTouchMenuButtons);
						updateGameplayTweaksValues(true);
					}
				});
				addItem(gameplayTweaksItem);

				tabsItem = new ListItem(skin, "Menu tabs", null);
				tabsItem.addButton("[ITEMS]", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.setNextValue(Pref.ItemsTab);
						updateTabsValues(false);
					}
				});
				tabsItem.addButton("INVENTORY", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ShowOrigItemsTab);
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("AGENT", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ShowAgentTab);
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("MISSIONS", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ShowMissionTab);
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("INTEL", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ShowIntelTab);
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("RECRUIT", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ShowRecruitTab);
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("PASSCODE", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ShowPasscodeTab);
						updateTabsValues(true);
					}
				});
				tabsItem.addButton("DEVICE", "Show", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ShowDeviceTab);
						updateTabsValues(true);
					}
				});
				addItem(tabsItem);

				animsItem = new ListItem(skin, "Animations", null);
				animsItem.addButton("Scanner zoom in", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ScannerZoomInAnimEnabled);
						updateAnimsValues(true);
					}
				});
				animsItem.addButton("Hacking", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.HackAnimEnabled);
						updateAnimsValues(true);
					}
				});
				animsItem.addButton("Item rotation", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.RotateInventoryItemsEnabled);
						updateAnimsValues(true);
					}
				});
				animsItem.addButton("Recycle animation", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.RecycleAnimationsEnabled);
						updateAnimsValues(true);
					}
				});
				animsItem.addButton("XM flow", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.XmFlowEnabled);
						updateAnimsValues(true);
					}
				});
				animsItem.addButton("Shield Animation", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ShieldAnimEnabled);
						updateAnimsValues(true);
					}
				});
				addItem(animsItem);

				uiTweaksItem = new ListItem(skin, "UI tweaks", null);
				uiTweaksItem.addButton("Fullscreen", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.Fullscreen);
						updateUiTweaksValues(true);
						Mod.updateFullscreenMode();
						restartItem.descLabel.setText("Restart is recommended");
					}
				});
				uiTweaksItem.addButton("Portal vectors", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ShowPortalVectors);
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Portal particles", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.PortalParticlesEnabled);
						updateUiTweaksValues(true);
						PortalParticleRender.enabled = Config.getBoolean(Pref.PortalParticlesEnabled);
					}
				});
				uiTweaksItem.addButton("Scanner objects", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ScannerObjectsEnabled);
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Simplify Items", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.SimplifyInventoryItems);
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Chat time format", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.setNextValue(Pref.ChatTimeFormat);
						updateUiTweaksValues(true);
						restartItem.descLabel.setText("Restart is recommended");
					}
				});
				uiTweaksItem.addButton("Vibrate", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.Vibration);
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Keep screen on", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.KeepScreenOn);
						updateUiTweaksValues(true);
						Mod.updateKeepScreenOn();
					}
				});
				uiTweaksItem.addButton("Keep GPS on", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.setNextValue(Pref.GpsLockTime);
						updateUiTweaksValues(true);
						Mod.updateKeepScreenOn();
					}
				});
				uiTweaksItem.addButton("Modify portal info", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.ChangePortalInfoDialog);
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Allow Cubes recyling", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.EnablePowerCubesRecycle);
						updateUiTweaksValues(true);
					}
				});
				uiTweaksItem.addButton("Privacy", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.IsPrivacyOn);
						updateUiTweaksValues(true);
						restartItem.descLabel.setText("Restart is recommended");
					}
				});
				uiTweaksItem.addButton("Block invite nag", "", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.invertBooleanPreference(Pref.NeedInviteNagBlock);
						updateUiTweaksValues(true);
					}
				});
				addItem(uiTweaksItem);

				addItem(uiVariantItem = new ListItem(skin, "UI variant", "", "Toggle", new ClickListener() {
					@Override
					public void clicked(final InputEvent event, final float x, final float y) {
						Config.setNextValue(Pref.UiVariant);
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
		animsItem.buttons.get(0).setText(Config.getBoolean(Pref.ScannerZoomInAnimEnabled) ? "ON" : "OFF");
		animsItem.buttons.get(1).setText(Config.getBoolean(Pref.HackAnimEnabled) ? "ON" : "OFF");
		animsItem.buttons.get(2).setText(Config.getBoolean(Pref.RotateInventoryItemsEnabled) ? "ON" : "OFF");
		animsItem.buttons.get(3).setText(Config.getBoolean(Pref.RecycleAnimationsEnabled) ? "ON" : "OFF");
		animsItem.buttons.get(4).setText(Config.getBoolean(Pref.XmFlowEnabled) ? "ON" : "OFF");
		animsItem.buttons.get(5).setText(Config.getBoolean(Pref.ShieldAnimEnabled) ? "ON" : "OFF");
	}

	private void updateGameplayTweaksValues(final boolean save) {
		if (save) {
			Config.save();
		}
		gameplayTweaksItem.buttons.get(0).setText(Config.getBoolean(Pref.SwapTouchMenuButtons) ? "Swap" : "Leave");
	}

	private void updateTabsValues(final boolean save) {
		if (save) {
			Config.save();
		}
		tabsItem.buttons.get(0).setText(Config.getEnumValue(Pref.ItemsTab).toString());
		tabsItem.buttons.get(1).setText(Config.getBoolean(Pref.ShowOrigItemsTab) ? "Show" : "Hide");
		tabsItem.buttons.get(2).setText(Config.getBoolean(Pref.ShowAgentTab) ? "Show" : "Hide");
		tabsItem.buttons.get(3).setText(Config.getBoolean(Pref.ShowMissionTab) ? "Show" : "Hide");
		tabsItem.buttons.get(4).setText(Config.getBoolean(Pref.ShowIntelTab) ? "Show" : "Hide");
		tabsItem.buttons.get(5).setText(Config.getBoolean(Pref.ShowRecruitTab) ? "Show" : "Hide");
		tabsItem.buttons.get(6).setText(Config.getBoolean(Pref.ShowPasscodeTab) ? "Show" : "Hide");
		tabsItem.buttons.get(7).setText(Config.getBoolean(Pref.ShowDeviceTab) ? "Show" : "Hide");
		topWidget.createTabs();
	}

	private void updateUiTweaksValues(final boolean save) {
		if (save) {
			Config.save();
		}
		uiTweaksItem.buttons.get(0).setText(Config.getBoolean(Pref.Fullscreen) ? "ON" : "OFF");
		uiTweaksItem.buttons.get(1).setText(Config.getBoolean(Pref.ShowPortalVectors) ? "ON" : "OFF");
		uiTweaksItem.buttons.get(2).setText(Config.getBoolean(Pref.PortalParticlesEnabled) ? "ON" : "OFF");
		uiTweaksItem.buttons.get(3).setText(Config.getBoolean(Pref.ScannerObjectsEnabled) ? "ON" : "OFF");
		uiTweaksItem.buttons.get(4).setText(Config.getBoolean(Pref.SimplifyInventoryItems) ? "ON" : "OFF");
		ChatTimeFormat f = Config.getEnumValue(Pref.ChatTimeFormat);
		uiTweaksItem.buttons.get(5).setText(f.toString());
		uiTweaksItem.buttons.get(6).setText(Config.getBoolean(Pref.Vibration) ? "ON" : "OFF");
		uiTweaksItem.buttons.get(7).setText(Config.getBoolean(Pref.KeepScreenOn) ? "ON" : "OFF");
		GpsLockTime t = Config.getEnumValue(Pref.GpsLockTime);
		uiTweaksItem.buttons.get(8).setText(t.toString());
		uiTweaksItem.buttons.get(9).setText(Config.getBoolean(Pref.ChangePortalInfoDialog) ? "ON" : "OFF");
		uiTweaksItem.buttons.get(10).setText(Config.getBoolean(Pref.EnablePowerCubesRecycle) ? "ON" : "OFF");
		uiTweaksItem.buttons.get(11).setText(Config.getBoolean(Pref.IsPrivacyOn) ? "ON" : "OFF");
		uiTweaksItem.buttons.get(12).setText(Config.getBoolean(Pref.NeedInviteNagBlock) ? "ON" : "OFF");
	}

	private void updateUiVariantValue() {
		uiVariantItem.descLabel.setText(Config.getEnumValue(Pref.UiVariant).toString());
	}
}

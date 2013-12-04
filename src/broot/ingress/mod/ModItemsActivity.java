package broot.ingress.mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nianticproject.ingress.common.ComponentManager;
import com.nianticproject.ingress.common.inventory.ui.IndistinguishableItems;
import com.nianticproject.ingress.common.ui.BaseSubActivity;
import com.nianticproject.ingress.common.ui.FormatUtils;
import com.nianticproject.ingress.common.ui.UiLayer;
import com.nianticproject.ingress.common.ui.widget.MenuTabId;
import com.nianticproject.ingress.common.ui.widget.MenuTopWidget;
import com.nianticproject.ingress.gameentity.GameEntity;
import com.nianticproject.ingress.gameentity.components.FlipCard;
import com.nianticproject.ingress.gameentity.components.FlipCardType;
import com.nianticproject.ingress.gameentity.components.ItemRarity;
import com.nianticproject.ingress.shared.ItemType;

public class ModItemsActivity extends BaseSubActivity {

	private class Button {

		public final TextButton button;
		public GameEntity       entity;

		private Button() {
			button = new TextButton("", defaultClearStyle);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					if (entity == null) {
						return;
					}
					Mod.menuController.showItemDetails(entity);
				}
			});
		}
	}

	private static final int LEVEL_COUNT = 8;

	private static String formatValue(final int value) {
		return value == 0 ? "-" : String.format(Locale.US, "%,d", value);
	}

	private TextButton.TextButtonStyle                 defaultClearStyle;
	private MenuTopWidget                              topWidget;

	private Label                                      sumLabel;
	private Label                                      keysLabel;

	private final Map<ItemType, List<Button>>          buttonsByLvl    = new HashMap<ItemType, List<Button>>();

	private final Map<Object, Map<ItemRarity, Button>> buttonsByRarity = new HashMap<Object, Map<ItemRarity, Button>>();

	final float                                        den             = Mod.displayMetrics.density;

	public ModItemsActivity() {
		super(ModItemsActivity.class.getName());

		getRenderer().addUiLayer(new UiLayer() {
			private void addItemsTable(final Skin skin, final Table parent) {
				final Table t = new Table();

				t.row();
				t.add();
				t.add(new Label("R", skin));
				t.add(new Label("X", skin));
				t.add(new Label("U", skin));
				t.add(new Label("C", skin));
				t.add(new Label("M", skin));

				for (int lvl = 0; lvl < LEVEL_COUNT + 1; lvl++) {
					final Color color = (lvl < LEVEL_COUNT) ? FormatUtils.getColorForLevel(skin, lvl + 1) : Color.WHITE;

					t.row().height(40 * den);

					final Label label1 = new Label((lvl < LEVEL_COUNT) ? ("L" + (lvl + 1)) : "Total", skin);
					label1.setColor(color);
					t.add(label1).pad(-1, 0, -1, 16 * den);

					for (final ItemType type : new ItemType[] { ItemType.EMITTER_A, ItemType.EMP_BURSTER,
					        ItemType.ULTRA_STRIKE, ItemType.POWER_CUBE, ItemType.MEDIA }) {
						List<Button> buttons = buttonsByLvl.get(type);
						if (buttons == null) {
							buttons = new ArrayList<Button>();
							buttonsByLvl.put(type, buttons);
						}
						final Button btn = new Button();
						buttons.add(btn);
						btn.button.getLabel().setColor(color);
						t.add(btn.button).width(0).expandX().fill().pad(-1, -1, -1, -1);
					}
				}

				t.row();
				t.add().height(16 * den);

				addRarityRows(t, skin, new String[] { "Shields" }, new Object[] { ItemType.RES_SHIELD },
				        new ItemRarity[] { ItemRarity.COMMON, ItemRarity.RARE, ItemRarity.VERY_RARE });
				addRarityRows(t, skin, new String[] { "Heat Sink", "Multi-hack" }, new Object[] { ItemType.HEATSINK,
				        ItemType.MULTIHACK }, new ItemRarity[] { ItemRarity.COMMON, ItemRarity.RARE,
				        ItemRarity.VERY_RARE });
				addRarityRows(t, skin, new String[] { "Link Amp" }, new Object[] { ItemType.LINK_AMPLIFIER },
				        new ItemRarity[] { ItemRarity.RARE , ItemRarity.VERY_RARE});
				addRarityRows(t, skin, new String[] { "Force Amp", "Turret" }, new Object[] { ItemType.FORCE_AMP,
				        ItemType.TURRET }, new ItemRarity[] { ItemRarity.RARE });
				addRarityRows(t, skin, new String[] { "ADA Refactor", "JARVIS Virus" }, new Object[] {
				        FlipCardType.ADA, FlipCardType.JARVIS }, new ItemRarity[] { ItemRarity.VERY_RARE });

				t.row();
				keysLabel = new Label("", skin);
				t.add(keysLabel).colspan(2).width(0).pad(-1, 0, -1, 0).left();

				parent.row();
				parent.add(t).expandX().fillX();
			}

			private void addRarityRows(final Table t, final Skin skin, final String[] names, final Object[] types,
			        final ItemRarity[] rarities) {
				for (int i = 0; i < types.length; i++) {
					t.row().height(40 * den);
					t.add(new Label(names[i], skin)).colspan(2).width(0).pad(-1, 0, -1, 0).left();

					final Map<ItemRarity, Button> buttons = new HashMap<ItemRarity, Button>(3);
					buttonsByRarity.put(types[i], buttons);
					for (final ItemRarity rarity : new ItemRarity[] { ItemRarity.COMMON, ItemRarity.RARE,
					        ItemRarity.VERY_RARE }) {
						if (Arrays.binarySearch(rarities, rarity) < 0) {
							t.add().pad(-1, -1, -1, -1);
							continue;
						}
						final Button btn = new Button();
						btn.button.getLabel().setColor(FormatUtils.getColorForRarity(skin, rarity));
						t.add(btn.button).width(0).fill().pad(-1, -1, -1, -1);
						buttons.put(rarity, btn);
					}
				}
			}

			@Override
			public void createUi(final Skin skin, final Stage stage) {
				defaultClearStyle = new TextButton.TextButtonStyle();
				defaultClearStyle.down = skin.getDrawable("nav-button-clear-down");
				defaultClearStyle.up = skin.getDrawable("nav-button-clear");
				defaultClearStyle.font = skin.getFont("default-font");

				final Table t = new Table(skin);
				t.top().pad(6.6f * den);

				t.row();
				t.add(sumLabel = new Label("", skin));
				addItemsTable(skin, t);

				final Table root = new Table();
				root.setFillParent(true);
				topWidget = new MenuTopWidget(skin, (int) stage.getWidth(), Mod.menuController, MenuTabId.MOD_ITEMS);
				topWidget.createTabs();
				root.add(topWidget);
				root.row();
				root.add(new ScrollPane(t)).expand().fill().pad(2);

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
		return ModItemsActivity.class.getName();
	}

	@Override
	protected void onResume() {
		updateLabels();
		topWidget.createTabs();
	}

	private void updateLabels() {
		for (final List<Button> buttons : buttonsByLvl.values()) {
			for (final Button btn : buttons) {
				btn.button.setText(formatValue(0));
				btn.entity = null;
			}
		}
		for (final Map<ItemRarity, Button> buttons : buttonsByRarity.values()) {
			for (final Button btn : buttons.values()) {
				btn.button.setText(formatValue(0));
				btn.entity = null;
			}
		}

		int sum = 0;
		int resoCnt = 0;
		int xmpCnt = 0;
		int ultraCnt = 0;
		int cubeCnt = 0;
		int mediaCnt = 0;
		boolean itemHandled;
		int keysNumber = 0;
		int distinctKeysNumber = 0;
		final int media[] = new int[LEVEL_COUNT];
		for (final IndistinguishableItems items : IndistinguishableItems.fromItemsByPlayerInfo(null,
		        Mod.cache.getInventory())) {
			itemHandled = false;
			final int count = items.getCount();
			sum += count;
			final int lvl = items.getLevel() - 1;

			final ItemType type = items.getType();
			Button btn;
			switch (type) {
			case MEDIA:
				final int curr = media[lvl];
				if (curr == 0) {
					buttonsByLvl.get(type).get(lvl).entity = items.getEntity();
				}
				media[lvl] += count;
				mediaCnt += count;
				continue;
			case EMITTER_A:
				resoCnt += count;
				itemHandled = true;
			case EMP_BURSTER:
				if (!itemHandled) {
					xmpCnt += count;
					itemHandled = true;
				}
			case ULTRA_STRIKE:
				if (!itemHandled) {
					ultraCnt += count;
					itemHandled = true;
				}
			case POWER_CUBE:
				if (!itemHandled) {
					cubeCnt += count;
				}
				btn = buttonsByLvl.get(type).get(lvl);
				break;
			case PORTAL_LINK_KEY:
				keysNumber += count;
				distinctKeysNumber += 1;
				continue;
			case RES_SHIELD:
			case FORCE_AMP:
			case HEATSINK:
			case LINK_AMPLIFIER:
			case MULTIHACK:
			case TURRET:
				btn = buttonsByRarity.get(type).get(items.getRarity());
				break;
			case FLIP_CARD:
				btn = buttonsByRarity
				        .get(((FlipCard) items.getEntity().getComponent(FlipCard.class)).getFlipCardType()).get(
				                items.getRarity());
				break;
			default:
				continue;
			}

			if (btn != null) {
				btn.button.setText(formatValue(count));
				btn.entity = items.getEntity();
			}
		}
		for (int lvl = 0; lvl < LEVEL_COUNT; lvl++) {
			buttonsByLvl.get(ItemType.MEDIA).get(lvl).button.setText(formatValue(media[lvl]));
		}
		buttonsByLvl.get(ItemType.EMITTER_A).get(LEVEL_COUNT).button.setText(formatValue(resoCnt));
		buttonsByLvl.get(ItemType.EMP_BURSTER).get(LEVEL_COUNT).button.setText(formatValue(xmpCnt));
		buttonsByLvl.get(ItemType.ULTRA_STRIKE).get(LEVEL_COUNT).button.setText(formatValue(ultraCnt));
		buttonsByLvl.get(ItemType.POWER_CUBE).get(LEVEL_COUNT).button.setText(formatValue(cubeCnt));
		buttonsByLvl.get(ItemType.MEDIA).get(LEVEL_COUNT).button.setText(formatValue(mediaCnt));

		final long xm = ComponentManager.getPlayerModel().getCurrentXM();
		sumLabel.setText(String.format(Locale.US, "Items: %,d - XM: %,d", sum, xm));
		keysLabel.setText("Keys:  " + keysNumber + " for " + distinctKeysNumber + " distinct portals");
	}
}

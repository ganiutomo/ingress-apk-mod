package broot.ingress.mod;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.Cell;
import com.nianticproject.ingress.common.ComponentManager;
import com.nianticproject.ingress.common.PlayerLocation;
import com.nianticproject.ingress.common.model.BasePlayerListener;
import com.nianticproject.ingress.common.model.PlayerListener;
import com.nianticproject.ingress.common.ui.FormatUtils;
import com.nianticproject.ingress.common.upgrade.PortalUpgradeUi;
import com.nianticproject.ingress.gameentity.GameEntity;
import com.nianticproject.ingress.gameentity.components.LocationE6;
import com.nianticproject.ingress.shared.location.LocationUtils;

public class PortalUpgradeMod {

	private static GameEntity     portalEntity;
	private static Label          distLabel;
	private static PlayerListener playerListener;

	public static int getResonatorBrowserHeight(final int withoutPad) {
		return withoutPad + (Mod.displayMetrics.heightPixels < 800 ? 0 : 30);
	}

	private static void init(final PortalUpgradeUi ui) {
		portalEntity = ui.activity.portalEntity;
		playerListener = new BasePlayerListener() {
			@Override
			public String getName() {
				return PortalUpgradeMod.class.getSimpleName() + ":playerListener";
			}

			@Override
			public void onLocationChanged(final PlayerLocation location) {
				updateDistLabel(location);
			}
		};
		ComponentManager.getPlayerModel().addListener(playerListener);
	}

	public static void onDispose() {
		portalEntity = null;
		distLabel = null;
		ComponentManager.getPlayerModel().removeListener(playerListener);
	}

	public static void onStatsTableCreated(final PortalUpgradeUi ui, final Table t) {
		init(ui);

		final Label.LabelStyle style = Mod.skin.get("portal-stats", Label.LabelStyle.class);
		final float den = Mod.displayMetrics.density;

		final List<Cell> cells = new ArrayList<Cell>(t.getCells());
		t.clear();
		t.left();
		t.defaults().left();
		t.add((Actor) cells.get(1).getWidget()).padLeft(20 * den);
		t.add((Actor) cells.get(2).getWidget()).padLeft(8 * den);
		t.add((Actor) cells.get(3).getWidget()).padLeft(16 * den);
		t.add((Actor) cells.get(4).getWidget()).padLeft(8 * den);
		t.row();
		t.add((Actor) cells.get(7).getWidget()).padLeft(20 * den);
		t.add((Actor) cells.get(8).getWidget()).padLeft(8 * den);
		t.add(new Label("Dist.:", style)).padLeft(16 * den);
		t.add(distLabel = new Label("", style)).padLeft(8 * den);

		updateDistLabel(ComponentManager.getPlayerModel().getPlayerLocation());
	}

	private static void updateDistLabel(final PlayerLocation location) {
		final double dist = LocationUtils.calculateDistance(ComponentManager.getPlayerModel().getPlayerLocation().getLatLng(),
		        ((LocationE6) portalEntity.getComponent(LocationE6.class)).getLatLng());
		distLabel.setText(FormatUtils.formatDistance((float) dist));
	}
}

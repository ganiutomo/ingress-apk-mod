package broot.ingress.mod.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import broot.ingress.mod.BuildConfig;

public final class UiVariant {

	public static final UiVariant              AUTO = new UiVariant("auto", "Auto");
	public static final List<UiVariant>        variants;
	public static final Map<String, UiVariant> byName;

	static {
		final List<String> avail = new ArrayList<String>(Arrays.asList(BuildConfig.AVAILABLE_ASSETS));
		variants = new ArrayList<UiVariant>(avail.size() + 1);
		variants.add(AUTO);

		for (final UiVariant variant : new UiVariant[] { new UiVariant("data-xxhdpi", "Original xxhdpi", "data-xhdpi"),
		    new UiVariant("data-xhdpi", "Original xhdpi", "data"), new UiVariant("data", "Original normal"),
		    new UiVariant("data-hvga", "Mod HVGA"), new UiVariant("data-qvga", "Mod QVGA"),
		    new UiVariant("data-ingressopt-hvga", "Ingressopt HVGA"),
		    new UiVariant("data-ingressopt-qvga", "Ingressopt QVGA"), }) {
			if (avail.remove(variant.name)) {
				variants.add(variant);
			}
		}
		Collections.sort(avail);
		for (final String name : avail) {
			variants.add(new UiVariant(name, "Custom " + name));
		}

		byName = new HashMap<String, UiVariant>(variants.size());
		for (final UiVariant variant : variants) {
			byName.put(variant.name, variant);
		}
	}

	public final String                        name;
	public final String                        desc;
	public final String                        parent;

	private UiVariant(final String name, final String desc) {
		this(name, desc, null);
	}

	private UiVariant(final String name, final String desc, final String parent) {
		this.name = name;
		this.desc = desc;
		this.parent = parent;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final UiVariant uiVariant = (UiVariant) o;

		if (!name.equals(uiVariant.name)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}

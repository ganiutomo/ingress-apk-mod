package broot.ingress.mod;

public class BuildConfig {

	public static enum UiVariant {
		auto("auto", "Auto")
		// AVAILABLE_ASSETS
		;

		private final String name;
		private final String desc;
		private final String parent;

		private UiVariant(final String name, final String desc) {
			this(name, desc, null);
		}

		private UiVariant(final String name, final String desc, final String parent) {
			this.name = name;
			this.desc = desc;
			this.parent = parent;
		}

		public final String getDescription() {
			String result = desc;
			if (this == auto) {
				result += " (" + Mod.currUiVariant.desc + ")";
			}
			return result;
		}

		public final String getName() {
			return name;
		}

		public final String getParent() {
			return parent;
		}

		@Override
		public String toString() {
			return "Toggle";
		}
	}

	public static final String   MOD_VERSION = null;
}

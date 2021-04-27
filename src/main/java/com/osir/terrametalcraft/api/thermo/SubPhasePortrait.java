package com.osir.terrametalcraft.api.thermo;

import java.util.EnumMap;

import com.github.zi_jing.cuckoolib.util.data.ContinuousMap;

public abstract class SubPhasePortrait {
	public abstract float getSpecificHeat(Phase phase);

	public abstract ContinuousMap<Phase> getCriticalPoints();

	public static Builder builder() {
		return new Builder();
	}

	public static SubPhasePortrait getSimplified(Phase phase, float specificHeat) {
		return new SimplifiedSubPhasePortrait(phase, specificHeat);
	}

	public static class Builder {
		private ContinuousMap<Phase> criticalPoints;
		private EnumMap<Phase, Float> specificHeat;

		public Builder() {
			this.specificHeat = new EnumMap<Phase, Float>(Phase.class);
		}

		public Builder setCriticalPoints(ContinuousMap<Phase> criticalPoints) {
			this.criticalPoints = criticalPoints;
			return this;
		}

		public Builder putSpecificHeat(Phase phase, float value) {
			this.specificHeat.put(phase, value);
			return this;
		}

		public SubPhasePortrait build() {
			return new NormalSubPhasePortrait(this.criticalPoints, this.specificHeat);
		}
	}

	private static class NormalSubPhasePortrait extends SubPhasePortrait {
		private ContinuousMap<Phase> criticalPoints;
		private EnumMap<Phase, Float> specificHeat;

		private NormalSubPhasePortrait(ContinuousMap<Phase> criticalPoints, EnumMap<Phase, Float> specificHeat) {
			this.criticalPoints = criticalPoints;
			this.specificHeat = specificHeat;
		}

		@Override
		public float getSpecificHeat(Phase phase) {
			if (this.specificHeat.containsKey(phase)) {
				return this.specificHeat.get(phase);
			}
			return 1;
		}

		@Override
		public ContinuousMap<Phase> getCriticalPoints() {
			return this.criticalPoints;
		}
	}

	private static class SimplifiedSubPhasePortrait extends SubPhasePortrait {
		private ContinuousMap<Phase> criticalPoints;
		private float specificHeat;

		private SimplifiedSubPhasePortrait(Phase phase, float specificHeat) {
			this.criticalPoints = ContinuousMap.<Phase>builder().addCriticalPoint(0, phase).build();
			this.specificHeat = specificHeat;
		}

		@Override
		public float getSpecificHeat(Phase phase) {
			return this.specificHeat;
		}

		@Override
		public ContinuousMap<Phase> getCriticalPoints() {
			return this.criticalPoints;
		}
	}
}
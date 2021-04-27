package com.osir.terrametalcraft.api.thermo;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.github.zi_jing.cuckoolib.util.data.ContinuousMap;

public abstract class PhasePortrait {
	public static final PhasePortrait SIMPLE_SOLID = getSimplified(
			SubPhasePortrait.getSimplified(Phase.SOLID, ThermoUtil.UNIVERSAL_GAS_CONSTANT));

	public abstract SubPhasePortrait getSubPhasePortrait(float pressure);

	public float getSpecificHeat(float pressure, Phase phase) {
		return this.getSubPhasePortrait(pressure).getSpecificHeat(phase);
	}

	public ContinuousMap<Phase> getCriticalPoints(float pressure) {
		return this.getSubPhasePortrait(pressure).getCriticalPoints();
	}

	public float getTemperature(float pressure, float moleEnergy) {
		SubPhasePortrait sub = this.getSubPhasePortrait(pressure);
		List<Pair<Float, Phase>> points = sub.getCriticalPoints().getPointsMap();
		float temp = 0;
		for (Pair<Float, Phase> pair : points) {
			float pointTemp = pair.getLeft();
			float specificHeat = sub.getSpecificHeat(pair.getRight());
			if (specificHeat * (pointTemp - temp) > moleEnergy) {
				temp += moleEnergy / specificHeat;
				moleEnergy = 0;
				break;
			} else {
				moleEnergy -= specificHeat * (pointTemp - temp);
				temp = pointTemp;
			}
		}
		temp += moleEnergy / sub.getSpecificHeat(points.get(points.size() - 1).getRight());
		return temp;
	}

	public float getMoleEnergy(float pressure, float temperature) {
		SubPhasePortrait sub = this.getSubPhasePortrait(pressure);
		List<Pair<Float, Phase>> points = sub.getCriticalPoints().getPointsMap();
		float moleEnergy = 0, temp = 0;
		for (Pair<Float, Phase> pair : points) {
			float pointTemp = pair.getLeft();
			if (pointTemp <= temperature) {
				moleEnergy += (pointTemp - temp) * sub.getSpecificHeat(pair.getRight());
				temp = pointTemp;
			} else {
				moleEnergy += (temperature - temp) * sub.getSpecificHeat(pair.getRight());
				temp = pointTemp;
				break;
			}
		}
		if (temperature > temp) {
			moleEnergy += (temperature - temp) * sub.getSpecificHeat(points.get(points.size() - 1).getRight());
		}
		return moleEnergy;
	}

	public static PhasePortrait getNormal(Function<Float, SubPhasePortrait> function) {
		return new NormalPhasePortrait(function);
	}

	public static PhasePortrait getSimplified(SubPhasePortrait sub) {
		return new SimplifiedPhasePortrait(sub);
	}

	private static class NormalPhasePortrait extends PhasePortrait {
		private Function<Float, SubPhasePortrait> function;

		private NormalPhasePortrait(Function<Float, SubPhasePortrait> function) {
			this.function = function;
		}

		@Override
		public SubPhasePortrait getSubPhasePortrait(float pressure) {
			return this.function.apply(pressure);
		}
	}

	private static class SimplifiedPhasePortrait extends PhasePortrait {
		private SubPhasePortrait sub;

		private SimplifiedPhasePortrait(SubPhasePortrait sub) {
			this.sub = sub;
		}

		@Override
		public SubPhasePortrait getSubPhasePortrait(float pressure) {
			return this.sub;
		}
	}
}
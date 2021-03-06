package fractrace.passes;

import io.gsonfire.annotations.PostDeserialize;
import fractrace.Vector;
import fractrace.ImagePass;
import fractrace.TraceResult;
import static fractrace.Vector.*;

public class PhongLightingPass implements ImagePass {
	private Vector lightdir;
	private Vector colour;
	
	@Override
	public void execute(TraceResult target) {
		if (target.isFinite()) {
			// To do the lighting get the dot product of the 
			// normal and the light direction and clamp the
			// resulting value between 0 and 1
			final Vector normal = normalize(target.object
					.normalAtPoint(target.endpoint()));
			final double mult = clamp(dot(normal, lightdir), 0, 1);
			
			target.mult = mul(target.mult, mul(mult, colour));
		}
	}
	
	public PhongLightingPass() {
		// Initialize with some reasonable constants
		// if the user doesn't provide any
		lightdir = new Vector(0, 1, 0);
		colour = new Vector(1, 1, 1);
	}
	
	public PhongLightingPass(Vector unnorm_lightdir, Vector colour) {
		this.lightdir = normalize(unnorm_lightdir).negate();
		this.colour = colour;
	}
	
	@PostDeserialize
	private void deserializeHook() {
		// Setup the lightdir from the deserialized direction
		lightdir = normalize(lightdir).negate();
	}

	private static double clamp(double v, double min, double max) {
		return Math.min(Math.max(v, min), max);
	}
}

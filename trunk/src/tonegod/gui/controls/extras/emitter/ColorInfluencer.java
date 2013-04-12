/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter;

import com.jme3.math.ColorRGBA;
import tonegod.gui.controls.extras.emitter.ElementEmitter.ElementParticle;

/**
 *
 * @author t0neg0d
 */
public class ColorInfluencer implements Influencer {
	private boolean isEnabled = true;
	private ColorRGBA startColor = new ColorRGBA(ColorRGBA.Red);
	private ColorRGBA endColor = new ColorRGBA(ColorRGBA.White);
	
	@Override
	public void update(ElementParticle particle, float tpf) {
		if (isEnabled) {
			particle.color.interpolate(startColor, endColor, particle.blend);
		}
	}

	@Override
	public void initialize(ElementParticle particle) {
		particle.color.set(startColor);
	}

	@Override
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public boolean getIsEnabled() {
		return this.isEnabled;
	}
	
	public void setStartColor(ColorRGBA startColor) {
		this.startColor = startColor;
	}
	
	public void setEndColor(ColorRGBA endColor) {
		this.endColor = endColor;
	}
	
	@Override
	public ColorInfluencer clone() {
		ColorInfluencer clone = new ColorInfluencer();
		clone.setStartColor(startColor);
		clone.setEndColor(endColor);
		return clone;
	}
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.effects;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;
import tonegod.gui.core.Element;

/**
 *
 * @author t0neg0d
 */
public class Effect implements Cloneable {
	public enum EffectType {
		FadeIn,
		FadeOut,
		BlendTo,
		BlendFrom,
		ZoomIn,
		ZoomOut,
		SpinIn,
		SpinOut,
		Pulse,
		ColorSwap,
		PulseColor,
		ImageSwap
	}
	
	public enum EffectEvent {
		GetFocus,
		LoseFocus,
		Show,
		Hide,
		Hover,
		Press,
		Release,
		TabFocus,
		LoseTabFocus
	}
	
	private Element element;
	private EffectType type;
	private EffectEvent event;
	private float pass = 0.0f;
	private boolean direction = true;
	private float speed;
	private boolean isActive = true;
	private Texture blendImage;
	private ColorRGBA blendColor;
	private boolean init = false;
	private Vector2f def = new Vector2f();
	private Vector2f diff = new Vector2f();
	private Vector2f fract = new Vector2f();
	
	public Effect(EffectType type, EffectEvent event, float speed) {
		this.type = type;
		this.event = event;
		this.speed = speed;
	}
	
	public void setElement(Element element) {
		this.element = element;
	}
	
	public void setBlendImage(Texture blendImage) {
		this.blendImage = blendImage;
	}
	
	public void setColor(ColorRGBA blendColor) {
		this.blendColor = blendColor;
	}
	
	public Element getElement() {
		return this.element;
	}
	
	public boolean getIsActive() {
		return this.isActive;
	}
	
	public EffectEvent getEffectEvent() {
		return this.event;
	}
	
	public void update(float tpf) {
		if (type == EffectType.ZoomIn) {
			if (!init) {
				def.set(element.getPosition().clone());
				diff.set(element.getWidth()/2,element.getHeight()/2);
				Vector2f inc = new Vector2f(diff.x*pass,diff.y*pass);
				element.setPosition(def.add(diff.subtract(inc)));
				element.setLocalScale(pass);
				init = true;
			} else if (isActive) {
				Vector2f inc = new Vector2f(diff.x*pass,diff.y*pass);
				element.setPosition(def.add(diff.subtract(inc)));
				element.setLocalScale(pass);
			}
			if (pass >= 1.0) {
				element.setPosition(def);
				element.setLocalScale(pass);
			}
		} else if (type == EffectType.ZoomOut) {
			if (!init) {
				def.set(element.getPosition().clone());
				diff.set(element.getWidth()/2,element.getHeight()/2);
				Vector2f inc = new Vector2f(diff.x*pass,diff.y*pass);
				element.setPosition(def.subtract(inc));
				element.setLocalScale(1-pass);
				init = true;
			} else if (isActive) {
				Vector2f inc = new Vector2f(diff.x*pass,diff.y*pass);
				element.setPosition(def.subtract(inc));
				element.setLocalScale(1-pass);
			}
			if (pass >= 1.0) {
				element.hide();
				element.setPosition(def);
				element.setLocalScale(1);
			}
		} else if (type == EffectType.SpinIn) {
			if (!init) {
				def.set(element.getPosition().clone());
				diff.set(element.getWidth()/2,element.getHeight()/2);
				Vector2f inc = new Vector2f(diff.x*pass,diff.y*pass);
				element.setPosition(def.add(diff.subtract(inc)));
				element.setLocalScale(pass);
				init = true;
			} else if (isActive) {
				Vector2f inc = new Vector2f(diff.x*pass,diff.y*pass);
				element.setPosition(def.add(diff.subtract(inc)));
				element.setLocalScale(pass);
			}
			if (pass >= 1.0) {
				element.setPosition(def);
				element.setLocalScale(pass);
			}
			element.setLocalRotation(element.getLocalRotation().fromAngles(0, 0, 360*FastMath.DEG_TO_RAD*pass));
		} else if (type == EffectType.SpinOut) {
			if (!init) {
				def.set(element.getPosition().clone());
				diff.set(element.getWidth()/2,element.getHeight()/2);
				Vector2f inc = new Vector2f(diff.x*pass,diff.y*pass);
				element.setPosition(def.subtract(inc));
				element.setLocalScale(1-pass);
				init = true;
			} else if (isActive) {
				Vector2f inc = new Vector2f(diff.x*pass,diff.y*pass);
				element.setPosition(def.subtract(inc));
				element.setLocalScale(1-pass);
			}
			if (pass >= 1.0) {
				element.hide();
				element.setPosition(def);
				element.setLocalScale(1);
			}
			element.setLocalRotation(element.getLocalRotation().fromAngles(0, 0, 360*FastMath.DEG_TO_RAD*(1.0f-pass)));
		} else if (type == EffectType.BlendFrom) {
		//	if (pass == 0.0) element.getElementMaterial().setTexture("BlendImg", element.getHoverImg);
		//	element.getElementMaterial().setFloat("EffectPass", 1.0f-pass);
		} else if (type == EffectType.BlendTo) {
		//	if (pass == 0.0) element.getElementMaterial().setTexture("BlendImg", element.getHoverImg());
		//	element.getElementMaterial().setFloat("EffectPass", pass);
		} else if (type == EffectType.FadeIn) {
			if (!init) {
				element.getElementMaterial().setBoolean("UseEffect", true);
				element.getElementMaterial().setBoolean("EffectFade", true);
				element.getElementMaterial().setBoolean("EffectPulse", false);
			//	element.getElementMaterial().setTexture("EffectMap", blendImage);
				init = true;
			}
			element.getElementMaterial().setFloat("EffectStep", pass);
		} else if (type == EffectType.FadeOut) {
			if (!init) {
				element.getElementMaterial().setBoolean("UseEffect", true);
				element.getElementMaterial().setBoolean("EffectFade", true);
				element.getElementMaterial().setBoolean("EffectPulse", false);
			//	element.getElementMaterial().setTexture("EffectMap", blendImage);
				init = true;
			}
			element.getElementMaterial().setFloat("EffectStep", 1.0f-pass);
		} else if (type == EffectType.ImageSwap) {
			if (!init) {
				element.getElementMaterial().setBoolean("UseEffect", true);
				element.getElementMaterial().setBoolean("EffectFade", false);
				element.getElementMaterial().setBoolean("EffectPulse", false);
				element.getElementMaterial().setTexture("EffectMap", blendImage);
				element.getElementMaterial().setFloat("EffectStep", 1.0f);
				init = true;
				isActive = false;
			}
		} else if (type == EffectType.ColorSwap) {
			if (!init) {
				element.getElementMaterial().setBoolean("UseEffect", true);
				element.getElementMaterial().setBoolean("EffectFade", false);
				element.getElementMaterial().setBoolean("EffectPulse", false);
				element.getElementMaterial().setBoolean("EffectPulseColor", false);
				element.getElementMaterial().setColor("EffectColor", blendColor);
				element.getElementMaterial().setFloat("EffectStep", 1.0f);
				init = true;
				isActive = false;
			}
		}

		if (isActive) {
			if (type != EffectType.Pulse && type != EffectType.PulseColor) {
				pass += tpf*speed;
				if (pass >= 1.0) {

					pass = 1.0f;
					isActive = false;
				}
			} else {
				if (type == EffectType.Pulse) {
					if (pass >= 1.0f) {
						direction = false;
					} else if (pass <= -1.0f) {
						direction = true;
					}
					if (direction) pass += tpf*speed;
					else pass -= tpf*speed;
					if (!init) {
						element.getElementMaterial().setBoolean("UseEffect", true);
						element.getElementMaterial().setBoolean("EffectFade", false);
						element.getElementMaterial().setBoolean("EffectPulse", true);
						element.getElementMaterial().setBoolean("EffectPulseColor", false);
						element.getElementMaterial().setTexture("EffectMap", blendImage);
						init = true;
					}
					element.getElementMaterial().setFloat("EffectStep", pass);
				} else if (type == EffectType.PulseColor) {
					if (pass >= 1.0f) {
						direction = false;
					} else if (pass <= -1.0f) {
						direction = true;
					}
					if (direction) pass += tpf*speed;
					else pass -= tpf*speed;
					if (!init) {
						element.getElementMaterial().setBoolean("UseEffect", true);
						element.getElementMaterial().setBoolean("EffectFade", false);
						element.getElementMaterial().setBoolean("EffectPulse", false);
						element.getElementMaterial().setBoolean("EffectPulseColor", true);
						element.getElementMaterial().setColor("EffectColor", blendColor);
						init = true;
					}
					element.getElementMaterial().setFloat("EffectStep", pass);
				} else {
					pass += tpf*speed;
				}
			}
		}
	}
	
	public void resetShader() {
		element.getElementMaterial().setBoolean("UseEffect", false);
		element.getElementMaterial().setBoolean("EffectFade", false);
		element.getElementMaterial().setBoolean("EffectPulse", false);
		element.getElementMaterial().setTexture("EffectMap", null);
		element.getElementMaterial().setFloat("EffectStep", 0.0f);
	}
	
	@Override
	public Effect clone() {
		Effect effect = new Effect(
			this.type,
			this.event,
			this.speed
		);
		effect.setElement(this.element);
		return effect;
	}
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.buttons;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.text.Label;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.Effect;

/**
 *
 * @author t0neg0d
 */
public class RadioButton extends CheckBox {
	
	
	/**
	 * Creates a new instance of the Button control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public RadioButton(Screen screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("RadioButton").getVector2f("defaultSize"),
			screen.getStyle("RadioButton").getVector4f("resizeBorders"),
			screen.getStyle("RadioButton").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Button control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public RadioButton(Screen screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("RadioButton").getVector4f("resizeBorders"),
			screen.getStyle("RadioButton").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Button control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public RadioButton(Screen screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		this.clearAltImages();
		this.removeEffect(Effect.EffectEvent.Hover);
		this.removeEffect(Effect.EffectEvent.Press);
		this.removeEffect(Effect.EffectEvent.LoseFocus);
		
		setIsRadioButton(true);
		
		if (screen.getStyle("RadioButton").getString("hoverImg") != null) {
			setButtonHoverInfo(
				screen.getStyle("RadioButton").getString("hoverImg"),
				screen.getStyle("RadioButton").getColorRGBA("hoverColor")
			);
		}
		if (screen.getStyle("RadioButton").getString("pressedImg") != null) {
			setButtonPressedInfo(
				screen.getStyle("RadioButton").getString("pressedImg"),
				screen.getStyle("RadioButton").getColorRGBA("pressedColor")
			);
		}
		
		populateEffects("RadioButton");
	}
}

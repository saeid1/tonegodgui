/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.scrolling;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tonegod.gui.controls.menuing.Menu;
import tonegod.gui.controls.scrolling.ScrollArea;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class ScrollAreaAdapter extends ScrollArea {
	Map<String, ChildInfo> childInfo = new HashMap();
	List<Element> scrollableChildren = new ArrayList();
	
	/**
	 * Creates a new instance of the ScrollArea control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param isTextOnly Boolean defining if the scroll area will contain other Elements or use formatted text
	 */
	public ScrollAreaAdapter(Screen screen, String UID, Vector2f position, boolean isTextOnly) {
		this(screen, UID, position,
			screen.getStyle("ScrollArea").getVector2f("defaultSize"),
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg"),
			isTextOnly
		);
	}
	
	/**
	 * Creates a new instance of the ScrollArea control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param isTextOnly Boolean defining if the scroll area will contain other Elements or use formatted text
	 */
	public ScrollAreaAdapter(Screen screen, String UID, Vector2f position, Vector2f dimensions, boolean isTextOnly) {
		this(screen, UID, position, dimensions,
			screen.getStyle("ScrollArea").getVector4f("resizeBorders"),
			screen.getStyle("ScrollArea").getString("defaultImg"),
			isTextOnly
		);
	}
	
	/**
	 * Creates a new instance of the ScrollArea control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 * @param isTextOnly Boolean defining if the scroll area will contain other Elements or use formatted text
	 */
	public ScrollAreaAdapter (Screen screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, boolean isTextOnly) {
		super (screen, UID, position, dimensions, resizeBorders, defaultImg, false);
		scrollableArea.setText("");
		scrollableArea.setIgnoreMouse(true);
		scrollableArea.setTextPosition(8,8);
		setClipPadding(4);
		setPadding(0);
	}
	
	@Override
	public void addScrollableChild(Element child) {
		System.out.println("HERE");
		child.setDockS(true);
		child.setClippingLayer(this);
		child.setClipPadding(15);
		childInfo.put(child.getUID(), new ChildInfo(child.getPosition().x, child.getPosition().y, child.getDimensions().x, child.getDimensions().y));
		scrollableChildren.add(child);
		scrollableArea.addChild(child);
		pack();
	}
	
	@Override
	public void setText(String text) {
		scrollableArea.setText(text);
		pack();
	}
	
	private void pack() {
		scrollableArea.setHeight(0);
		
		float nWidth = 0;
		float nHeight = 0;
		
		Set<String> keys = childInfo.keySet();
		for (String key : keys) {
			ChildInfo el = childInfo.get(key);
			float w = el.x+el.w;
			float h = el.y+el.h;
			nWidth = (w > nWidth) ? w : nWidth;
			nHeight = (h > nHeight) ? h : nHeight;
		}
		
		this.resize(getAbsoluteWidth(), getAbsoluteHeight(), Borders.SE);
		
		scrollableArea.setWidth(getWidth());
		scrollableArea.setHeight(nHeight);
		
		if (scrollableArea.getTextElement().getHeight() > nHeight)
			scrollableArea.setHeight(scrollableArea.getTextElement().getHeight());
		
		for (Element el : scrollableChildren) {
			ChildInfo info = childInfo.get(el.getUID());
			el.setY(scrollableArea.getHeight()-info.y-50-getPadding());
		}
		
		scrollToTop();
	}
	
	public class ChildInfo {
		float x;
		float y;
		float w;
		float h;
		
		public ChildInfo(float x, float y, float w, float h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}
}

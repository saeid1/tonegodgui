/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.lists;

import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.effects.BatchEffect;
import tonegod.gui.effects.Effect;
import tonegod.gui.framework.core.util.GameTimer;

/**
 *
 * @author t0neg0d
 */
public class SlideTray extends Element {
	/*
	public static enum Orientation {
		VERTICAL,
		HORIZONTAL
	}
	*/
	
	public static enum ZOrderSort {
		FIRST_TO_LAST,
		LAST_TO_FIRST
	}
	private Orientation orientation;
	
	private ZOrderSort sort = ZOrderSort.FIRST_TO_LAST;
	
	private ButtonAdapter btnPrevElement, btnNextElement;
	private Element elTray;
	private float btnSize;
	
	protected List<Element> trayElements = new ArrayList();
	protected int currentElementIndex = 0;
	
	protected float trayPadding = 5;
	
	private boolean useSlideEffect = false;
	private Effect slideEffect;
	
	private BatchEffect batch = null;
	private GameTimer timer;
	
	private float currentOffset = 0;
						
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, Orientation orientation) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			orientation
		);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, Vector2f position, Orientation orientation) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			orientation
		);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, Vector2f position, Vector2f dimensions, Orientation orientation) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			orientation
		);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the SlideTray's track
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, Orientation orientation) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg, orientation);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, String UID, Vector2f position, Orientation orientation) {
		this(screen, UID, position,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			orientation
		);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Orientation orientation) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg"),
			orientation
		);
	}
	
	/**
	 * Creates a new instance of the SlideTray control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the SlideTray's track
	 * @param orientation The orientation of the SlideTray
	 */
	public SlideTray(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, Orientation orientation) {
		super(screen, UID, position, dimensions, resizeBorders, null);
		this.orientation = orientation;
		
		this.setDocking(Docking.NW);
		
		if (orientation == Orientation.HORIZONTAL) {
			this.setScaleEW(true);
			this.setScaleNS(false);
		} else {
			this.setScaleEW(false);
			this.setScaleNS(true);
		}
		this.setAsContainerOnly();
		
		initControl();
	}
	
	private void initControl() {
		btnSize = screen.getStyle("Button").getVector2f("defaultSize").y;
				
		slideEffect = new Effect(Effect.EffectType.SlideTo, Effect.EffectEvent.Show, .25f);
		timer = new GameTimer(.25f) {
			@Override
			public void onComplete(float time) {
				hideShowButtons();
			}
		};
		
		Vector2f pos = new Vector2f();
		Vector2f dim = new Vector2f();
		if (orientation == Orientation.HORIZONTAL) {
			pos.set(-btnSize,0);
			dim.set(btnSize, getHeight());
		} else {
			pos.set(0,-btnSize);
			dim.set(getWidth(),btnSize);
		}
			
		btnPrevElement = new ButtonAdapter(screen, getUID() + ":btnPrevElement",
			pos, dim, Vector4f.ZERO, null
		) {
			@Override
			public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean isToggled) {
				if (batch == null)
					prevElement();
				else if (!batch.getIsActive())
					prevElement();
			}
		};
		if (orientation == Orientation.HORIZONTAL)
			btnPrevElement.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowLeft"));
		else
			btnPrevElement.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowUp"));
		btnPrevElement.clearAltImages();
		btnPrevElement.setDocking(Docking.SW);
		btnPrevElement.setScaleEW(false);
		btnPrevElement.setScaleNS(false);
		
		if (orientation == Orientation.HORIZONTAL) {
			pos.set(getWidth(),0);
		} else {
			pos.set(0,getHeight());
		}
		
		btnNextElement = new ButtonAdapter(screen, getUID() + ":btnNextElement",
			pos, dim, Vector4f.ZERO, null
		) {
			@Override
			public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean isToggled) {
				if (batch == null)
					nextElement();
				else if (!batch.getIsActive())
					nextElement();
			}
		};
		if (orientation == Orientation.HORIZONTAL)
			btnNextElement.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		else
			btnNextElement.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowDown"));
		btnNextElement.clearAltImages();
		if (orientation == Orientation.HORIZONTAL)
			btnNextElement.setDocking(Docking.SE);
		else
			btnNextElement.setDocking(Docking.SW);
		btnNextElement.setScaleEW(false);
		btnNextElement.setScaleNS(false);
		
		elTray = new Element(screen, getUID() + ":elTray",
			new Vector2f(0,0),
			new Vector2f(getWidth(), getHeight()),
			new Vector4f(0,0,0,0),
			null
		) {
			@Override
			public void controlResizeHook() {
				if (orientation == Orientation.HORIZONTAL) {
					btnNextElement.setX(getWidth());
				} else {
					btnPrevElement.setY(getHeight());
				}
				hideShowButtons();
			}
		};
		elTray.setDocking(Docking.SW);
		if (orientation == Orientation.HORIZONTAL) {
			elTray.setScaleEW(true);
			elTray.setScaleNS(false);
		} else {
			elTray.setScaleEW(false);
			elTray.setScaleNS(true);
		}
		elTray.setAsContainerOnly();
		
		addChild(elTray);
		addChild(btnPrevElement);
		addChild(btnNextElement);
	}
	
	public void setButtonSize(float size) {
		if (orientation == Orientation.HORIZONTAL) {
			btnPrevElement.setHeight(size);
			btnPrevElement.getButtonIcon().centerToParentV();
			btnNextElement.setHeight(size);
			btnNextElement.getButtonIcon().centerToParentV();
		} else {
			btnPrevElement.setWidth(size);
			btnPrevElement.getButtonIcon().centerToParentH();
			btnNextElement.setWidth(size);
			btnNextElement.getButtonIcon().centerToParentH();
		}
	}
	
	public void alignButtonsV(VAlign vAlign) {
		if (vAlign == VAlign.Top) {
			btnPrevElement.setY(getHeight()-btnPrevElement.getHeight());
			btnNextElement.setY(getHeight()-btnNextElement.getHeight());
		} else if (vAlign == VAlign.Center) {
			btnPrevElement.centerToParentV();
			btnNextElement.centerToParentV();
		} else if (vAlign == VAlign.Center) {
			btnPrevElement.setY(0);
			btnNextElement.setY(0);
		}	
	}
	
	public void alignButtonsH(Align align) {
		if (align == Align.Right) {
			btnPrevElement.setX(getWidth()-btnPrevElement.getWidth());
			btnNextElement.setX(getWidth()-btnNextElement.getWidth());
		} else if (align == Align.Center) {
			btnPrevElement.centerToParentH();
			btnNextElement.centerToParentH();
		} else if (align == Align.Left) {
			btnPrevElement.setX(0);
			btnNextElement.setX(0);
		}	
	}
	
	public void setZOrderSorting(ZOrderSort sort) {
		this.sort = sort;
	}
	
	public void resort(Element toFront) {
		float step = screen.getZOrderStepMinor();
		if (sort == ZOrderSort.FIRST_TO_LAST) {
			for (int i = 0; i < trayElements.size(); i++) {
				Element el = trayElements.get(i);
				el.setLocalTranslation(el.getLocalTranslation().setZ(step));
				step += screen.getZOrderStepMinor();
			}
		} else if (sort == ZOrderSort.LAST_TO_FIRST) {
			for (int i = trayElements.size()-1; i >= 0; i--) {
				Element el = trayElements.get(i);
				if (el != toFront) {
					el.setLocalTranslation(el.getLocalTranslation().setZ(step));
					step += screen.getZOrderStepMinor();
				}
			}
		}
		toFront.setLocalTranslation(toFront.getLocalTranslation().setZ(step));
	}
	
	/**
	 * Returns the current slide tray padding value
	 * @return 
	 */
	public float getTrayPadding() { return this.trayPadding; }
	
	/**
	 * Sets the padding between slide tray elements
	 * @param trayPadding 
	 */
	public void setTrayPadding(float trayPadding) {
		this.trayPadding = trayPadding;
	}
	
	/**
	 * Enables/disables the use of the SlideTo effect when using next/previous buttons
	 * @param useSlideEffect 
	 */
	public void setUseSlideEffect(boolean useSlideEffect) {
		this.useSlideEffect = useSlideEffect;
	}
	
	/**
	 * Adds the provided Element as a tray item
	 * @param element 
	 */
	public void addTrayElement(Element element) {
		if (orientation == Orientation.HORIZONTAL)
			element.setPosition(getNextPosition(),0);
		else
			element.setPosition(0,getNextPosition());
	//	element.setClippingLayer(elTray);
		element.addClippingLayer(elTray);
		element.setDocking(Docking.SW);
		element.setScaleEW(false);
		element.setScaleNS(false);
		trayElements.add(element);
		elTray.addChild(element);
		hideShowButtons();
	}
	
	private void nextElement() {
		if (currentElementIndex+1 < trayElements.size()) {
			if (useSlideEffect) {
				batch = new BatchEffect();
				float diff = getNextOffset(true);
				for (Element el : trayElements) {
					if (orientation == Orientation.HORIZONTAL) {
						Vector2f destination = new Vector2f(el.getX()-diff,el.getY());
						Effect effect = slideEffect.clone();
						effect.setElement(el);
						effect.setEffectDestination(destination);
						batch.addEffect(effect);
					} else {
						Vector2f destination = new Vector2f(el.getX(),el.getY()+diff);
						Effect effect = slideEffect.clone();
						effect.setElement(el);
						effect.setEffectDestination(destination);
						batch.addEffect(effect);
					}
				}
				screen.getEffectManager().applyBatchEffect(batch);
			} else {
				for (Element el : trayElements) {
					if (orientation == Orientation.HORIZONTAL) {
						el.setX(el.getX()-(trayElements.get(currentElementIndex).getWidth()+trayPadding));
					} else {
						el.setY(el.getY()+(trayElements.get(currentElementIndex).getHeight()+trayPadding));
					}
				}
			}
			currentElementIndex++;
			if (useSlideEffect) {
				timer.reset(false);
				screen.getAnimManager().addGameTimer(timer);
			} else
				hideShowButtons();
		}
	}
	
	private void prevElement() {
		if (currentElementIndex-1 > -1) {
			if (useSlideEffect) {
				batch = new BatchEffect();
				float diff = getNextOffset(false);
				for (Element el : trayElements) {
					if (orientation == Orientation.HORIZONTAL) {
						Vector2f destination = new Vector2f(el.getX()+diff,el.getY());
						Effect effect = slideEffect.clone();
						effect.setElement(el);
						effect.setEffectDestination(destination);
						batch.addEffect(effect);
					} else {
						Vector2f destination = new Vector2f(el.getX(),el.getY()-diff);
						Effect effect = slideEffect.clone();
						effect.setElement(el);
						effect.setEffectDestination(destination);
						batch.addEffect(effect);
					}
				}
				screen.getEffectManager().applyBatchEffect(batch);
			} else {
				for (Element el : trayElements) {
					if (orientation == Orientation.HORIZONTAL) {
						el.setX(el.getX()+(trayElements.get(currentElementIndex-1).getWidth()+trayPadding));
					} else {
						el.setY(el.getY()-(trayElements.get(currentElementIndex-1).getHeight()+trayPadding));
					}
				}
			}
			currentElementIndex--;
			if (useSlideEffect) {
				timer.reset(false);
				screen.getAnimManager().addGameTimer(timer);
			} else
				hideShowButtons();
		}
	}
	
	private float getNextOffset(boolean dir) {
		float diff, end, offset;
		if (dir) {
			if (orientation == Orientation.HORIZONTAL) {
				diff = (trayElements.get(currentElementIndex).getWidth()+trayPadding);
			} else {
				diff = (trayElements.get(currentElementIndex).getHeight()+trayPadding);
			}
		} else {
			if (orientation == Orientation.HORIZONTAL) {
				diff = FastMath.abs(trayElements.get(currentElementIndex-1).getX());
			} else {
				diff = FastMath.abs(trayElements.get(currentElementIndex-1).getY());
			}
		}
		
		if (orientation == Orientation.HORIZONTAL) {
			if (dir) {
				Element el = trayElements.get(trayElements.size()-1);
				end = el.getX()+el.getWidth()+trayPadding;
				end -= diff;
				offset = elTray.getWidth()-end;
				if (offset > 0)
					diff -= offset;
				diff = FastMath.floor(diff);
			} else {
				Element el = trayElements.get(0);
				end = el.getWidth()+trayPadding;
				end -= diff;
				offset = end;
				if (offset > 0)
					diff -= offset;
				diff = FastMath.ceil(diff);
			}
		} else {
			if (dir) {
				Element el = trayElements.get(trayElements.size()-1);
				end = el.getY();
				end += diff;
				offset = end;
				if (offset > 0)
					diff -= offset;
				diff = FastMath.ceil(diff);
			} else {
				Element el = trayElements.get(currentElementIndex-1);
				end = el.getY()+el.getHeight();
				end -= diff;
				offset = elTray.getHeight()-end;
				if (offset > 0)
					diff -= offset;
				diff = FastMath.floor(diff);
			}
		}
		
		return diff;
	}
	
	private void hideShowButtons() {
		if (currentElementIndex == 0)
			btnPrevElement.hide();
		else
			btnPrevElement.show();
		Element el = trayElements.get(trayElements.size()-1);
		if (orientation == Orientation.HORIZONTAL) {
			if (el.getX()+el.getWidth() <= elTray.getWidth()+5)
				btnNextElement.hide();
			else
				btnNextElement.show();
		} else {
			if (el.getY() >= 0)
				btnNextElement.hide();
			else
				btnNextElement.show();
		}
		if (!trayElements.isEmpty())
			currentOffset = (elTray.getHeight()-trayElements.get(0).getY());
	}
	
	private float getNextPosition() {
		float ret = 0;
		for (Element el : trayElements) {
			if (orientation == Orientation.HORIZONTAL) {
				ret += el.getWidth()+trayPadding;
			} else {
				ret += el.getHeight()+trayPadding;
			}
		}
		return ret;
	}
	
	@Override
	public void setControlClippingLayer(Element clippingLayer) {
		addClippingLayer(clippingLayer);
	}
}

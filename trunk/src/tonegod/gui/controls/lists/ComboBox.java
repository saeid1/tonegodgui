/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.lists;

import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.menuing.Menu;
import tonegod.gui.controls.menuing.MenuItem;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public abstract class ComboBox extends TextField {
	private ButtonAdapter btnArrowDown;
	private Menu DDList = null;
	float btnHeight;
	String ddUID;
	private int selectedIndex = -1;
	private String selectedValue;
	private String selectedCaption;
	
	private int hlIndex;
	private String hlValue;
	private String hlCaption;
	
	private int ssIndex;
	private String ssValue;
	private String ssCaption;
	
	/**
	 * Creates a new instance of the ComboBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ComboBox(Screen screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("TextField").getVector2f("defaultSize"),
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ComboBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ComboBox(Screen screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("TextField").getVector4f("resizeBorders"),
			screen.getStyle("TextField").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ComboBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public ComboBox(Screen screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		setScaleNS(false);
		setScaleEW(false);
		
		ddUID = UID + ":ddMenu";
		
		btnHeight = screen.getStyle("Common").getFloat("defaultControlSize");
		
		btnArrowDown = new ButtonAdapter(screen, UID + ":ArrowDown",
			new Vector2f(
				getWidth(),
				0
			),
			new Vector2f(
				btnHeight,
				btnHeight
			)
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				Menu m = ((Menu)screen.getElementById(ddUID));
				m.showMenu(null, getElementParent().getAbsoluteX(), getElementParent().getAbsoluteY()-m.getHeight());
				screen.setTabFocusElement((ComboBox)getElementParent());
			}
		};
		btnArrowDown.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowDown"));
		btnArrowDown.setDockS(true);
		btnArrowDown.setDockW(true);
		this.addChild(btnArrowDown);
	}
	
	/**
	 * Adds a new list item to the drop-down list associated with this control
	 * 
	 * @param caption The String to display as the list item
	 * @param value A String value to associate with this list item
	 */
	public void addListItem(String caption, String value) {
		if (DDList == null) {
			DDList = new Menu(screen, ddUID, new Vector2f(0,0), true) {
				@Override
				public void onMenuItemClicked(int index, String value) {
					((ComboBox)getCallerElement()).setSelected(index, DDList.getMenuItem(index).getCaption(), value);
					screen.setTabFocusElement(((ComboBox)getCallerElement()));
					hide();
				}
			};
			DDList.setCallerElement(this);
		}
		DDList.addMenuItem(caption, value, null);
		
		if (DDList.getParent() == null) {
			screen.addElement(DDList);
			DDList.hide();
		}
	//	pack();
	}
	
	/**
	 * Method needs to be called once last list item has been added.  This eventually
	 * will be updated to automatically be called when a new item is added to, instert into
	 * the list or an item is removed from the list.
	 */
	public void pack() {
		DDList.pack();
		
		DDList.setIsResizable(true);
		DDList.setResizeN(false);
		DDList.setResizeW(false);
		DDList.setResizeE(true);
		DDList.setResizeS(true);
		if (DDList.getWidth() < getWidth())
			DDList.resize(getWidth()+btnHeight, DDList.getMenuItemHeight()*5, Borders.SE);
		else
			DDList.resize(DDList.getWidth(), DDList.getMenuItemHeight()*5, Borders.SE);
		DDList.getVScrollBar().setX(DDList.getWidth());
		DDList.setResizeE(false);
		
		if (selectedIndex == -1) {
			setSelectedIndex(0);
		}
	//	screen.addElement(DDList);
	//	DDList.hide();
	}
	
	public void setSelectedIndex(int selectedIndex) {
		if (selectedIndex < 0)
			selectedIndex = 0;
		else if (selectedIndex > DDList.getMenuItems().size()-1)
			selectedIndex = DDList.getMenuItems().size()-1;
		
		MenuItem mi = DDList.getMenuItem(selectedIndex);
		String caption = mi.getCaption();
		String value = mi.getValue();
		setSelected(selectedIndex, caption, value);
	}
	
	protected void setSelected(int index, String caption, String value) {
		this.selectedIndex = index;
		this.selectedCaption = caption;
		this.selectedValue = value;
		setTextFieldText(selectedCaption);
		onChange(selectedIndex, selectedValue);
	}
	
	public void hideDropDownList() {
		this.DDList.hideMenu();
	}
	
	@Override
	public void controlKeyPressHook(KeyInputEvent evt, String text) {
		if (evt.getKeyCode() != KeyInput.KEY_UP && evt.getKeyCode() != KeyInput.KEY_DOWN && evt.getKeyCode() != KeyInput.KEY_RETURN) {
			int miIndexOf = 0;
			int strIndex = -1;
			for (MenuItem mi : DDList.getMenuItems()) {
				strIndex = mi.getCaption().toLowerCase().indexOf(text.toLowerCase());
				if (strIndex == 0) {
					ssIndex = miIndexOf;
					hlIndex = ssIndex;
					hlCaption = ssCaption = DDList.getMenuItem(miIndexOf).getCaption();
					hlValue = ssValue = DDList.getMenuItem(miIndexOf).getValue();
					
					int rIndex = DDList.getMenuItems().size()-miIndexOf;
					float diff = rIndex * DDList.getMenuItemHeight() + (DDList.getMenuPadding()*2);
					
					DDList.scrollThumbYTo(
						( DDList.getHeight()-diff )
					);
					break;
				}
				miIndexOf++;
			}
			if (miIndexOf > -1 && miIndexOf < DDList.getMenuItems().size()-1)
				handleHightlight(miIndexOf);
			if (!DDList.getIsVisible() && evt.getKeyCode() != KeyInput.KEY_LSHIFT && evt.getKeyCode() != KeyInput.KEY_RSHIFT) DDList.showMenu(null, getAbsoluteX(), getAbsoluteY()-DDList.getHeight());
		} else {
			if (evt.getKeyCode() == KeyInput.KEY_UP) {
				if (hlIndex > 0) {
					hlIndex--;
					hlCaption = DDList.getMenuItem(hlIndex).getCaption();
					hlValue = DDList.getMenuItem(hlIndex).getValue();
					int rIndex = DDList.getMenuItems().size()-hlIndex;
					float diff = rIndex * DDList.getMenuItemHeight() + (DDList.getMenuPadding()*2);
					
					DDList.scrollThumbYTo(
						( DDList.getHeight()-diff )
					);
					handleHightlight(hlIndex);
					setSelected(hlIndex, hlCaption, hlValue);
				}
			} else if (evt.getKeyCode() == KeyInput.KEY_DOWN) {
				if (hlIndex < DDList.getMenuItems().size()-1) {
					hlIndex++;
					hlCaption = DDList.getMenuItem(hlIndex).getCaption();
					hlValue = DDList.getMenuItem(hlIndex).getValue();
					int rIndex = DDList.getMenuItems().size()-hlIndex;
					float diff = rIndex * DDList.getMenuItemHeight() + (DDList.getMenuPadding()*2);
					
					DDList.scrollThumbYTo(
						( DDList.getHeight()-diff )
					);
					handleHightlight(hlIndex);
					setSelected(hlIndex, hlCaption, hlValue);
				}
			}
			if (evt.getKeyCode() == KeyInput.KEY_RETURN) {
				updateSelected();
			}
		}
	}
	
	private void updateSelected() {
		setSelected(hlIndex, hlCaption, hlValue);
		if (DDList.getIsVisible()) DDList.hide();
	}
	
	private void handleHightlight(int index) {
		if (DDList.getIsVisible()) DDList.setHighlight(index);
	}
	
	public abstract void onChange(int selectedIndex, String value);
	
	public int getSelectIndex() {
		return this.selectedIndex;
	}
	
	public MenuItem getSelectedListItem() {
		return this.DDList.getMenuItem(selectedIndex);
	}
	
	public MenuItem getListItemByIndex(int index) {
		return this.DDList.getMenuItem(index);
	}
	
	@Override
	public void controlTextFieldResetTabFocusHook() {
	//	DDList.hideMenu();
	}
}
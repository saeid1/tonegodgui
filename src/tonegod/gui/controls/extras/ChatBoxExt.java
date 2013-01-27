/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tonegod.gui.controls.buttons.Button;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.lists.Spinner;
import tonegod.gui.controls.scrolling.ScrollArea;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class ChatBoxExt extends Panel {
	ScrollArea saChatArea;
	TextField tfChatInput;
	Button btnChatSendMsg;
	Spinner spnChannels;
	SelectBox sbDefaultChannel;
	
	int sendKey;
	int chatHistorySize = 30;
	List<ChatMessage> chatMessages = new ArrayList();
	
	Map<String, ChatChannel> channels = new HashMap();
	String defaultCommand;
	
	List<Label> displayMessages = new ArrayList();
	List<ChatChannel> filteredChannels = new ArrayList();
	
	/**
	 * Creates a new instance of the ChatBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ChatBoxExt(Screen screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ChatBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ChatBoxExt(Screen screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ChatBox control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public ChatBoxExt(Screen screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		this.setIsMovable(true);
		this.setIsResizable(true);
		this.setScaleNS(false);
		this.setScaleEW(false);
		
		spnChannels = new Spinner(
			screen,
			UID + ":Channels",
			new Vector2f(getWidth()-120-30,10),
			new Vector2f(120, 20),
			Spinner.Orientation.HORIZONTAL,
			true
		) {
			@Override
			public void onChange(int selectedIndex, String value) {
				addFilteredChannel(value);
			}
		};
		spnChannels.setFontSize(16);
		spnChannels.setDockN(true);
		spnChannels.setDockE(true);
		spnChannels.setScaleEW(false);
		spnChannels.setScaleNS(false);
		spnChannels.addStepValue("All");
		
		addChild(spnChannels);
		
		saChatArea = new ScrollArea(screen, UID + ":ChatArea", new Vector2f(10, 35), new Vector2f(getWidth()-45, getHeight()-85), false) {
			@Override
			public void controlResizeHook() {
				float totalHeight = 0;
				int index = 0;
				for (Label l : displayMessages) {
					l.setHeight(l.getTextElement().getHeight());
					totalHeight += l.getHeight();
					index++;
				}
				if (totalHeight > saChatArea.getHeight()) {
					saChatArea.getScrollableArea().setHeight(totalHeight+(saChatArea.getPadding()*2));
				}
				totalHeight = 0;
				for (Label l : displayMessages) {
					totalHeight += l.getHeight();
					l.setY(saChatArea.getScrollableArea().getHeight()-totalHeight);
				}
			}
		};
		saChatArea.setFontColor(ColorRGBA.LightGray);
		saChatArea.setTextAlign(BitmapFont.Align.Left);
		saChatArea.setTextPosition(5,5);
		saChatArea.setTextWrap(LineWrapMode.Word);
		saChatArea.setIsResizable(false);
		saChatArea.setScaleEW(true);
		saChatArea.setScaleNS(true);
		saChatArea.setClippingLayer(saChatArea);
		saChatArea.setPadding(5);
		saChatArea.setText("");
		addChild(saChatArea);
		
		sbDefaultChannel = new SelectBox(
			screen,
			UID + ":DefaultChannel",
			new Vector2f(10, getHeight()-25-15),
			new Vector2f(75, 25)
		) {
			@Override
			public void onChange(int selectedIndex, String value) {
			//	throw new UnsupportedOperationException("Not supported yet.");
			}
		};
		sbDefaultChannel.setDockS(true);
		sbDefaultChannel.setDockW(true);
		sbDefaultChannel.setScaleEW(false);
		sbDefaultChannel.setScaleNS(false);
		addChild(sbDefaultChannel);
	//	sbDefaultChannel.pack();
		
		tfChatInput = new TextField(
			screen,
			UID + ":ChatInput",
			new Vector2f(110, getHeight()-25-15),
			new Vector2f(getWidth()-20-195, 25)
		) {
			@Override
			public void controlKeyPressHook(KeyInputEvent evt, String text) {
				if (evt.getKeyCode() == sendKey) {
					if (tfChatInput.getText().length() > 0) {
						tfChatInput.setText(tfChatInput.getText().substring(0,tfChatInput.getText().length()-1));
						sendMsg();
					}
				}
			}
		};
		tfChatInput.setScaleEW(true);
		tfChatInput.setScaleNS(false);
		tfChatInput.setDockS(true);
		tfChatInput.setDockW(true);
		
		btnChatSendMsg = new Button(
			screen,
			UID + ":ChatSendMsg",
			new Vector2f(getWidth()-10-100, getHeight()-25-15),
			new Vector2f(100,25)
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				sendMsg();
			}
		};
		btnChatSendMsg.setScaleEW(false);
		btnChatSendMsg.setScaleNS(false);
		btnChatSendMsg.setDockS(true);
		btnChatSendMsg.setDockE(true);
		btnChatSendMsg.setText("Send");
		
		addChild(btnChatSendMsg);
		addChild(tfChatInput);
		
		populateEffects("Window");
		
		this.addChatChannel("Default", "/say", ColorRGBA.White, true);
	}
	
	private void sendMsg() {
		if (tfChatInput.getText().length() > 0) {
			if (!tfChatInput.getText().equals("")) {
				String command = sbDefaultChannel.getSelectedListItem().getValue();
				onSendMsg(command, tfChatInput.getText());
				tfChatInput.setTextFieldText("");
			}
		}
	}
	
	public void receiveMsg(String command, String msg) {
		System.out.println(command);
		ChatChannel channel = getChannelByCommand(command);
		chatMessages.add(new ChatMessage(channel, msg));
		updateChatHistory();
	}
	
	private void updateChatHistory() {
		if (chatMessages.size() > chatHistorySize) {
			chatMessages.remove(0);
		}
		rebuildChat();
	}
	
	private void rebuildChat() {
		String displayText = "";
		int index = 0;
		saChatArea.getScrollableArea().removeAllChildren();
		saChatArea.getScrollableArea().setIgnoreMouse(true);
		saChatArea.getScrollableArea().setY(0);
		saChatArea.getScrollableArea().setHeight(saChatArea.getHeight());
		saChatArea.getScrollableArea().setDockS(true);
		displayMessages.clear();
		
		float totalHeight = 0;
		for (ChatMessage cm : chatMessages) {
			if (!filteredChannels.contains(cm.getChannel())) {
				Label l = createMessageLabel(index, cm);
				displayMessages.add(l);
				saChatArea.addScrollableChild(l);
				l.setHeight(l.getTextElement().getHeight());
				totalHeight += l.getHeight();
			//	l.setY(saChatArea.getScrollableArea().getHeight()-totalHeight);
				index++;
			}
		}
		if (totalHeight > saChatArea.getHeight()) {
			saChatArea.getScrollableArea().setHeight(totalHeight+(saChatArea.getPadding()*2));
		}
		totalHeight = 0;
		for (Label l : displayMessages) {
			totalHeight += l.getHeight();
			l.setY(saChatArea.getScrollableArea().getHeight()-totalHeight);
		}
		saChatArea.scrollToBottom();
	}
	
	private Label createMessageLabel(int index, ChatMessage cm) {
		String s = cm.getMsg();
		Label l = new Label(
			screen,
			getUID() + ":Label" + index,
			new Vector2f(0, 0),
			new Vector2f(saChatArea.getWidth(),25)
		);
		l.setTextWrap(LineWrapMode.Word);
		l.setScaleEW(true);
		l.setScaleNS(false);
		l.setDockS(true);
		l.setDockW(true);
		l.setIsResizable(false);
		l.setIsMovable(false);
		l.setIgnoreMouse(true);
		l.setClippingLayer(saChatArea);
		l.setFontColor(cm.getChannel().getColor());
		l.setFontSize(saChatArea.getFontSize());
		l.setText("[" + cm.getChannel().getName() + "] " + s);
		l.setHeight(l.getTextElement().getLineHeight()*l.getTextElement().getLineCount());
		l.setIgnoreMouse(true);
		
		return l;
	}
	
	public void setSendKey(int sendKey) {
		this.sendKey = sendKey;
	}
	
	public void onSendMsg(String command, String msg) {  }
	
	public final void addChatChannel(String name, String command, ColorRGBA color, boolean visibleToUser) {
		channels.put(command, new ChatChannel(name, command, color, visibleToUser));
		this.spnChannels.addStepValue(name);
		if (visibleToUser) {
			this.sbDefaultChannel.addListItem(name, command);
			this.sbDefaultChannel.pack();
		}
	}
	
	private ChatChannel getChannelByCommand(String command) {
		ChatChannel c = null;
		Set<String> keys = channels.keySet();
		for (String key : keys) {
			if (channels.get(key).getCommand().equals(command)) {
				c = channels.get(key);
				break;
			}
		}
		return c;
	}
	
	private ChatChannel getChannelByName(String name) {
		ChatChannel c = null;
		Set<String> keys = channels.keySet();
		for (String key : keys) {
			if (channels.get(key).getName().equals(name)) {
				c = channels.get(key);
				break;
			}
		}
		return c;
	}
	
	public class ChatMessage {
		private ChatChannel channel;
		private String msg;
		public ChatMessage(ChatChannel channel, String msg) {
			this.channel = channel;
			this.msg = msg;
		}
		
		public ChatChannel getChannel() {
			return channel;
		}
		public String getMsg() {
			return this.msg;
		}
	}
	
	public class ChatChannel {
		private String name, command;
		private ColorRGBA color;
		private boolean visibleToUser;
		public ChatChannel(String name, String command, ColorRGBA color, boolean visibleToUser) {
			this.name = name;
			this.command = command;
			this.color = color;
			this.visibleToUser = visibleToUser;
		}
		
		public String getName() {
			return this.name;
		}
		public String getCommand() {
			return this.command;
		}
		public ColorRGBA getColor() {
			return this.color;
		}
	}
	
	public void addFilteredChannel(String name) {
		filteredChannels.clear();
		filteredChannels.add(this.getChannelByName(name));
		rebuildChat();
	}
}
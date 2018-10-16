package net.hdt.neutronia.base.client.gui.listener;

import net.hdt.neutronia.base.client.gui.components.BaseComponent;

@FunctionalInterface
public interface IMouseDragListener extends IListenerBase {
	public void componentMouseDrag(BaseComponent component, int x, int y, int button, long time);
}
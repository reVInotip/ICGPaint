package controller.toolsWorker;

import controller.tools.Tool;
import event.Observer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolsManager {
    private final HashMap<String, Tool> tools;
    private String usedTool = "undefined";

    private class ToolsAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);

            if (tools.containsKey(usedTool)) {
                tools.get(usedTool).mousePressed(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);

            if (tools.containsKey(usedTool)) {
                tools.get(usedTool).mouseReleased(e);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);

            if (tools.containsKey(usedTool)) {
                tools.get(usedTool).mouseDragged(e);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            if (tools.containsKey(usedTool)) {
                tools.get(usedTool).mouseClicked(e);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);

            if (tools.containsKey(usedTool)) {
                tools.get(usedTool).mouseMoved(e);
            }
        }
    }

    private final ToolsAdapter toolsAdapter = new ToolsAdapter();

    public ToolsManager() {
        ToolsFactory.initFactory();
        tools = ToolsFactory.createTools();
    }

    public MouseAdapter getToolsAdapter() {
        return toolsAdapter;
    }

    public Map<String, String> getToolsDescription() {
        return ToolsFactory.toolsDescr;
    }

    public Map<String, String> getToolsIcons() {
        return ToolsFactory.toolsIcons;
    }

    public List<String> getAvailableTools() {
        return new ArrayList<>(tools.keySet());
    }

    public void addToolObserver(String tool, Observer observer) {
        if (tools.containsKey(tool)) {
            tools.get(tool).add(observer);
        }
    }

    public void switchTool(String tool) {
        usedTool = tool;
    }

}

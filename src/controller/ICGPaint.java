package controller;

import controller.toolsWorker.ToolsManager;
import event.Observer;
import view.InitMainFrame;

public class ICGPaint {
    private final InitMainFrame mainFrame = new InitMainFrame();
    private final ToolsManager toolManager = new ToolsManager();

    public void run() {
        subscribeOnTools();
        createToolsButtons();

        mainFrame.addDrawPanelMouseListener(toolManager.getToolsAdapter());
        mainFrame.addDrawPanelMouseMotionListener(toolManager.getToolsAdapter());

        mainFrame.showFrame();
    }

    private void subscribeOnTools() {
        for (Observer observer: mainFrame.getToolsObservers()) {
            for (String tool: toolManager.getAvailableTools()) {
                toolManager.addToolObserver(tool, observer);
            }
        }
    }

    private void createToolsButtons() {
        mainFrame.addToolsButtons(toolManager.getAvailableTools().toArray(new String[0]));
        for (String tool: toolManager.getAvailableTools()) {
            mainFrame.addButtonActionListener(tool, actionEvent -> toolManager.switchTool(tool));
        }
    }
}

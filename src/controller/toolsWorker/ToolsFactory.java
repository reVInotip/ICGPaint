package controller.toolsWorker;

import controller.ToolView;
import controller.tools.Tool;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

class ToolsFactory {
    static private final Map<String, Class<Tool>> toolClasses = new HashMap<>();
    static private final String RESOURCE_FILE_NAME = "./tools.conf";

    static public void initFactory() {
        try(InputStream stream = ToolsFactory.class.getResourceAsStream(RESOURCE_FILE_NAME)) {
            if (stream == null) {
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            for (String classPath; (classPath = reader.readLine()) != null; ) {
                Class<?> tool = Class.forName(classPath);
                if (tool.getSuperclass().getName().equals(Tool.class.getName())) {
                    toolClasses.put(tool.getAnnotation(ToolView.class).name(), (Class<Tool>) tool);
                }
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new RuntimeException(e);
        }
    }

    static public HashMap<String, Tool> createTools() {
        HashMap<String, Tool> toolHashMap = HashMap.newHashMap(toolClasses.size());
        for (Map.Entry<String, Class<Tool>> item: toolClasses.entrySet()) {
            toolHashMap.put(item.getKey(), createTool(item.getValue()));
        }

        return toolHashMap;
    }

    static private Tool createTool(Class<Tool> toolClass) {
        try {
            return toolClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.err.println("Class not found");
            throw new RuntimeException(e);
        }
    }
}
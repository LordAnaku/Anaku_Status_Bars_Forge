package io.github.lordanaku.anaku_status_bars.screen.gui;

import com.google.gson.*;
import io.github.lordanaku.anaku_status_bars.AnakuStatusBarsCore;
import io.github.lordanaku.anaku_status_bars.screen.hud.RenderHudHelper;
import io.github.lordanaku.anaku_status_bars.utils.Settings;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ConfigFileHandler {

    public static void readFromConfig() {
        JsonObject root = new JsonObject();
        try (FileReader file = new FileReader(getConfigFile())) {
            root = JsonParser.parseReader(file).getAsJsonObject();
        } catch (IOException e) {
            AnakuStatusBarsCore.LOGGER.error(e.getMessage());
        }

        /* * Load settings * */
        if (root.has("render_settings")) {
            JsonObject object = root.get("render_settings").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                if (Settings.shouldRenderSettings.containsKey(entry.getKey())) {
                    Settings.shouldRenderSettings.replace(entry.getKey(), entry.getValue().getAsBoolean());
                } else {
                    Settings.shouldRenderSettings.put(entry.getKey(), entry.getValue().getAsBoolean());
                }
            }
        }
        if(root.has("color_settings")) {
            JsonObject object = root.get("color_settings").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                if (Settings.colorSettings.containsKey(entry.getKey())) {
                    Settings.colorSettings.replace(entry.getKey(), entry.getValue().getAsInt());
                } else {
                    Settings.colorSettings.put(entry.getKey(), entry.getValue().getAsInt());
                }
            }
        }
        if(root.has("alpha_settings")) {
            JsonObject object = root.get("alpha_settings").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                if (Settings.alphaSettings.containsKey(entry.getKey())) {
                    Settings.alphaSettings.replace(entry.getKey(), entry.getValue().getAsFloat());
                } else {
                    Settings.alphaSettings.put(entry.getKey(), entry.getValue().getAsFloat());
                }
            }
        }
        if(root.has("render_icon_settings")) {
            JsonObject object = root.get("render_icon_settings").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                if (Settings.shouldRenderIconSettings.containsKey(entry.getKey())) {
                    Settings.shouldRenderIconSettings.replace(entry.getKey(), entry.getValue().getAsBoolean());
                } else {
                    Settings.shouldRenderIconSettings.put(entry.getKey(), entry.getValue().getAsBoolean());
                }
            }
        }
        if(root.has("render_side")) {
            JsonObject object = root.get("render_side").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                Settings.sideOrderSettings.replace(entry.getKey(), getStringList(entry.getValue().getAsJsonArray()));
            }
        }
        if(root.has("offsets")) {
            JsonObject object = root.get("offsets").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                Settings.positionSettings.replace(entry.getKey(), entry.getValue().getAsInt());
            }
        }
        if(root.has("enable_vanilla_textures")) {
            Settings.shouldUseVanillaTextures = root.get("enable_vanilla_textures").getAsBoolean();
        }
    }

    public static void writeToConfig() {
        JsonObject root = new JsonObject();
        Gson gson = new Gson();

        /* * Save settings * */
        root.add("render_settings", gson.toJsonTree(Settings.shouldRenderSettings));
        root.add("color_settings", gson.toJsonTree(Settings.colorSettings));
        root.add("alpha_settings", gson.toJsonTree(Settings.alphaSettings));
        root.add("render_icon_settings", gson.toJsonTree(Settings.shouldRenderIconSettings));
        root.add("render_side", gson.toJsonTree(Settings.sideOrderSettings));
        root.add("offsets", gson.toJsonTree(Settings.positionSettings));
        root.addProperty("enable_vanilla_textures", Settings.shouldUseVanillaTextures);

        RenderHudHelper.setupHudElements();

        try (FileWriter file = new FileWriter(getConfigFile())) {
            file.write(new GsonBuilder().setPrettyPrinting().create().toJson(root));
            file.flush();
        } catch (IOException e) {
            AnakuStatusBarsCore.LOGGER.error(e.getMessage());
        }
    }

    private static ArrayList<String> getStringList(JsonArray jsonArray) {
        ArrayList<String> list = new ArrayList<>();
        for(JsonElement element : jsonArray) {
            list.add(element.getAsString());
        }
        return list;
    }

    private static File getConfigFile() {
        return new File("./config/anaku_status_bars.json");
    }
}

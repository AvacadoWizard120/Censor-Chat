package com.confusingfool.censor_chat.client;

import com.confusingfool.censor_chat.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CensorChatClient implements ClientModInitializer
{
    public static Config config;
    @Override
    public void onInitializeClient() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "censor_chat.json");
        if (!configFile.exists())
        {
            generateDefaultConfig(configFile);
        }
        config = Config.load(configFile);

        System.out.println("CensorChat initialized.");
    }

    private void generateDefaultConfig(File configFile)
    {
        Config defaultConfig = new Config();
        defaultConfig.deleteMessage = false;
        defaultConfig.blacklist = new String[]{"badword1", "badword2"};
        defaultConfig.repeatCharForLengthOfWord = false;
        defaultConfig.customChar = false;
        defaultConfig.replaceWith = "*";
        defaultConfig.repeatChar = 1;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(configFile))
        {
            gson.toJson(defaultConfig, writer);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

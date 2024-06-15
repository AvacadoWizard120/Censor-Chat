package com.confusingfool.censor_chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Config
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean deleteMessage;

    public String[] blacklist;

    public boolean repeatCharForLengthOfWord;

    public boolean customChar;

    public String replaceWith;

    public int repeatChar;


    public static Config load(File file)
    {
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))
        {
            return GSON.fromJson(reader, Config.class);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean containsBlackListedWord(String message)
    {
        for (String word : blacklist)
        {
            if (message.contains(word))
            {
                return true;
            }
        }
        return false;
    }
}

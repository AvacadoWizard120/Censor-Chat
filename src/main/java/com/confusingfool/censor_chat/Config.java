package com.confusingfool.censor_chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

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
        try (Reader reader = new FileReader(file))
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

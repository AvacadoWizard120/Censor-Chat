package io.github.avacadowizard120.censor_chat;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class Config
{
    public static final ForgeConfigSpec GENERAL_SPEC;
    public static ForgeConfigSpec.BooleanValue deleteMessage;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> blacklist;
    public static ForgeConfigSpec.BooleanValue repeatCharForLengthOfWord;
    public static ForgeConfigSpec.BooleanValue customChar;
    public static ForgeConfigSpec.ConfigValue<String> replaceWith;

    public static ForgeConfigSpec.IntValue repeatChar;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder)
    {
        deleteMessage = builder.define("delete_message", false);
        blacklist = builder.defineList("blacklist", Arrays.asList("badword1", "badword2"), entry -> true);
        repeatCharForLengthOfWord = builder.define("repeat_char_for_length_of_word", false);
        customChar = builder.define("custom_char", false);
        replaceWith = builder.define("replace_with", "*");
        repeatChar = builder.defineInRange("repeat_char", 1, 1, 100);
    }

    public static Boolean containsBlacklistedWord(String message)
    {
        for (String word : blacklist.get())
        {
            if (message.contains(word))
            {
                return true;
            }
        }
        return false;
    }
}

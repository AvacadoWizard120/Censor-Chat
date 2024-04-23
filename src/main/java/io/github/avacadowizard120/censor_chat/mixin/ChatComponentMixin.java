package io.github.avacadowizard120.censor_chat.mixin;

import io.github.avacadowizard120.censor_chat.Config;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatComponent.class)
public class ChatComponentMixin
{
    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"), cancellable = true)
    private void onAddMessage(Component pChatComponent, CallbackInfo ci)
    {
        String messageText = pChatComponent.getString();
        if (Config.containsBlacklistedWord(messageText))
        {
            if (Config.deleteMessage.get())
            {
                ci.cancel();
            }else {
                if (Config.customChar.get()) // Custom Character
                {
                    if (Config.repeatCharForLengthOfWord.get()) // Custom Character and Repeat
                    {
                        StringBuilder censoredMessage = new StringBuilder(messageText);
                        for (String blacklistedWord : Config.blacklist.get())
                        {
                            Pattern pattern = Pattern.compile("(?i)" + blacklistedWord);
                            Matcher matcher = pattern.matcher(censoredMessage);
                            while (matcher.find())
                            {
                                int start = matcher.start();
                                int end = matcher.end();
                                censoredMessage.replace(start, end, Config.replaceWith.get().repeat(end - start));
                            }
                        }
                        String censoredText = censoredMessage.toString();
                        ci.cancel();
                        ((ChatComponent)(Object)this).addMessage(Component.nullToEmpty(censoredText));
                    } else if (Config.repeatChar.get() > 1) // Custom Character CUSTOM REPEAT
                    {
                        String censoredMessage = messageText.replaceAll("(?i)" + String.join("|", Config.blacklist.get()), Config.replaceWith.get().repeat(Config.repeatChar.get()));
                        String censoredText = censoredMessage.toString();
                        ci.cancel();
                        ((ChatComponent)(Object)this).addMessage(Component.nullToEmpty(censoredText));
                    } else {
                        String censoredMessage = messageText.replaceAll("(?i)" + String.join("|", Config.blacklist.get()), Config.replaceWith.get());
                        ci.cancel();
                        ((ChatComponent)(Object)this).addMessage(Component.nullToEmpty(censoredMessage));
                    }
                } else {   // No Custom Character
                    if (Config.repeatCharForLengthOfWord.get()) // NO Custom Character and Repeat
                    {
                        StringBuilder censoredMessage = new StringBuilder(messageText);
                        for (String blacklistedWord : Config.blacklist.get())
                        {
                            Pattern pattern = Pattern.compile("(?i)" + blacklistedWord);
                            Matcher matcher = pattern.matcher(censoredMessage);
                            while (matcher.find())
                            {
                                int start = matcher.start();
                                int end = matcher.end();
                                censoredMessage.replace(start, end, " ".repeat(end - start));
                            }
                        }
                        String censoredText = censoredMessage.toString();
                        ci.cancel();
                        ((ChatComponent)(Object)this).addMessage(Component.nullToEmpty(censoredText));
                    } else if (Config.repeatChar.get() > 1) // NO Custom Character CUSTOM REPEAT
                    {
                        String censoredMessage = messageText.replaceAll("(?i)" + String.join("|", Config.blacklist.get()), " ".repeat(Config.repeatChar.get()));
                        ci.cancel();
                        ((ChatComponent)(Object)this).addMessage(Component.nullToEmpty(censoredMessage));
                    } else {
                        String censoredMessage = messageText.replaceAll("(?i)" + String.join("|", Config.blacklist.get()), " ");
                        ci.cancel();
                        ((ChatComponent)(Object)this).addMessage(Component.nullToEmpty(censoredMessage));
                    }
                }
            }
        }
    }
}
package com.confusingfool.censor_chat.mixin;

import com.confusingfool.censor_chat.client.CensorChatClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void onAddMessage(Text message, CallbackInfo ci) {
        String messageText = message.getString();
        if (CensorChatClient.config != null && CensorChatClient.config.containsBlackListedWord(messageText)) {
            if (CensorChatClient.config.deleteMessage)
            {
                ci.cancel();
            } else {
                if (CensorChatClient.config.customChar) // Custom Character
                {
                    if (CensorChatClient.config.repeatCharForLengthOfWord) // Custom Character and Repeat
                    {
                        StringBuilder censoredMessage = new StringBuilder(messageText);
                        for (String blacklistedWord : CensorChatClient.config.blacklist)
                        {
                            Pattern pattern = Pattern.compile("(?i)" + blacklistedWord);
                            Matcher matcher = pattern.matcher(censoredMessage);
                            while (matcher.find())
                            {
                                int start = matcher.start();
                                int end = matcher.end();
                                censoredMessage.replace(start, end, CensorChatClient.config.replaceWith.repeat(end - start));
                            }
                        }
                        Text censoredText = Text.of(censoredMessage.toString());
                        ci.cancel();
                        ((ChatHud)(Object)this).addMessage(censoredText);
                    } else if (CensorChatClient.config.repeatChar > 1) // Custom Character CUSTOM REPEAT
                    {
                        String censoredMessage = messageText.replaceAll("(?i)" + String.join("|", CensorChatClient.config.blacklist), CensorChatClient.config.replaceWith.repeat(CensorChatClient.config.repeatChar));
                        Text censoredText = Text.of(censoredMessage);
                        ci.cancel();
                        ((ChatHud)(Object)this).addMessage(censoredText);
                    } else {
                        String censoredMessage = messageText.replaceAll("(?i)" + String.join("|", CensorChatClient.config.blacklist), CensorChatClient.config.replaceWith);
                        Text censoredText = Text.of(censoredMessage);
                        ci.cancel();
                        ((ChatHud)(Object)this).addMessage(censoredText);
                    }
                } else {   // No Custom Character
                    if (CensorChatClient.config.repeatCharForLengthOfWord) // NO Custom Character and Repeat
                    {
                        StringBuilder censoredMessage = new StringBuilder(messageText);
                        for (String blacklistedWord : CensorChatClient.config.blacklist)
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
                        Text censoredText = Text.of(censoredMessage.toString());
                        ci.cancel();
                        ((ChatHud)(Object)this).addMessage(censoredText);
                    } else if (CensorChatClient.config.repeatChar > 1) // NO Custom Character CUSTOM REPEAT
                    {
                        String censoredMessage = messageText.replaceAll("(?i)" + String.join("|", CensorChatClient.config.blacklist), " ".repeat(CensorChatClient.config.repeatChar));
                        Text censoredText = Text.of(censoredMessage);
                        ci.cancel();
                        ((ChatHud)(Object)this).addMessage(censoredText);
                    } else {
                        String censoredMessage = messageText.replaceAll("(?i)" + String.join("|", CensorChatClient.config.blacklist), " ");
                        Text censoredText = Text.of(censoredMessage);
                        ci.cancel();
                        ((ChatHud)(Object)this).addMessage(censoredText);
                    }
                }
            }
        }
    }
}
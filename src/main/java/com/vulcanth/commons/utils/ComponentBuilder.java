package org.nebula.core.bukkit.utility;

import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author Onyzn
 */
@AllArgsConstructor
public class ComponentBuilder {

  private TextComponent message;

  public ComponentBuilder url(String ur) {
    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ur));
    return this;
  }

  public ComponentBuilder suggest(String suggest) {
    message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggest));
    return this;
  }

  public ComponentBuilder command(String command) {
    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
    return this;
  }

  public ComponentBuilder tooltip(String text) {
    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(text)}));
    return this;
  }

  public ComponentBuilder append(ComponentBuilder other) {
    return append(other.build());
  }

  public ComponentBuilder append(TextComponent other) {
    message.addExtra(other);
    return this;
  }

  public TextComponent build() {
    return this.message;
  }

  public static ComponentBuilder of(TextComponent message) {
    return new ComponentBuilder(message);
  }

  public static ComponentBuilder of(String message) {
    return new ComponentBuilder(new TextComponent(message));
  }
}

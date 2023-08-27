package com.vulcanth.commons.player.scoreboard;

import com.vulcanth.commons.Main;
import com.vulcanth.commons.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public abstract class ScoreboardManager {

    public abstract void setScoreboard();

    public abstract void update();

    public abstract void setTitle();

    private Player player;
    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private Map<Integer, String> FIND_BY_LINE;
    private int lines;
    private BukkitTask task;
    private int updateTime;

    public ScoreboardManager(Player player, int updateTime) {
        this.player = player;
        this.FIND_BY_LINE = new HashMap<>();
        this.lines = 0;
        this.updateTime = updateTime;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = this.scoreboard.registerNewObjective("VulcanthSB", "infomations");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(this.scoreboard);
        this.updateScore();
    }

    public void setTitle(String title) {
        Objective objective = this.scoreboard.getObjective("VulcanthSB");
        objective.setDisplayName(StringUtils.formatColors(title));
    }

    public void destroy() {
        this.player.getScoreboard().getObjectives().clear();
        this.player = null;
        this.scoreboard = null;
        this.FIND_BY_LINE.clear();
        this.FIND_BY_LINE = null;
        this.task.cancel();
        this.task = null;
        this.updateTime = 0;
        this.lines = 0;
    }

    public void addLine(String line) {
        String finalLine = StringUtils.formatColors(line);
        int lineNow = 16 - lines;
        lines++;
        Team team = this.scoreboard.getTeam(String.valueOf(lineNow));
        if (team == null) {
            String entry = ChatColor.values()[lineNow - 1].toString() + "§r";
            team = this.scoreboard.registerNewTeam(String.valueOf(lineNow));
            String prefix = finalLine.substring(0, Math.min(finalLine.length(), 16));
            if (prefix.endsWith("§") && prefix.length() == 16) {
                prefix = prefix.substring(0, prefix.length() - 1);
            }
            finalLine = finalLine.substring(prefix.length());

            String suffix = StringUtils.getLastColor(prefix) + finalLine;
            suffix = suffix.substring(0, Math.min(16, suffix.length()));
            if (suffix.endsWith("§")) {
                suffix = suffix.substring(0, suffix.length() - 1);
            }

            team.setSuffix(suffix);
            team.setPrefix(prefix);
            team.addEntry(entry);
            FIND_BY_LINE.put(lineNow, entry);
        } else {
            String prefix = finalLine.substring(0, Math.min(finalLine.length(), 16));
            if (prefix.endsWith("§") && prefix.length() == 16) {
                prefix = prefix.substring(0, prefix.length() - 1);
            }
            finalLine = finalLine.substring(prefix.length());

            String suffix = StringUtils.getLastColor(prefix) + finalLine;
            suffix = suffix.substring(0, Math.min(16, suffix.length()));
            if (suffix.endsWith("§")) {
                suffix = suffix.substring(0, suffix.length() - 1);
            }

            team.setSuffix(suffix);
            team.setPrefix(prefix);
        }

        Score score = this.scoreboard.getObjective("VulcanthSB").getScore(FIND_BY_LINE.get(lineNow));
        score.setScore(lineNow);
    }

    public void setLine(String line, int lineSlot) {
        String finalLine = StringUtils.formatColors(line);
        Team team = this.scoreboard.getTeam(String.valueOf(lineSlot));
        String prefix = finalLine.substring(0, Math.min(finalLine.length(), 16));
        if (prefix.endsWith("§") && prefix.length() == 16) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }
        finalLine = finalLine.substring(prefix.length());

        String suffix = StringUtils.getLastColor(prefix) + finalLine;
        suffix = suffix.substring(0, Math.min(16, suffix.length()));
        if (suffix.endsWith("§")) {
            suffix = suffix.substring(0, suffix.length() - 1);
        }

        team.setSuffix(suffix);
        team.setPrefix(prefix);
        Score score = this.scoreboard.getObjective("VulcanthSB").getScore(FIND_BY_LINE.get(lineSlot));
        score.setScore(lineSlot);
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    private void updateScore() {
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            lines = 0;
            this.update();
        }, 0L, this.updateTime);
    }

    public Player getPlayer() {
        return this.player;
    }
}

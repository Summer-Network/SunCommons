package com.vulcanth.commons.utils;

import java.util.Timer;
import java.util.TimerTask;

//Sistema desenvolvido para fazer que, toda vez que der um horário, a aplicação é encerrada. Serve para fins de deixar um upTime de no máximo de 24hrs.
public class ResetAplication {
    public static void scheduleShutdown(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Reiniciando os sistemas...");
                System.exit(0);
            }
        };

        long delay = (hours * 3600L + minutes * 60L + seconds) * 1000;
        new Timer().schedule(task, delay);
    }
}

package me.ep.extrakits.Cooldown;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;

public class Cooldown {

    static Map<String, Long> map = new HashMap<String, Long>();
    private FileConfiguration fc;
    private File f;

    public Cooldown(FileConfiguration dFileC, File dFile) {
        fc = dFileC;
        f = dFile;
    }

    public void addDelay(String id) {
        if (fc == null || f == null) return;
        fc.set("Delay." + id, System.currentTimeMillis());
        map.put(id, System.currentTimeMillis());
        try {
            fc.save(f);
        } catch (IOException e) {}
    }

    public long getDelay(String id) {
        if (map.containsKey(id)) return map.get(id);
        if (fc.contains("Delay." + id)) return fc.getLong("Delay." + id);
        return System.currentTimeMillis();
    }

    public boolean acabouDelay(String id, long l) {
        String coisa = getDelayString(id, l);
        if (coisa.equals("agora")) {
            return true;
        }
        return false;
    }

    public String getDelayString(String id, long l) {
        long tempoAntes = getDelay(id);
        long tempoAtual = System.currentTimeMillis() - l;
        String coisa = formatDifferenceStr((tempoAntes - tempoAtual));
        return coisa;
    }

    public void removeDelay(String id) {
        if (map.containsKey(id)) map.remove(id);
        if (fc.contains("Delay." + id)) {
            fc.set("Delay." + id, null);
            try {
                fc.save(f);
            } catch (IOException e) {}
        }
    }

    public boolean temDelay(String id) {
        if (map.containsKey(id)) return true;
        if (fc.contains("Delay." + id)) return true;
        return false;
    }


    public String formatDifferenceStr(long time) {
        if (time == 0) {
            return "never";
        }
        long day = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) - (day * 24);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) - (TimeUnit.MILLISECONDS.toHours(time) * 60);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - (TimeUnit.MILLISECONDS.toMinutes(time) * 60);
        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append(" ").append(day == 1 ? "dia" : "dias").append(" ");
        }
        if (hours > 0) {
            sb.append(hours).append(" ").append(hours == 1 ? "hora" : "horas").append(" ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" ").append(minutes == 1 ? "minuto" : "minutos").append(" ");
        }
        if (seconds > 0) {
            sb.append(seconds).append(" ").append(seconds == 1 ? "segundo" : "segundos");
        }
        String diff = sb.toString();
        return diff.isEmpty() ? "agora" : diff;
    }

    public String getDifferenceFormatStr(long timestamp) {
        return formatDifferenceStr(timestamp - (System.currentTimeMillis()));
    }

    // Copyright essentials, all credits to them for this.
    public long parseDateDiff(String time, boolean future) throws Exception {
        Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
                + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find()) {
            if (m.group() == null || m.group().isEmpty()) {
                continue;
            }
            for (int i = 0; i < m.groupCount(); i++) {
                if (m.group(i) != null && !m.group(i).isEmpty()) {
                    found = true;
                    break;
                }
            }
            if (found) {
                if (m.group(1) != null && !m.group(1).isEmpty()) {
                    years = Integer.parseInt(m.group(1));
                }
                if (m.group(2) != null && !m.group(2).isEmpty()) {
                    months = Integer.parseInt(m.group(2));
                }
                if (m.group(3) != null && !m.group(3).isEmpty()) {
                    weeks = Integer.parseInt(m.group(3));
                }
                if (m.group(4) != null && !m.group(4).isEmpty()) {
                    days = Integer.parseInt(m.group(4));
                }
                if (m.group(5) != null && !m.group(5).isEmpty()) {
                    hours = Integer.parseInt(m.group(5));
                }
                if (m.group(6) != null && !m.group(6).isEmpty()) {
                    minutes = Integer.parseInt(m.group(6));
                }
                if (m.group(7) != null && !m.group(7).isEmpty()) {
                    seconds = Integer.parseInt(m.group(7));
                }
                break;
            }
        }
        if (!found) {
            throw new Exception("Illegal Date");
        }
        if (years > 20) {
            throw new Exception("Illegal Date");
        }
        Calendar c = new GregorianCalendar();
        if (years > 0) {
            c.add(Calendar.YEAR, years * (future ? 1 : -1));
        }
        if (months > 0) {
            c.add(Calendar.MONTH, months * (future ? 1 : -1));
        }
        if (weeks > 0) {
            c.add(Calendar.WEEK_OF_YEAR, weeks * (future ? 1 : -1));
        }
        if (days > 0) {
            c.add(Calendar.DAY_OF_MONTH, days * (future ? 1 : -1));
        }
        if (hours > 0) {
            c.add(Calendar.HOUR_OF_DAY, hours * (future ? 1 : -1));
        }
        if (minutes > 0) {
            c.add(Calendar.MINUTE, minutes * (future ? 1 : -1));
        }
        if (seconds > 0) {
            c.add(Calendar.SECOND, seconds * (future ? 1 : -1));
        }
        return c.getTimeInMillis();
    }

}

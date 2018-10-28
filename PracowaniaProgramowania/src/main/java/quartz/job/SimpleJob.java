package quartz.job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.temporal.ChronoUnit;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.impl.multimap.bag.HashBagMultimap;
import java.util.*;

public class SimpleJob implements org.quartz.Job {

    //False = Break; dayOfWeek=0 -> it's sunday
    public static final boolean checkBreak (int dayOfWeek, LocalTime now){
        LocalTime[] zaj = new LocalTime[6];
        LocalTime[] prz = new LocalTime[6];
        zaj[0] = LocalTime.of(8, 15);
        zaj[1] = LocalTime.of(10, 00);
        zaj[2] = LocalTime.of(11, 45);
        zaj[3] = LocalTime.of(13, 45);
        zaj[4] = LocalTime.of(15, 30);
        zaj[5] = LocalTime.of(17, 15);

        prz[0] = LocalTime.of(9, 45);
        prz[1] = LocalTime.of(11, 30);
        prz[2] = LocalTime.of(13, 15);
        prz[3] = LocalTime.of(15, 15);
        prz[4] = LocalTime.of(17, 00);
        prz[5] = LocalTime.of(18, 45);
        switch (dayOfWeek) {
            case (1): {
                return false;
            }
            case (7): {
                return false;
            }
            default: {
                if (now.getHour() > 19 || (now.getHour() == 18 && now.getMinute() >= 45)) {
                    return false;
                }

                if (now.getHour() < 8 || (now.getHour() == 8 && now.getMinute() < 15)) {
                    return false;
                }
            }

            for (int i = 0; i < 6; i++) {
                if (zaj[i].isBefore(now) && prz[i].isAfter(now)) {
                    return true;
                }

                if (prz[i].isBefore(now) && zaj[i].isAfter(now)) {
                    return false;
                }

                if (now.equals(zaj[i])) {
                    return true;
                }

                if (now.equals(prz[i])) {
                    return false;
                }

            }
        }



        return false;
    };



    static final void CheckActualStatus() {
        LocalTime now = LocalTime.now();
        long minutesBetween;
        Date now1 = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now1);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        boolean timestatus = checkBreak(dayOfWeek, now);
        LocalTime midnight = LocalTime.MAX;
        LocalTime[] zaj = new LocalTime[6];
        LocalTime[] prz = new LocalTime[6];
        zaj[0] = LocalTime.of(8, 15);
        zaj[1] = LocalTime.of(10, 00);
        zaj[2] = LocalTime.of(11, 45);
        zaj[3] = LocalTime.of(13, 45);
        zaj[4] = LocalTime.of(15, 30);
        zaj[5] = LocalTime.of(17, 15);

        prz[0] = LocalTime.of(9, 45);
        prz[1] = LocalTime.of(11, 30);
        prz[2] = LocalTime.of(13, 15);
        prz[3] = LocalTime.of(15, 15);
        prz[4] = LocalTime.of(17, 00);
        prz[5] = LocalTime.of(18, 45);



        switch (dayOfWeek) {
            case (1): {
                minutesBetween = ChronoUnit.MINUTES.between(now, midnight);
                minutesBetween += 60 * 8 + 15;
                PrintActualStatus(timestatus, minutesBetween);
                return;
            }
            case (7): {
                minutesBetween = ChronoUnit.MINUTES.between(now, midnight);
                minutesBetween += 60 * 32 + 15;
                PrintActualStatus(timestatus, minutesBetween);
                return;
            }
            default: {
                if (now.getHour() > 19 || (now.getHour() == 18 && now.getMinute() >= 45)) {
                    minutesBetween = ChronoUnit.MINUTES.between(now, midnight);
                    minutesBetween += 60 * 8 + 15;
                    if (dayOfWeek == 6) {
                        minutesBetween += 60 * 48;
                    }
                    PrintActualStatus(timestatus, minutesBetween);
                    return;
                }

                if (now.getHour() < 8 || (now.getHour() == 8 && now.getMinute() < 15)) {
                    minutesBetween = ChronoUnit.MINUTES.between(now, zaj[0]);
                    PrintActualStatus(timestatus, minutesBetween);
                    return;
                }
            }

            for (int i = 0; i < 6; i++) {
                if (zaj[i].isBefore(now) && prz[i].isAfter(now)) {
                    minutesBetween = ChronoUnit.MINUTES.between(now, prz[i]);
                    PrintActualStatus(timestatus, minutesBetween);
                    return;
                }

                if (prz[i].isBefore(now) && zaj[i].isAfter(now)) {
                    minutesBetween = ChronoUnit.MINUTES.between(now, zaj[i + 1]);
                    PrintActualStatus(timestatus, minutesBetween);
                    return;
                }

                if (now.equals(zaj[i])) {
                    minutesBetween = ChronoUnit.MINUTES.between(now, prz[i]);
                    PrintActualStatus(timestatus, minutesBetween);
                    return;
                }

                if (now.equals(prz[i])) {
                    minutesBetween = ChronoUnit.MINUTES.between(now, zaj[i + 1]);
                    PrintActualStatus(timestatus, minutesBetween);
                    return;
                }

            }
        }
    }


    static final void PrintActualStatus(boolean timestatus, long minutes) {
        minutes++;
        if (timestatus == false) {
            System.out.println("Zostalo " + minutes + " minut do konca przerwy.");
        } else {
            System.out.println("Zostalo " + minutes + " minut do konca zajec.");
        }
    }


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try {
            PrintWriter printfile = new PrintWriter("odp.txt");

            quartz.scheduler.SimpleScheduler.Person k = new quartz.scheduler.SimpleScheduler.Person("", "", "", "");

            HashBagMultimap<String, quartz.scheduler.SimpleScheduler.Person> persons = k.getPersons();


            SetIterable<String> keys;
            keys = persons.keySet();

            MutableList<String> list = keys.toList();

            Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
            list.forEach(name -> {
                printfile.println(name);
                persons.get(name).forEach(personname -> {
                    printfile.println("      " + personname.getPesel() + " " + personname.getName() + " " + personname.getSurname());
                    //System.out.println(personname.getPesel() + " " + personname.getName() + " " + personname.getSurname());
                });
            });
            printfile.close();
/* 97102906482
99051506146
98060606476*/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        if (LocalTime.now().getSecond() == 0) {
            CheckActualStatus();

        }

    }


}

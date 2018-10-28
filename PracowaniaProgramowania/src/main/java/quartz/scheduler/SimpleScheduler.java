package quartz.scheduler;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.impl.multimap.bag.HashBagMultimap;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import quartz.job.SimpleJob;

import java.util.List;
import java.util.Scanner;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;

import static org.quartz.TriggerBuilder.newTrigger;



public class SimpleScheduler {


    static public final boolean peselcontrol(String pesel) {

        int[] k = new int[11];
        int sum = 0;
        if (pesel.length() != 11) {
            System.err.println("Error - pesel is too short or too long!");
            return false;
        }
        for (int i = 0; i < pesel.length(); i++) {
            k[i] = (Character.getNumericValue(pesel.charAt(i)));
            if (k[i] > 9) {
                System.err.println("Error - pesel must not contain letters.");
                return false;
            }
        }
        sum = (9 * k[0] + 7 * k[1] + 3 * k[2] + k[3] + 9 * k[4] + 7 * k[5] + 3 * k[6] + k[7] + 9 * k[8] + 7 * k[9]) % 10;
        if (k[10] != sum) {
            System.err.println("Error - pesel is wrong.");
            return false;
        }
        return true;
    }

    ;

    public static class Person {
        String name, surname, pesel, city;
        static HashBagMultimap<String, Person> persons;


        public Person(String name, String surname, String pesel, String city) {
            this.name = name;
            this.surname = surname;
            this.pesel = pesel;
            this.city = city;
        }

        public static HashBagMultimap<String, Person> getPersons() {
            return persons;
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public String getPesel() {
            return pesel;
        }
    }

    ;



    public static void main(String[] args) throws InterruptedException {

        try {
            Person newperson;
            List<Person> personList = null;
            SetIterable<String> keys;
            MutableList<String> cityList = null;
            String city, nam, surname, pesel;//w nim zapiszemy swoje imie
            Scanner inputscan; //obiekt do odebrania danych od użytkownika


            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobDetail job = newJob(SimpleJob.class)
                    .withIdentity("job1", "group1")
                    .build();
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(cronSchedule("0/30 * * ? * * *"))
                    .build();

            scheduler.scheduleJob(job, trigger);

            scheduler.start();

            newperson = new Person("","","","");
            newperson.persons = HashBagMultimap.newMultimap();

            while (true) {
                inputscan = new Scanner(System.in);
                city = inputscan.nextLine();
                nam = inputscan.next();
                surname = inputscan.next();
                pesel = inputscan.next();

                if (peselcontrol(pesel) == true) {
                    newperson = new Person(nam, surname, pesel, city);
                    keys = newperson.persons.keySet();
                    cityList = keys.toList();
                    personList = newperson.persons.valuesView().toList();
                    for (int i = 0; i < personList.size(); i++) {
                        String k = personList.get(i).getPesel();

                        if (pesel.equals(k)) {
                            for (int j = 0; j < cityList.size(); j++) {
                                newperson.persons.remove(cityList.get(j), personList.get(i));
                            }
                        }
                    }
                    newperson.persons.put(city, newperson);


                }

            }


        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }
}

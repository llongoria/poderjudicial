package mx.com.wcontact.poderjudicial.util;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public final class Fecha {

    public static List<LocalDate> getDatesBetween( LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate).collect(Collectors.toList());
    }

    public static boolean isBetweenSqlServerDates(LocalDate localDate){
        LocalDate min = LocalDate.of(1900,1,1);
        LocalDate max = LocalDate.of(2100,1,1);
        return (localDate.isAfter(min) || localDate.isEqual(min)) && (localDate.isBefore(max) || localDate.isEqual(max));
    }
}

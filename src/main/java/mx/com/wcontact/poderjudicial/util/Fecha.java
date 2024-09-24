package mx.com.wcontact.poderjudicial.util;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public final class Fecha {

    public static List<LocalDate> getDatesBetween( LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate).collect(Collectors.toList());
    }
}

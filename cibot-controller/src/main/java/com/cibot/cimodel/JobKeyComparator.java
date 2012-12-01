package com.cibot.cimodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * @author Uefix
 */
@Component
public class JobKeyComparator implements Comparator<String> {

    @Autowired
    private CIModel model;

    public JobKeyComparator() {
    }

    public JobKeyComparator(CIModel model) {
        this.model = model;
    }

    @Override
    public int compare(String k1, String k2) {
        BuildStatus status1 = model.getStatusForJob(k1);
        BuildStatus status2 = model.getStatusForJob(k2);

        if (status1 == status2) {
            return k1.compareTo(k2);
        }

        return status2.ordinal() - status1.ordinal();
    }
}
